package com.example.hotel.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//CORS
		http.cors();
		//SESSION -> STATELESS
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		//CSRF
		http.csrf().disable();
		//JWT FILTER

		//API AUTHENTICATION
		http.antMatcher("/api/**").authorizeRequests()
		.antMatchers("/api/**").permitAll()
		.anyRequest().authenticated();
	}
}
