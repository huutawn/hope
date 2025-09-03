package com.llt.hope.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final String[] PUBLIC_ENDPOINTS = {
        "/api/users",
        "/api/auth/token",
        "/api/auth/introspect",
        "/api/users/signup",
        "/api/auth/logout",
        "/api/auth/refresh",
        "/api/cloudinary/upload",
        "/api/v3/api-docs/**",
        "/api/swagger-ui/**",
        "/api/swagger-ui.html",
            "/api/users/**",
        "/api/job/getAll",
            "/api/job/**",
            "/api/comments/**",
        "/api/post/getAll",
        "/api/job/filter",
            "/ws",
            "/ws/**",
        "/api/job/search",
            "/api/auth/**",
            "/api/auth/outbound/authentication",
        "/api/",
        "/api/postVolunteer/getAll",
            "/api/support/post/**",
        "/api/hooks/sepay-payment",
        "/api/users/send-otp",
        "/api/users/verify-otp",
        "/api/users/reset-password",
        "/api/email",
        "/api/index.html",
        "/api/api/public/**",
        "/api/payment/vn-pay-callback",
        "/api/product/getAll",
        "/api/product/searchProduct",
            "/api/jobCategory",
            "/api/company/**",
            "/api/search/**",
            "/api/comments",
            "/api/comments/**",
            "/api/profile/**"
    };

    @Autowired
    private CustomJwtDecoder customJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.authorizeHttpRequests(request -> request.requestMatchers(PUBLIC_ENDPOINTS)
                .permitAll()
                .anyRequest()
                .authenticated());

        httpSecurity.oauth2ResourceServer(oauth2 -> oauth2.jwt(jwtConfigurer -> jwtConfigurer
                        .decoder(customJwtDecoder)
                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint()));
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource()));
        // Vô hiệu hóa CORS

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        // Cho phép các origin cụ thể cho API HTTP (KHÔNG dùng *)
        // Hoặc chỉ cho phép * nếu bạn chắc chắn API đó không gửi/nhận credentials
        // Nhưng nếu bạn gửi JWT, bạn nên liệt kê rõ ràng
        config.setAllowedOrigins(List.of("http://localhost:3000","https://ourhope.io.vn","https://fe-hope-vn-version.vercel.app")); // Mở cho dev
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT","PATCH", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(Arrays.asList("*")); // Cho phép tất cả headers
        config.setAllowCredentials(true); // Rất quan trọng nếu bạn gửi cookie/auth header
        config.setMaxAge(3600L); // Thời gian cache preflight request
        source.registerCorsConfiguration("/api/**", config); // Áp dụng cho các API của bạn
        // source.registerCorsConfiguration("/**", config); // Cẩn thận khi dùng /**
        return source;
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
