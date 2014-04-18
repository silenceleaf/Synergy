/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.zjy.synergy.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.Date;
import java.util.Map;

@EnableAutoConfiguration
@Configuration
@ComponentScan("org.zjy.synergy")
//@EntityScan(basePackages="org.zjy.synergy.entity")
public class ApplicationEntrance extends WebMvcConfigurerAdapter {

	public static void main(String[] args) throws Exception {
		// Set user password to "password" for demo purposes only
		new SpringApplicationBuilder(ApplicationEntrance.class).properties(
				"security.user.password=password").run(args);
	}

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("images/**").addResourceLocations("WEB-INF/images/");
        registry.addResourceHandler("css/**").addResourceLocations("WEB-INF/css/");
        registry.addResourceHandler("js/**").addResourceLocations("WEB-INF/js/");
        registry.addResourceHandler("extjs/**").addResourceLocations("WEB-INF/extjs/");
        //registry.addResourceHandler("extjs5/**").addResourceLocations("WEB-INF/extjs5/");
        //registry.addResourceHandler("views/**").addResourceLocations("WEB-INF/views/");
    }

//	@Override
//	public void addViewControllers(ViewControllerRegistry registry) {
//		registry.addViewController("/login").setViewName("login");
//	}

//    @Bean
//    public InternalResourceViewResolver viewResolver() {
//        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
//        viewResolver.setPrefix("/WEB-INF/views/");
//        viewResolver.setSuffix(".jsp");
//        return viewResolver;
//    }
}
