package br.com.kerubin.api.apigateway;

import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;

@EnableResourceServer
@SpringBootApplication
@EnableZuulProxy
@EnableEurekaClient
public class APIGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(APIGatewayApplication.class, args);
	}
	
	@Primary
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("10MB");
        factory.setMaxRequestSize("100MB");
        
        String location = System.getProperty("java.io.tmpdir");
        System.out.println("location:" + location);
        
        /*Path path = Paths.get(System.getProperty("java.io.tmpdir"));
        location = path.toString();
        System.out.println("location:" + location);*/
        
        factory.setLocation(location);
        
        return factory.createMultipartConfig();
    }
}
