package com.api.automation.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {"com.api.automation"})
@PropertySource(value = {"${env:classpath:configuration.yml}"})
public class SpringConfig {

}
