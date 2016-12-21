/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016年11月21日	| wanchunhui 	| 	create the file                       
 */

package com.wch.boot.task;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 
 * 号码开奖兑奖Task
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author wangchunhui
 * 
 */
@Component
@Configurable
@EnableScheduling
public class BootTestTask
{
	private final static Logger logger = LoggerFactory.getLogger(BootTestTask.class);
	
	// 开奖定时任务
	@Scheduled (cron = "0 5,10,15,20,25,30,35,40,45,50,55 * * * * ")
	public void openAwardTask()
	{
		logger.info("Scheduling Tasks openAwardTask By Cron: The time is now " + new Date());
	}
}
