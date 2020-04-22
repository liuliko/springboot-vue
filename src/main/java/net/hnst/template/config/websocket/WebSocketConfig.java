package net.hnst.template.config.websocket;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;



import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {


    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        Map<String, Object> websockets = applicationContext.getBeansWithAnnotation(WebSocket.class);


        Set<String> socketPathSet = new HashSet<>();
        for (Object object : websockets.values()) {

            WebSocketHandler webSocketHandler;

            if (!(object instanceof AdvanceWebSocketHandler)) {
                throw new RuntimeException(object.getClass().getName() + "异常,必须继承AdvanceWebSocketHandler!");
            } else {
                webSocketHandler = (AdvanceWebSocketHandler) object;
            }

            WebSocket webSocket = webSocketHandler.getClass().getAnnotation(WebSocket.class);

            String socketPath = webSocket.path();

            if (!socketPath.startsWith("/socket/")) {
                throw new RuntimeException(webSocketHandler.getClass().getName() + "异常,path必须以/socket/开头");
            }

            if (socketPathSet.contains(socketPath)) {
                throw new RuntimeException(webSocketHandler.getClass().getName() + "异常,path与其它websocket路径冲突");
            }
            socketPathSet.add(socketPath);

            registry.addHandler(webSocketHandler, socketPath)
                    .addInterceptors(new HandshakeInterceptor())//此处添加内置拦截器为了传递httpsession属性到玩websocket session会话--spring官方文档
                    .setAllowedOrigins("*") //设置允许请求的来源
                    .withSockJS();//启用 enable socketJS

            log.debug(webSocketHandler.getClass().getName() + "初始化成功!");

        }
    }

}
