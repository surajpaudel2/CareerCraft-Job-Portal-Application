package com.suraj.careercraft.security.config;

import com.suraj.careercraft.security.auth.CustomAuthenticationEntryPoint;
import com.suraj.careercraft.security.auth.CustomAuthenticationFailureHandler;
import com.suraj.careercraft.security.auth.CustomAuthenticationSuccessHandler;
import com.suraj.careercraft.security.jwt.JwtRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler;
    private final CustomAuthenticationFailureHandler customAuthenticationFailureHandler;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CorsConfigurationSource corsConfigurationSource;
//    private final AuthorizationManager<RequestAuthorizationContext> employerRegisterAuthorizationManager;
    private final  CustomAuthorizationService customAuthorizationService;

    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter,
                          @Lazy CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler,
                          CustomAuthenticationFailureHandler customAuthenticationFailureHandler,
                          CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                          CorsConfigurationSource corsConfigurationSource, CustomAuthorizationService customAuthorizationService) {

        this.jwtRequestFilter = jwtRequestFilter;
        this.customAuthenticationSuccessHandler = customAuthenticationSuccessHandler;
        this.customAuthenticationFailureHandler = customAuthenticationFailureHandler;
        this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
        this.corsConfigurationSource = corsConfigurationSource;
//        this.employerRegisterAuthorizationManager = employerRegisterAuthorizationManager;
        this.customAuthorizationService = customAuthorizationService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        configureCsrf(httpSecurity);
        configureAuthorization(httpSecurity);
        configureEntryPoint(httpSecurity);
        configureRestLogin(httpSecurity);
//        configureOAuthLogin(httpSecurity);
        configureSessionManagement(httpSecurity);

        httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.addFilter(new CorsFilter(corsConfigurationSource));

        return httpSecurity.build();
    }


    private void configureCsrf(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
    }

    private void configureEntryPoint(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
                    httpSecurityExceptionHandlingConfigurer
                            .authenticationEntryPoint(customAuthenticationEntryPoint);
                });
    }

    private void configureRestLogin(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .formLogin(httpSecurityFormLoginConfigurer ->
                        httpSecurityFormLoginConfigurer.loginProcessingUrl("/auth/login")
                                .usernameParameter("usernameOrEmail")
                                .passwordParameter("password")
                                .successHandler(customAuthenticationSuccessHandler)
                                .failureHandler(customAuthenticationFailureHandler)
                );
    }

    private void configureAuthorization(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers("/", "/oauth2/**", "/home", "/auth/**", "/job/search").permitAll()
                        .requestMatchers("/employer/register").access(employerRegisterAuthorizationManager())
                        .requestMatchers("/employer/**", "/job/create").hasAuthority("ROLE_EMPLOYER")
                        .anyRequest().authenticated()
                );
    }

//    private void configureOAuthLogin(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .oauth2Login(oauth2Login -> {
//                    oauth2Login
//                            .successHandler(customAuthenticationSuccessHandler)
//                            .failureHandler(customAuthenticationFailureHandler);
//                });
//    }

    private void configureSessionManagement(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private AuthorizationManager<RequestAuthorizationContext> employerRegisterAuthorizationManager() {
        return (authentication, context) -> {
            boolean hasAccess = customAuthorizationService.canAccessEmployerRegister(authentication.get());
            return new AuthorizationDecision(hasAccess);
        };
    }
}
