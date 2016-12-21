package com.wch.boot.module;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@Configuration
@EnableAutoConfiguration
@ComponentScan ("com.wch.boot")
@ImportResource ({ "classpath:dubbo/dubboContext.xml", "classpath:webservice/ws-client.xml", "classpath:hessian/hessian-client.xml", "classpath:spring/spring-service.xml" })
@MapperScan ("com.wch.boot.mapper")
public class BootModuleApplication
{
	
	public static void main(String[] args)
	{
		SpringApplication.run(BootModuleApplication.class, args);
	}
}
