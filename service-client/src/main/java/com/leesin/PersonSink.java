package com.leesin;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/22 0022 13:35
 * @modified By:
 */
public interface PersonSink {
    @Input("person-sink")
    SubscribableChannel channel();
}
