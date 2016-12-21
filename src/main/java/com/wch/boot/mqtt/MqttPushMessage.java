/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2014-10-9	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import java.net.SocketAddress;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.hawtdispatch.transport.Transport;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Future;
import org.fusesource.mqtt.client.FutureConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 
 * MQTT消息推送
 * 
 * <p>
 * MQTT消息推送
 * </p>
 * 
 * @author wangchunhui
 * 
 */
@Service
public class MqttPushMessage
{
	
	private static final Logger logger = LoggerFactory.getLogger(MqttPushMessage.class);
	
	private static LinkedList<Future<Void>> futureList = new LinkedList<Future<Void>>();
	
	// 连接超时时间为10秒
	private static int WAIT_TIMEOUT = 10000;
	
	// futrue list overflow size
	private static int FUTRUE_QUENCE_SIZE = 2000;
	
	// 获取MQTT连接
	private static FutureConnection connection;
	
	/**
	 * 发送MQTT消息
	 * 
	 * @param topicName
	 * @param req
	 * @param other
	 * @throws Exception
	 */
	public static synchronized void push(String topicName, byte[] req, Object other) throws Exception
	{
		logger.debug("Push start topicName = " + topicName);
		UTF8Buffer topic = new UTF8Buffer(topicName);
		logger.debug("Push message = " + new String(req));
		// 推送消息
		futureList.add(getConnection().publish(topic, new UTF8Buffer(req), QoS.EXACTLY_ONCE, false));
		logger.debug("Push future list size  = " + futureList.size());
		if (futureList.size() > FUTRUE_QUENCE_SIZE)
		{
			// 保证消息队列不益处，同步等待消费
			futureList.removeFirst().await();
		}
		logger.debug("Push end topicName = " + topicName);
	}
	
	/**
	 * 获取异步发送连接
	 * 
	 * @return
	 * @throws Exception
	 */
	private static FutureConnection getConnection() throws Exception
	{
		if (connection == null)
		{
			MQTT mqtt = MqttClientInitialize.getMqttClient();
			MultiListenerCallBackConnection xwCbc = new MultiListenerCallBackConnection(mqtt);
			// 创建MQTT Future连接
			connection = new FutureConnection(xwCbc);
			
			// 启动线程，处理LocalAddress
			new Thread(new LocalAddressCallBack(xwCbc, mqtt)).start();
			
			// 开启连接
			connection.connect().await(WAIT_TIMEOUT, TimeUnit.MILLISECONDS);
		}
		return connection;
	}
	
	/**
	 * 处理MQTT连接失败，重新连接LocalAddress问题
	 * 
	 * @author wangchunhui
	 * 
	 */
	static class LocalAddressCallBack implements Runnable
	{
		private CallbackConnection callbackConnection;
		
		private MQTT mqtt = null;
		
		public LocalAddressCallBack(CallbackConnection callbackConnection, MQTT mqtt)
		{
			super();
			this.callbackConnection = callbackConnection;
			this.mqtt = mqtt;
		}
		
		@Override
		public void run()
		{
			callbackConnection.listener(new org.fusesource.mqtt.client.Listener()
			{
				public void onConnected()
				{
					logger.info("Mqtt onConnected is start");
					try
					{
						Transport transport = callbackConnection.transport();
						SocketAddress localAddress = transport.getLocalAddress();
						logger.info("Mqtt onConnected Tcp localAddress = " + localAddress.toString());
						mqtt.setLocalAddress("tcp:/" + localAddress.toString());
					}
					catch (URISyntaxException e)
					{
						e.printStackTrace();
						logger.error("Mqtt onConnected Tcp localAddress is error.", e);
					}
					logger.info("Mqtt onConnected is end");
				}
				
				public void onDisconnected()
				{
					logger.error("Mqtt onDisconnected");
				}
				
				public void onFailure(Throwable value)
				{
					logger.error("Mqtt onFailure. " + value.getMessage(), value);
				}
				
				public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack)
				{
					
				}
			});
			
			// Wait forever..
			synchronized (LocalAddressCallBack.class)
			{
				while (true)
				{
					try
					{
						LocalAddressCallBack.class.wait();
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		}
		
	}
}
