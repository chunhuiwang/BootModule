/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016-3-31	| liguoqiang 	| 	create the file                       
 */

package com.wch.boot.mqtt;

/**
 * 
 * 发送Mqtt消息示例
 * 
 * <p>
 * 发送Mqtt消息示例
 * </p>
 * 
 * @author wangchunhui
 * 
 */

public class TestPublishSample
{
	
	/**
	 * 发送Mqtt消息示例
	 */
	public void sendMessage()
	{
		// 发送消息主题
		String topicName = "TopicName";
		// 发送消息内容
		String content = "Message Info.";
		
		try
		{
			// 发送消息调用
			MqttPushMessage.push(topicName, content.getBytes(), null);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
}
