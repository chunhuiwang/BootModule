/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-9	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import java.net.URISyntaxException;
import java.util.concurrent.atomic.AtomicInteger;

import org.fusesource.mqtt.client.MQTT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 
 * MQTT客户端初始化
 * 
 * <p>
 * MQTT客户端初始化
 * </p>
 * 
 * @author wangchunhui
 * 
 */
@Component
public class MqttClientInitialize implements ApplicationContextAware
{
	
	private static final Logger logger = LoggerFactory.getLogger(MqttClientInitialize.class);
	
	// Spring上下文
	private static ApplicationContext context;
	
	// MQTT listener bean name
	private static final String LISTENER_NAME = "mqttCallbackListener";
	
	// MQTT Client ID
	private static volatile AtomicInteger inCrement = new AtomicInteger(Integer.MAX_VALUE);
	
	// MQTT Config info
	private static MqttConfig mqttConfig = new MqttConfig();
	
	// MQTT Server username
	@Value ("${mqtt.username}")
	private String mqttUserName;
	
	// MQTT Server password
	@Value ("${mqtt.password}")
	private String mqttPassword;
	
	// MQTT Server host
	@Value ("${mqtt.host}")
	private String mqttHost;
	
	// MQTT Server port
	@Value ("${mqtt.prot}")
	private int mqttPort;
	
	// MQTT ClientId prefix
	@Value ("${mqtt.prefix}")
	private String mqttPrefix;
	
	@Override
	public void setApplicationContext(ApplicationContext ctx) throws BeansException
	{
		logger.info("Init MQTT Client start. mqttUserName = " + mqttUserName + ", mqttPassword = " + mqttPassword + ", mqttHost = " + mqttHost + ", mqttPort = " + mqttPort + ", mqttPrefix = "
				+ mqttPrefix);
		context = ctx;
		try
		{
			// MqttConfig set
			mqttConfig.setMqttHost(mqttHost);
			mqttConfig.setMqttPort(mqttPort);
			mqttConfig.setMqttUserName(mqttUserName);
			mqttConfig.setMqttPassword(mqttPassword);
			mqttConfig.setMqttPrefix(mqttPrefix);
			logger.info("Init MQTT Client end.");
			
			try
			{
				MqttCallbackListener mqttCallbackListener = (MqttCallbackListener) context.getBean(LISTENER_NAME);
				if (mqttCallbackListener != null)
				{
					logger.info("Init MQTT Client listener start.");
					mqttCallbackListener.initMqtt(getMqttClient());
					Thread listener = new Thread(mqttCallbackListener);
					listener.start();
					logger.info("Init MQTT Client listener end.");
				}
			}
			catch (Exception e)
			{
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("Init MQTT Client is error.", e);
		}
		logger.info("Init MQTT Client end.");
	}
	
	/**
	 * 根据BeanName获取对像实例
	 * 
	 * @param beanName
	 * @return
	 */
	public static Object getSpringBean(String beanName)
	{
		return context.getBean(beanName);
	}
	
	/**
	 * 获取MQTT实例
	 * 
	 * @return
	 */
	public static synchronized MQTT getMqttClient()
	{
		MqttConfig mqttConfig = getMqttConfig();
		MQTT mqtt = new MQTT();
		// MQTT link server set
		try
		{
			mqtt.setHost(mqttConfig.getMqttHost(), mqttConfig.getMqttPort());
		}
		catch (URISyntaxException e)
		{
			e.printStackTrace();
		}
		mqtt.setUserName(mqttConfig.getMqttUserName());
		mqtt.setPassword(mqttConfig.getMqttPassword());
		mqtt.setClientId(mqttConfig.getMqttPrefix() + inCrement.getAndIncrement() + System.currentTimeMillis());
		return mqtt;
	}
	
	/**
	 * MQTT配置信息
	 * 
	 * @return
	 */
	public static MqttConfig getMqttConfig()
	{
		return mqttConfig;
	}
}
