package com.ibm.fsarch.resource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.stereotype.Component;



@EnableWebSecurity
@EnableOAuth2Sso // Enables OAuth2 Single-Sign-On that will leverage application.yml
				// security properties
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.csrf().disable()
		.authorizeRequests()
		.antMatchers("/","/login**","/error**", "/authenticated","/actuator/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.logout().deleteCookies().invalidateHttpSession(true).logoutSuccessUrl("/").permitAll();
	}

	@Bean
	public OAuth2RestTemplate oauth2RestTemplate(OAuth2ClientContext oauth2ClientContext,
	        OAuth2ProtectedResourceDetails details) {
	    return new OAuth2RestTemplate(details, oauth2ClientContext);
	}

	@Configuration
	@ConfigurationProperties(prefix = "app.security")
	public class AppWebSecurityConfiguration  {
		private  List<String> allowedOrigins = new ArrayList<>();

		public List<String> getAllowedOrigins() {
			return allowedOrigins;
		}

		public void setAllowedOrigins(List<String> allowedOrigins) {
			this.allowedOrigins = allowedOrigins;
		}
	}


	@Component
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public static class SimpleCORSFilter implements Filter {
		// https://stackoverflow.com/questions/43114750/header-in-the-response-must-not-be-the-wildcard-when-the-requests-credentia/43409061

		@Autowired
		AppWebSecurityConfiguration appWebSecurityConfiguration;

		@Override
		public void init(FilterConfig fc) throws ServletException {
		}

		@Override
		public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {

			List<String> allowedOrigins = appWebSecurityConfiguration.getAllowedOrigins();

			if ((req instanceof HttpServletRequest) && (resp instanceof HttpServletResponse)) {
				HttpServletResponse response = (HttpServletResponse) resp;
				HttpServletRequest request = (HttpServletRequest) req;

				// Access-Control-Allow-Origin
				String origin = request.getHeader("Origin");

	            response.setHeader("Access-Control-Allow-Origin", allowedOrigins.contains(origin) ? origin : "");
	            response.setHeader("Vary", "Origin");

	            // Access-Control-Max-Age
	            response.setHeader("Access-Control-Max-Age", "3600");

	            // Access-Control-Allow-Credentials
	            response.setHeader("Access-Control-Allow-Credentials", "true");

	            // Access-Control-Allow-Methods
	            response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");

	            // Access-Control-Allow-Headers
	            response.setHeader("Access-Control-Allow-Headers",
	                "Origin, X-Requested-With, Content-Type, Accept, X-CSRF-TOKEN");

			}

			if ((req instanceof HttpServletRequest) && (resp instanceof HttpServletResponse) && ("OPTIONS".equalsIgnoreCase(((HttpServletRequest) req).getMethod()))) {
				((HttpServletResponse) resp).setStatus(HttpServletResponse.SC_OK);
			} else {
				chain.doFilter(req, resp);
			}

		}

		@Override
		public void destroy() {
		}

	}
}
