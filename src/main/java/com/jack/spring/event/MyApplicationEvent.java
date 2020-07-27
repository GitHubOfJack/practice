package com.jack.spring.event;

import org.springframework.context.ApplicationEvent;

/**
 * @author 马钊
 * @date 2020-07-24 13:59
 */
public class MyApplicationEvent extends ApplicationEvent {
    /**
     * Create a new {@code ApplicationEvent}.
     *
     * @param source the object on which the event initially occurred or with
     *               which the event is associated (never {@code null})
     */
    public MyApplicationEvent(Object source) {
        super(source);
    }
}
