package com.llt.hope.configuration;

import java.util.List;

import jakarta.annotation.Nonnull;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Slf4j
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketMessageBrokerConfigurer {
    
    private final CustomJwtDecoder jwtDecoder;
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // For SockJS with specific origins (production và local development)
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns(
                "http://localhost:*", // Cho phép mọi port localhost
                "http://127.0.0.1:*", // Cho phép 127.0.0.1 với mọi port
                "file://*", // Cho phép file:// protocol cho HTML test files
                "http://localhost:3000",
                "https://ourhope.io.vn",
                "https://fe-hope-vn-version.vercel.app"
            )
            .withSockJS();
            
        // For native WebSocket connection (cho phép tất cả origins để test)
        registry.addEndpoint("/ws").setAllowedOriginPatterns("*");
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue");
        registry.setApplicationDestinationPrefixes("/app");
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(@Nonnull Message<?> message, @Nonnull MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

                // set auth for websocket session
                if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
                    String token = accessor.getLogin();
                    if (token != null) {
                        String userEmail = jwtDecoder.decode(token).getSubject();
                        log.info(userEmail);
                        Authentication authentication =
                                new PreAuthenticatedAuthenticationToken(userEmail, null, List.of());
                        accessor.setUser(authentication);
                    }
                }

                return message;
            }
        });
    }
}
