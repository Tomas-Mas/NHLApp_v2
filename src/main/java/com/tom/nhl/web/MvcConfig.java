package com.tom.nhl.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
@ComponentScan("com.tom.nhl")
public class MvcConfig implements WebMvcConfigurer {

	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry
		.addResourceHandler("/src/**")
		.addResourceLocations("/resources/");
	}
}
