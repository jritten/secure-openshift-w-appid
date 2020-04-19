package com.ibm.fsarch.resource;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

//@EnableWebSecurity(debug = true)
@EnableWebSecurity
public class OIDCResourceServerConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests()
		.antMatchers("/","/error**","/actuator/**").permitAll()
		.anyRequest().authenticated()
            .and()
        .oauth2ResourceServer()
            .jwt();
		
	    
	    // Enable stateless Service
	    http.sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.NEVER);
	}
}
