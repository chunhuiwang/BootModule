/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-12-25	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import org.fusesource.mqtt.client.MQTT;

/**
 * 
 * 类简要描述
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author wangchunhui
 * 
 */

public interface MqttCallbackListener extends Runnable
{
	/**
	 * 初始设置Mqtt对象实例
	 * 
	 * @param mqtt
	 */
	public void initMqtt(MQTT mqtt);
	
}
