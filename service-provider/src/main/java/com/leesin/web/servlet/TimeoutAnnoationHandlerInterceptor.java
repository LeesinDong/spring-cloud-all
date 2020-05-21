package com.leesin.web.servlet;

import com.leesin.annotation.Timeouted;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.concurrent.*;
import java.util.stream.Stream;


/*
 * {@link Timeout @Timeout} 注解处理 web mvc 拦截器
 * @see HandlerInterceptor
 * @
 *
 * @See
 * */
public class TimeoutAnnoationHandlerInterceptor implements HandlerInterceptor {

    private ExecutorService executorService = Executors.newFixedThreadPool(2);

    @Override
    // preHandle方法
    //在Controller处理之前进行调用。
    //重写preHandle方法，在请求发生前执行。
    //这个方法只会在当前这个Interceptor的preHandle方法返回值为true的时候才会执行。

    //postHandle是进行处理器拦截用的，它的执行时间是在处理器进行处理之后，
    // 也就是在Controller的方法调用之后执行，但是它会在DispatcherServlet进行视图的渲染之前执行
    //也就是说在这个方法中你可以对ModelAndView进行操作。
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1. 拦截处理方法（spring web mvc 内建实现 HandlerInterceptor）
        //老师也解释不清楚
        //2. 得到被拦截的方法对象（handler对象在spring web mvc注解编程中 永远是HandlerMethod）
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            //2.1 通过HandlerMethod获取Method对象
            Method method = handlerMethod.getMethod();
            //3. 通过method获取标注的@TimeOut注解
            Timeouted timeout = method.getAnnotation(Timeouted.class);
            if (timeout != null) {//如果标注的话
                //得到数组的对象
                //获取timeout中的属性
                Object bean = handlerMethod.getBean();
                long value = timeout.value();
                TimeUnit timeUnit = timeout.timeUnit();
                String fallback = timeout.fallback();
                // 5. 根据以上数据构造future超时时间
                Future<Object> future = executorService.submit(new Callable<Object>() {
                    @Override
                    public Object call() throws Exception {
                        return method.invoke(bean);
                    }
                });
                //超时时间，超时时间单位
                Object returnValue = null;
                try {
                     //正常处理
                     //6. 执行被拦截的方法
                     returnValue = future.get(value, timeUnit);
                } catch (TimeoutException e) {
                    //补偿处理
                    // 7. 如果失败调用fallback方法
                    returnValue = invokeFallbackMethod(handlerMethod,bean,fallback);
                }
                //8. 返回执行结果（当前实现是存在缺陷的）
                //有时间可以通过HandlerMethodReturnValueHandler实现
                response.getWriter().write(String.valueOf(returnValue));
                return false;
            }
        }
        //返回true，不然后面没法执行了
        return true;
    }

    private Object invokeFallbackMethod(HandlerMethod handlerMethod, Object bean, String fallback) throws Exception {
        Method fallbackMethod = findFallbackMethod(handlerMethod, bean, fallback);
    //    hello和 fallback的方法签名字是一样的，什么是签名？顺序和类型是一样的
    //    7.1 查找 fallback方法
        return fallbackMethod.invoke(bean);
    }

    private Method findFallbackMethod(HandlerMethod handlerMethod, Object bean, String fallbackMethodName) throws NoSuchMethodException {
        //因为fallback和hello是在一个类中，这里找到那个bean类
        // 通过被拦截方法的参数类型列表结合方法名，从同一类中找到fallback方法
        Class beanClass = bean.getClass();
        //这里得到的是包装类，需要的到class 类型
        //这个是method参数，还可以保存返回值
        //MethodParameter很重要
        MethodParameter[] methodParameters = handlerMethod.getMethodParameters();
        Class[] parameterTypes = Stream.of(methodParameters) //转化成stream
                .map(MethodParameter::getParameterType) //得到class
                .toArray(Class[]::new);                                //Stream<Class> -> Class[]
        Method fallbackMethod = beanClass.getMethod(fallbackMethodName, parameterTypes);
        return fallbackMethod;
    }

}
