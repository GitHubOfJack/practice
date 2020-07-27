package com.jack.spring;

import com.jack.spring.event.MyApplicationEvent;
import lombok.Data;
import lombok.ToString;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;

/**
 * @author 马钊
 * @date 2020-07-20 09:32
 */
@Data
@ToString
public class Car {
    private int speed;

    private String name;

    @EventListener
    public void handEvent(MyApplicationEvent myApplicationEvent) {
        System.out.println(myApplicationEvent.getSource());
    }
}
