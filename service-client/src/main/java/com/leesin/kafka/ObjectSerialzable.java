package com.leesin.kafka;


import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/22 0022 9:16
 * @modified By:
 */
public class ObjectSerialzable implements Serializer<Serializable> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public byte[] serialize(String topic, Serializable data) {
        System.out.printf("当前topic：%s ，序列化对象：%s \n",topic,data);
        //ByteArrayOutputStream close方法其实是空实现
        //因为close 方法主要是关闭一些数据，但是byteArray本身就是内存性的东西，不存在关不关，就好像new String 要kill掉
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] dataArray = null;
        try (ObjectOutputStream oos = new ObjectOutputStream(outputStream)){// Java & try resource{}
            //将对象写入到ObjectOutpuStream
            oos.writeObject(data);
            //将写入的数据，通过字节数组方式获取
            dataArray = outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataArray;
    }

    @Override
    public void close() {

    }
}
