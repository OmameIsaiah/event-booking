package com.event.booking.security.jwt;

import com.event.booking.security.user.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
        securedEnabled = true,
        jsr250Enabled = true,
        prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    private UnauthorizedEntryPoint unauthorizedEntryPoint;

    private static final String[] AUTH_WHITELIST = {
            "/api-docs/**",
            "/v2/api-docs/**",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui.html/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/api/v1/users/onboarding/**",
            "/api/v1/auth/**",
            "/api/v1/users/entrance/**"
    };

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .antMatchers("/api/v1/users/management/find-all").hasAnyRole("CAN_VIEW_USERS", "CAN_DELETE_USERS")
                .antMatchers("/api/v1/users/management/filter").hasAnyRole("CAN_VIEW_USERS", "CAN_DELETE_USERS")
                .antMatchers("/api/v1/users/management/search").hasAnyRole("CAN_VIEW_USERS", "CAN_DELETE_USERS")
                .antMatchers("/api/v1/users/management/delete").hasRole("CAN_DELETE_USERS")
                .antMatchers(HttpMethod.POST, "/api/v1/events").hasAnyRole("CAN_CREATE_EVENT", "CAN_UPDATE_EVENT", "CAN_DELETE_EVENT")
                .antMatchers(HttpMethod.PUT, "/api/v1/events").hasAnyRole("CAN_CREATE_EVENT", "CAN_UPDATE_EVENT", "CAN_DELETE_EVENT")
                .antMatchers(HttpMethod.DELETE, "/api/v1/events/{eventId}").hasAnyRole("CAN_CREATE_EVENT", "CAN_UPDATE_EVENT", "CAN_DELETE_EVENT")
                .antMatchers(HttpMethod.GET, "/api/v1/events/all/reservations").hasAnyRole("CAN_CREATE_EVENT", "CAN_UPDATE_EVENT", "CAN_DELETE_EVENT")
                .antMatchers(HttpMethod.GET, "/api/v1/events//all/reservations/filter").hasAnyRole("CAN_CREATE_EVENT", "CAN_VIEW_ALL_RESERVATIONS")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(unauthorizedEntryPoint)
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}