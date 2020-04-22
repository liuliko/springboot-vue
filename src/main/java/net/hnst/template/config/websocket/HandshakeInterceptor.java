package net.hnst.template.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.Nullable;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
public class HandshakeInterceptor extends HttpSessionHandshakeInterceptor {


    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.debug("websocket开始建立连接.websocket:" + request.getURI() + "; R : " + request.getRemoteAddress());
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession httpSession = servletRequest.getServletRequest().getSession(true);
            if (null != httpSession) {
                attributes.put("session_id", httpSession.getId());
            }
        }
        return super.beforeHandshake(request, response, wsHandler, attributes);
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, @Nullable Exception ex) {
        log.debug("websocket连接完成。websocket:" + request.getURI() + "; R : " + request.getRemoteAddress());
        super.afterHandshake(request, response, wsHandler, ex);
    }
}
