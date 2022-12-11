package com.ihbank.ihbank.security;


import com.ihbank.ihbank.services.user.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

    @Autowired
    CustomUserDetailsService customUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {return new BCryptPasswordEncoder(); }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }


    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic();

        httpSecurity.authorizeRequests()
                //ACCOUNT CONTROLLER
                .mvcMatchers(HttpMethod.POST, "/create-account-one-holder").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.POST, "/create-account-two-holders").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/all-accounts").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.GET, "/admin-check-balance").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PATCH, "/admin-change-balance").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.DELETE, "delete-account/{id}").hasRole("ADMIN")
                .mvcMatchers(HttpMethod.PUT, "/transfer").hasRole("ADMIN")

                .mvcMatchers(HttpMethod.GET, "/my-balance").hasRole("ACCOUNT HOLDER")
                .mvcMatchers(HttpMethod.PUT, "/new-transfer").hasRole("ACCOUNT HOLDER")

                .mvcMatchers(HttpMethod.PUT, "/third-party-transfer").hasRole("THIRD PARTY")


                //THIRD PARTY CONTROLLER
                .mvcMatchers(HttpMethod.POST, "/create-third-party").hasRole("ADMIN")

                //ACCOUNT HOLDER CONTROLLER
                .mvcMatchers(HttpMethod.POST, "/create-holder").hasRole("ADMIN")

                .anyRequest().permitAll();

        httpSecurity.csrf().disable();

        return httpSecurity.build();
    }
}
