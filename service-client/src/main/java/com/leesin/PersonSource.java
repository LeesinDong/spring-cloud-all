package com.leesin;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/22 0022 11:38
 * @modified By:
 */
public interface PersonSource {
    // String CANNEL_NAME = "gupao";
    @Output("person-source")
    MessageChannel output();
}
