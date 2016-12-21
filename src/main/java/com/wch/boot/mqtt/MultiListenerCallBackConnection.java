/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016-3-29	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.mqtt;

import java.util.ArrayList;
import java.util.List;

import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.Listener;
import org.fusesource.mqtt.client.MQTT;

/**
 * 
 * 接管MQTT CallBackConnection的Listener
 * 
 * <p>
 * 处理多个Listeners
 * </p>
 * 
 * @author wangchunhui
 * 
 */

public class MultiListenerCallBackConnection extends CallbackConnection
{
	// 是否支持多个Listener
	private boolean isSupportListers = false;
	
	// Listener列表
	private volatile List<Listener> listeners = new ArrayList<Listener>();
	
	public MultiListenerCallBackConnection(MQTT mqtt)
	{
		super(mqtt);
		// 设置默认Listener
		setSupportListers(true);
	}
	
	@Override
	public CallbackConnection listener(Listener listener)
	{
		if (isSupportListers())
		{
			listeners.add(listener);
			return this;
		}
		else
		{
			return super.listener(listener);
		}
		
	}
	
	public boolean isSupportListers()
	{
		return isSupportListers;
	}
	
	public void setSupportListers(boolean isSupportListers)
	{
		this.isSupportListers = isSupportListers;
		if (isSupportListers())
		{
			setDefaultListener(null);
		}
	}
	
	public void setDefaultListener(Listener listener)
	{
		if (listener == null)
		{
			super.listener(new Listener()
			{
				
				public void onConnected()
				{
					for (Listener listener : listeners)
					{
						listener.onConnected();
					}
				}
				
				public void onDisconnected()
				{
					for (Listener listener : listeners)
					{
						listener.onDisconnected();
					}
				}
				
				public void onPublish(UTF8Buffer topic, Buffer payload, Runnable onComplete)
				{
					for (Listener listener : listeners)
					{
						listener.onPublish(topic, payload, onComplete);
					}
				}
				
				public void onFailure(Throwable value)
				{
					for (Listener listener : listeners)
					{
						listener.onFailure(value);
					}
				}
			});
		}
		else
		{
			super.listener(listener);
		}
	}
}
