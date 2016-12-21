/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2015-3-11	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import java.net.SocketAddress;
import java.net.URISyntaxException;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.transport.Transport;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * MQTT接收监听器
 * 
 * <p>
 * MQTT接收监听器
 * </p>
 * 
 * @author wangchunhui
 * 
 */
// 在使用监听时（mqtt-config.properties 中 mqtt.listener=true）时打开下面注释
// @Service ("mqttCallbackListener")
public class TestListenerSample implements MqttCallbackListener
{
	private static final Logger logger = LoggerFactory.getLogger(TestListenerSample.class);
	
	private MQTT mqtt;
	
	// 监听主题
	private String destination;
	
	public TestListenerSample()
	{
		super();
		
		// 配置接收监听的TopicName
		// 需要修改下行代码
		this.destination = "TopicName";
	}
	
	@Override
	public void initMqtt(MQTT mqtt)
	{
		this.mqtt = mqtt;
	}
	
	@Override
	public void run()
	{
		logger.info("Init MqttListener is start.");
		
		// FALSE 设置接收消息时间，需要接收断开连接时间离线数据
		// TRUE 为只接对新MQTT消息
		mqtt.setCleanSession(false);
		
		final CallbackConnection connection = mqtt.callbackConnection();
		connection.listener(new org.fusesource.mqtt.client.Listener()
		{
			public void onConnected()
			{
				logger.info("Connection mqtt listener is onConnected start.");
				try
				{
					Transport transport = connection.transport();
					SocketAddress localAddress = transport.getLocalAddress();
					logger.info("Mqtt onConnected Tcp localAddress = " + localAddress.toString());
					mqtt.setLocalAddress("tcp:/" + localAddress.toString());
				}
				catch (URISyntaxException e)
				{
					e.printStackTrace();
					logger.error("Mqtt onConnected Tcp localAddress is error.", e);
				}
				logger.info("Connection mqtt listener is onConnected end.");
			}
			
			public void onDisconnected()
			{
				logger.error("Connection mqtt listener is onDisconnected.");
			}
			
			public void onFailure(Throwable t)
			{
				t.printStackTrace();
				logger.error("Connection mqtt listener is fail.", t);
			}
			
			public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack)
			{
				// 收到的消息内容
				String body = msg.utf8().toString();
				// DataBuffer : 自定义数据
				System.out.println("MQTT receive " + destination + ", body = " + body);
				ack.run();
			}
		});
		connection.connect(new Callback<Void>()
		{
			@Override
			public void onSuccess(Void value)
			{
				Topic[] topics = { new Topic(destination, QoS.EXACTLY_ONCE) };
				connection.subscribe(topics, new Callback<byte[]>()
				{
					public void onSuccess(byte[] qoses)
					{
						logger.info("Connection mqtt subscribe is success.");
					}
					
					public void onFailure(Throwable t)
					{
						t.printStackTrace();
						logger.error("Connection mqtt subscribe is fail.", t);
					}
				});
			}
			
			@Override
			public void onFailure(Throwable t)
			{
				t.printStackTrace();
				logger.error("Connection mqtt server is error.", t);
			}
		});
		
		logger.info("Init MqttListener is end.");
		
		// Wait forever..
		synchronized (this)
		{
			try
			{
				this.wait();
			}
			catch (InterruptedException e)
			{
				e.printStackTrace();
				logger.info("MqttListener wait thread is error.", e);
			}
		}
		
	}
}
