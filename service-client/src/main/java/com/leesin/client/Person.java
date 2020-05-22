package com.leesin.client;

import java.io.Serializable;

/**
 * @description:
 * @author: Leesin Dong
 * @date: Created in 2020/5/22 0022 8:23
 * @modified By:
 */
public class Person implements Serializable {
    private static final long serialVersionUID = 6938786370758813816L;
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
