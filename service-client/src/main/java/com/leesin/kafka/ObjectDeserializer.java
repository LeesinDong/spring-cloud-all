package com.leesin.kafka;


import org.apache.kafka.common.serialization.Deserializer;

import java.io.*;
import java.util.Map;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/22 0022 11:10
 * @modified By:
 */
public class ObjectDeserializer implements Deserializer<Serializable> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public Serializable deserialize(String topic, byte[] data) {
        Serializable object = null;
        ByteArrayInputStream inputStream = new ByteArrayInputStream(data);
        try (ObjectInputStream ois = new ObjectInputStream(inputStream)){
            object = (Serializable) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {//java 7 mutiple catch
            throw new RuntimeException(e);
        }
        System.out.printf("当前topic：%s，反序列化对象：%s\n",topic,String.valueOf(object));
        return object;
    }

    @Override
    public void close() {

    }
}
