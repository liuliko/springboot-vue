package net.hnst.template.config.websocket;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * websocket标签
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed

@Inherited
@Component
public @interface WebSocket {

    /**
     * socket连接路径
     * @return
     */
    String path();
}
