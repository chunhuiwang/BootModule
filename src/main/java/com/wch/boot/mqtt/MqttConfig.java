/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-10	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import java.io.Serializable;

/**
 * 
 * MQTT服务配置信息
 * 
 * <p>
 * MQTT服务配置信息
 * </p>
 * 
 * @author wangchunhui
 * 
 */

public class MqttConfig implements Serializable
{
	
	private static final long serialVersionUID = -7696472211859060075L;
	
	// MQTT Server username
	private String mqttUserName;
	
	// MQTT Server password
	private String mqttPassword;
	
	// MQTT Server host
	private String mqttHost;
	
	// MQTT Server port
	private int mqttPort;
	
	// MQTT ClientId prefix
	private String mqttPrefix;
	
	public MqttConfig()
	{
		super();
	}
	
	public String getMqttUserName()
	{
		return mqttUserName;
	}
	
	public void setMqttUserName(String mqttUserName)
	{
		this.mqttUserName = mqttUserName;
	}
	
	public String getMqttPassword()
	{
		return mqttPassword;
	}
	
	public void setMqttPassword(String mqttPassword)
	{
		this.mqttPassword = mqttPassword;
	}
	
	public String getMqttHost()
	{
		return mqttHost;
	}
	
	public void setMqttHost(String mqttHost)
	{
		this.mqttHost = mqttHost;
	}
	
	public int getMqttPort()
	{
		return mqttPort;
	}
	
	public void setMqttPort(int mqttPort)
	{
		this.mqttPort = mqttPort;
	}
	
	public String getMqttPrefix()
	{
		return mqttPrefix;
	}
	
	public void setMqttPrefix(String mqttPrefix)
	{
		this.mqttPrefix = mqttPrefix;
	}
	
	@Override
	public String toString()
	{
		return "MqttConfig [mqttUserName=" + mqttUserName + ", mqttPassword=" + mqttPassword + ", mqttHost=" + mqttHost + ", mqttPort=" + mqttPort + ", mqttPrefix=" + mqttPrefix + "]";
	}
	
}
