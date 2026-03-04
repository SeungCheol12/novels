package com.example.novels.common;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.example.novels.member.filter.JWTCheckFilter;
import com.example.novels.member.handler.LoginFailureHandler;
import com.example.novels.member.handler.LoginSuccessHandler;

import lombok.extern.log4j.Log4j2;

@EnableWebSecurity // 모든 웹 요청에 대해 Security Filter Chain 적용
@Configuration // 스프링 설정 클래스
@Log4j2
@EnableMethodSecurity // @PreAuthorize 사용가능
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                // swagger 로 접근 경로 열기
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.GET, "api/novels/**").permitAll()
                .anyRequest().authenticated());
        // cors 설정
        http.cors(httpSecurityCorsConfig -> httpSecurityCorsConfig.configurationSource(corsconfigurationSource()));

        // API 서버 상태
        http.sessionManagement(sessionConfig -> sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.csrf(csrf -> csrf.disable());
        http.formLogin(login -> login.loginPage("/api/member/login") // 폼 로그인
                // .defaultSuccessUrl("/", true)) // 로그인 성공 시 가는 기본 주소
                .successHandler(loginSuccessHandler()) // 로그인 성공 시 가는 주소
                .failureHandler(new LoginFailureHandler()));

        // 필터 지정
        http.addFilterBefore(jwtCheckFilter(), UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling(e -> e
                .authenticationEntryPoint((req, res, ex) -> {
                    res.setStatus(401);
                })
                .accessDeniedHandler((req, res, ex) -> {
                    res.setStatus(403);
                }));
        return http.build();
    }

    @Bean
    JWTCheckFilter jwtCheckFilter() {
        return new JWTCheckFilter();
    }

    // cors 설정 bean
    @Bean
    CorsConfigurationSource corsconfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOriginPattern("*");
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "HEAD"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Cache-Control", "Content-Type"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    RememberMeServices rememberMeServices(UserDetailsService userDetailsService) { // bean 누락
        // 토큰 생성용 알고리즘
        RememberMeTokenAlgorithm eTokenAlgorithm = RememberMeTokenAlgorithm.SHA256;

        TokenBasedRememberMeServices services = new TokenBasedRememberMeServices("myKey", userDetailsService,
                eTokenAlgorithm);

        // 브라우저에서 넘어온 remember-me 쿠키 검증용 알고리즘
        services.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
        // 7일 유효기간
        services.setTokenValiditySeconds(60 * 60 * 24 * 7);
        return services;
    }

    @Bean
    LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        // 운영, 실무, 여러 암호화 알고리즘 사용
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();

        // 연습, 단일 알고리즘 사용
        // return new BCryptPasswordEncoder();
    }
}
