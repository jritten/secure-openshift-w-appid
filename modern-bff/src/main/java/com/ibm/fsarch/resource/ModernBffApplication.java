package com.ibm.fsarch.resource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
@RestController
public class ModernBffApplication {
	
	private static final Logger log = LoggerFactory.getLogger(ModernBffApplication.class);
	
	@Value("${app.uiRedirectUri}")
	private String uiRedirectUri;

	@Value("${app.resourceMSUri}")
	private String resourceMSUri;
	
	@Autowired
	private OAuth2RestTemplate oauth2RestTemplate;

	public static void main(String[] args) {
		SpringApplication.run(ModernBffApplication.class, args);
	}

	@RequestMapping("/")
	public ResponseEntity<?> uiRedirect() throws URISyntaxException {
		log.info("uiRedirect:Redirecting to UI Uri={}", uiRedirectUri);
		BodyBuilder bodyBuilder = ResponseEntity.status(HttpStatus.FOUND).location(new URI(uiRedirectUri));
		return bodyBuilder.build();
	}
	
	@RequestMapping("/authenticated")
	public Map<String,Boolean> isAuthenticated() {		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		HashMap<String,Boolean> resultMap = new HashMap<>(1);
		
		boolean authenticated = ((auth != null) && !(auth instanceof AnonymousAuthenticationToken) && auth.isAuthenticated());
		
		log.info("isAuthenticated: authenticated={}", authenticated);
		resultMap.put("authenticated",authenticated);
		return resultMap;
	}

	@RequestMapping("/details")
	public Message details() {
		log.info("details: resourceMSUri={}", resourceMSUri);
		ResponseEntity<Message> entity = oauth2RestTemplate.exchange(resourceMSUri+"/v1/message", HttpMethod.GET, null, Message.class);
		return entity.getBody();
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Message {
		private String id;
		private String content;
		private String hostName;
	}
}
