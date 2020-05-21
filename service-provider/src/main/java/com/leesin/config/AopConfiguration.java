package com.leesin.config;

import com.leesin.annotation.Limited;
import com.leesin.annotation.Timeouted;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.*;
import java.util.stream.Stream;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/21 0021 19:39
 * @modified By:
 */
@Aspect
@Configuration
public class AopConfiguration {
    //这里一定要是Semaphore
    private volatile Semaphore semaphore;

    private ExecutorService executorService = Executors.newFixedThreadPool(2);


    //一个方法对应一个类，一个类对应一个classloader，所以方法也是唯一的
    //一个方法对应一个semaphore
    private Map<Method, Semaphore> semaphoreCache = new ConcurrentHashMap<>();

    //    只拦截出现limited注解的时候
    //@Aroud是围着他，需要主动去触发
    //@Before 是在方法执行之前去拦截他，不需要触发
    @Around("@annotation(com.leesin.annotation.Limited)")
    public Object aroundLimitedMethodInvocation(ProceedingJoinPoint pjp) throws Throwable {
        Object returnValue = null;


        //得到签名
        Signature signature = pjp.getSignature();
        //得到方法参数
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            //拿到方法
            Method method = methodSignature.getMethod();
            Semaphore semaphore = getSemaphore(method);
            //    4.在方法主动调用之前调用semaphore# acquire()
            try {
                semaphore.acquire();
                //执行完成。
                //    5.被拦截的方法执行
                returnValue = pjp.proceed();
            } finally {
                //因为pjp.proceed();可能会失败
                //    6. 在方法执行后，调用semaphore#release()
                semaphore.release();
            }
            // //方法名称
            // String methodName = signature.getName();
            // //bean对象
            // Object bean = pjp.getTarget();
        }
        return returnValue;
    }

    public Semaphore getSemaphore(Method method) {
        return semaphoreCache.computeIfAbsent(method, k -> {
            // 1. 得到 limited 注解
            Limited limited = method.getAnnotation(Limited.class);
            //    2,。 得到limited 注解中的属性 10
            int permits = limited.value();
            //     3。根据属性构造demaphore（10）
            return new Semaphore(permits);
        });
    }




    /*
     *
     * timeout的
     *
     * */

    // 1. 拦截处理方法（Spring + AspectJ）
    @Around("@annotation(com.leesin.annotation.Timeouted)")
    public Object aroundTimeoutMethodInvocation(ProceedingJoinPoint pjp) throws Throwable {
        Object returnValue = null;
        // 2. 得到被拦截的方法对象
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            Method method = methodSignature.getMethod();
            // 3. 通过 Method 获取标注的 @Timeout 注解
            Timeouted timeout = method.getAnnotation(Timeouted.class);
            if (timeout != null) { // 如果标注的话
                // 4. 获取 @Timeout 注解中的属性
                Long value = timeout.value();
                TimeUnit timeUnit = timeout.timeUnit();
                String fallbackMethodName = timeout.fallback();
                // 方法参数
                Object[] arguments = pjp.getArgs();
                // 5. 根据以上数据构造 Future 超时时间
                Future<Object> future = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        try {
                            return pjp.proceed(arguments);
                        } catch (Throwable throwable) {
                            throw new Exception(throwable);
                        }
                    }
                });
                try {
                    // 6. 执行被拦截的方法
                    returnValue = future.get(value, timeUnit); // 正常处理
                } catch (TimeoutException e) {
                    // 7. 如果失败，调用 fallback 方法
                    returnValue = invokeFallbackMethod(method, pjp.getTarget(), fallbackMethodName, arguments);      // 补偿处理
                }

            }
        }
        return returnValue;
    }


    private Object invokeFallbackMethod(Method method, Object bean, String fallback, Object[] arguments) throws Exception {
        // 7.1 查找 fallback 方法
        Method fallbackMethod = findFallbackMethod(method, bean, fallback);
        return fallbackMethod.invoke(bean, arguments);
    }

    private Method findFallbackMethod(Method method, Object bean, String fallbackMethodName) throws
            NoSuchMethodException {
        // 通过被拦截方法的参数类型列表结合方法名，从同一类中找到 fallback 方法
        Class beanClass = bean.getClass();
        Method fallbackMethod = beanClass.getMethod(fallbackMethodName, method.getParameterTypes());
        return fallbackMethod;
    }

}
