package com.xyt.articlespringboot3.config;


import com.xyt.articlespringboot3.filter.JWTAuthenticationTokenFilter;
import com.xyt.articlespringboot3.handler.*;
import com.xyt.articlespringboot3.service.impl.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final UserDetailsService userDetailsService;

    @Autowired
    private JWTAuthenticationTokenFilter jwtAuthenticationTokenFilter;

    @Autowired
    private AuthAccessDeniedHandler authAccessDeniedHandler;

    @Autowired
    private AuthEntryPointHandler authEntryPointHandler;

    @Autowired
    private AuthLoginSuccessHandler authLoginSuccessHandler;

    @Autowired
    private AuthLoginFailureHandler authLoginFailureHandler;

    @Autowired
    private MyUserDetailsService myUserDetailsService;
    


    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                .requestMatchers(
                        "/auth/login",
                        "/auth/register",
                        "/api/doc.html",
                        "/api/doc.html/**",
                        "/api/v3/api-docs",
                        "/api/v3/api-docs/**",
                        "/webjars/**",
                        "/authenticate",
                        "/swagger-ui.html/**",
                        "/swagger-resources",
                        "/swagger-resources/**").permitAll() //登录放行
                .anyRequest().authenticated()
        );


        //禁用登出页面
        httpSecurity.logout(AbstractHttpConfigurer::disable);
        //禁用session
        httpSecurity.sessionManagement(AbstractHttpConfigurer::disable);
        //禁用httpBasic
        httpSecurity.httpBasic(AbstractHttpConfigurer::disable);
        //禁用csrf保护
        httpSecurity.csrf(AbstractHttpConfigurer::disable);
        httpSecurity.cors();

        //禁用登录页面
        httpSecurity.formLogin((form) ->{
            form.usernameParameter("userAccount");
            form.passwordParameter("userPassword");
            form.loginProcessingUrl("/user/login");
            form.successHandler(authLoginSuccessHandler);
            form.failureHandler(authLoginFailureHandler);
        });
/*        httpSecurity.rememberMe(remember -> remember
                .rememberMeParameter("rememberMe")
                .userDetailsService(myUserDetailsService)
                .key("mykey")
                .rememberMeCookieName("drone-remember-me")
                .tokenRepository(myPersistentTokenRepository));*/
        //添加自定义token验证过滤器
        httpSecurity.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        //自定义处理器
        httpSecurity.exceptionHandling(exceptionHandling -> exceptionHandling
                .accessDeniedHandler(authAccessDeniedHandler) //处理用户权限不足
                .authenticationEntryPoint(authEntryPointHandler) //处理用户未登录（未携带token）
        );

        return httpSecurity.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(PasswordEncoder
                                                               passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(myUserDetailsService);
        // 关联使用的密码编码器
        provider.setPasswordEncoder(passwordEncoder);
        // 将provider放置进 AuthenticationManager 中,包含进去
        ProviderManager providerManager = new ProviderManager(provider);
        return providerManager;
    }

    /**
     * 加载用户信息
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }





    /**
     * 静态资源放行
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring().requestMatchers(
                "/doc.html",
                "/doc.html/**",
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/webjars/**",
                "/authenticate",
                "/swagger-ui.html/**",
                "/swagger-resources",
                "/swagger-resources/**"
        );
    }


}
