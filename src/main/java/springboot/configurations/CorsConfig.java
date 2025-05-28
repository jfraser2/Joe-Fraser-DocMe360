package springboot.configurations;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

/**
 * 
 * To Make Swagger Testing work comment out the @Configuration
 * Annotation. If you don't Cors will block swagger
 *
 */

@Configuration
public class CorsConfig extends WebMvcConfigurationSupport
{
	
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
    	System.out.println("did the Resource Registry");
    	
        registry.addResourceHandler("swagger-ui.html")
            .addResourceLocations("classpath:/META-INF/resources/");

        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
        
    } 
	
    @Override
    public void addCorsMappings(CorsRegistry registry) {

    	System.out.println("Set Up Cors for Angular");
    	// Angular Testing from AngularIDE
        registry.addMapping("/**")
            .allowedOrigins("http://localhost:4200")
            .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Accept", "Content-Type", "Authorization")
            .allowedMethods("POST", "OPTIONS", "GET", "DELETE", "PUT");

    	System.out.println("Set Up Cors for Swagger");
    	
    	// Mapping for Swagger Testing
        registry.addMapping("swagger-ui.html")
            .allowedOrigins("*")
            .allowedHeaders("Access-Control-Allow-Origin", "Origin", "Access-Control-Allow-Headers", "Accept", "Content-Type", "Vary")
            .allowedMethods("GET", "POST");
        
        // Add more mappings...
    }
    
}

