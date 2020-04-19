package com.ibm.fsarch.resource;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@SpringBootApplication
@RestController
public class ResourceApplication {

	@Value("${app.hostName}")
	private String appHostName;

	public static void main(String[] args) {
		SpringApplication.run(ResourceApplication.class, args);
	}

	@RequestMapping("/v1/message")
	@CrossOrigin(origins = "*", maxAge = 3600, allowedHeaders = { "x-auth-token", "x-requested-with", "x-xsrf-token",
			"authorization" })
	public Message home() {
		return new Message("Hello World", appHostName);
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	static class Message {
		private String id = UUID.randomUUID().toString();
		private String content;
		private String hostName;

		public Message(String content, String hostname) {
			this.content = content;
			this.hostName = hostname;
		}
	}
}
