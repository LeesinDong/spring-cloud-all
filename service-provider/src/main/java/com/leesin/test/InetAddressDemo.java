package com.leesin.test;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Random;
import java.util.stream.Stream;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/19 0019 19:46
 * @modified By:
 */
public class InetAddressDemo {
    public static void main(String[] args) throws UnknownHostException {
        Stream.of(InetAddress.getAllByName("www.baidu.com")).forEach(System.out::println);
        // www.baidu.com/61.135.169.121
        // www.baidu.com/61.135.169.125
        Random random = new Random();
        int size = 2;
        //随机
        random.nextInt(size+1); //[1,2]
    //    轮询 （环装） ring
    }
}
