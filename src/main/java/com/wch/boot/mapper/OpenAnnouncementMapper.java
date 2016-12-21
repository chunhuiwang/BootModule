/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016年12月16日	| wanchunhui 	| 	create the file                       
 */

package com.wch.boot.mapper;

import java.util.List;

import com.wch.boot.domain.OpenAnnouncement;

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

public interface OpenAnnouncementMapper
{
	/**
	 * 保存开奖公告信息
	 * 
	 * @param openAnnouncement
	 */
	public void save(OpenAnnouncement openAnnouncement);
	
	/**
	 * 查询开奖公告
	 * 
	 * @return
	 */
	public List<OpenAnnouncement> queryOpenAnnouncement();
	
	/**
	 * 转移开奖公告到历史表中
	 */
	public void transferAnnouncementToHistory();
	
	/**
	 * 清理开奖公告表
	 */
	public void cleanOpenAnnouncement();
	
}
