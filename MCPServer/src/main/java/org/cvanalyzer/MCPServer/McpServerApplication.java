package org.cvanalyzer.MCPServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = {"org.cvanalyzer.MCPServer.repository",
		"org.cvanalyzer.MCPServer.tools", "org.cvanalyzer.MCPServer.configuration",
		"org.cvanalyzer.MCPServer.service"})
public class McpServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(McpServerApplication.class, args);
	}

}
