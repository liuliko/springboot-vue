package net.hnst.template.config.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public abstract class AdvanceWebSocketHandler extends TextWebSocketHandler {

    protected Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();


    private void addToSessionMap(String httpSessionId, WebSocketSession session) {
        sessionMap.put(httpSessionId, session);
    }

    private WebSocketSession getSessionFromMap(String httpSessionId) {
        return sessionMap.get(httpSessionId);
    }

    private void removeFromSessionMap(String httpSessionId) {
        sessionMap.remove(httpSessionId);
    }


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        String httpSessionId = (String) session.getAttributes().get("session_id");
        addToSessionMap(httpSessionId, session);

//        log.debug("open["+socketPath()+"]:" + httpSessionId);

        super.afterConnectionEstablished(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        String httpSessionId = (String) session.getAttributes().get("session_id");
        removeFromSessionMap(httpSessionId);

//        log.debug("close["+socketPath()+"]:" + httpSessionId);

        super.afterConnectionClosed(session, status);
    }


    /**
     * 给某个用户发送消息
     *
     * @param message
     */
    public void sendMessageToUser(javax.servlet.http.HttpSession session, String message) {
        String httpSessionId = session.getId();
        WebSocketSession webSocketSession = getSessionFromMap(httpSessionId);
        if (webSocketSession != null) {
            try {
                webSocketSession.sendMessage(new TextMessage(message.getBytes("utf-8")));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 给所有在线用户发消息
     *
     * @param message
     */
    public void sendToAll(TextMessage message) {
        Collection<WebSocketSession> sessions = sessionMap.values();
        for (WebSocketSession session : sessions) {
            if (sessions != null) {
                //如果有多个线程同时向同一个session推送消息,会抛出异常
                synchronized (session) {
                    try {
                        session.sendMessage(message);
                    } catch (Exception e) {
                        log.error("socket 消息推送异常!");
                        e.printStackTrace();
                    }
                }
            }
        }
    }


}
