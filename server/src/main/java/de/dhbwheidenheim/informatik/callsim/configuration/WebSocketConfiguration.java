package de.dhbwheidenheim.informatik.callsim.configuration;

import de.dhbwheidenheim.informatik.callsim.controller.WebSocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration implements WebSocketConfigurer {

    // Enable/Export the server endpoint and websocket
    @Bean
    public WebSocket webSocket() {
        return new WebSocket();
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    // Implement websockethandler (no further configuration necessary)
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
                
    }
}