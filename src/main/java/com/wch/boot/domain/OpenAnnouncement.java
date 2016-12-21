/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016年12月16日	| wanchunhui 	| 	create the file                       
 */

package com.wch.boot.domain;

import java.io.Serializable;
import java.util.Date;

/**
 * 
 * 开奖公告信息
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author wangchunhui
 * 
 */

public class OpenAnnouncement implements Serializable , Comparable<OpenAnnouncement>
{
	
	private static final long serialVersionUID = 1194961509329478721L;
	
	// 主键ID
	private long id;
	
	// 期号
	private String periodNumber;
	
	// 号码
	private String betNumber;
	
	// 中奖级别1~n
	private int awardLevel;
	
	// 赔率
	private double awardOdds;
	
	// 单注最大兑奖额，兑奖时如单注中奖金额大于此金额则不兑奖
	private double maxAwardAmount;
	
	// 是否可以兑奖，0不可以兑奖1可以兑奖
	private int canCash;
	
	// securityCheck =
	// MD5(canCash_periodNumber_maxAwardAmount_betNumber_awardLevel_awardOdds_pwd)
	private String securityCheck;
	
	// 创建时间
	private Date createTime;
	
	public OpenAnnouncement()
	{
		super();
	}
	
	public long getId()
	{
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}
	
	public String getPeriodNumber()
	{
		return periodNumber;
	}
	
	public void setPeriodNumber(String periodNumber)
	{
		this.periodNumber = periodNumber;
	}
	
	public String getBetNumber()
	{
		return betNumber;
	}
	
	public void setBetNumber(String betNumber)
	{
		this.betNumber = betNumber;
	}
	
	public int getAwardLevel()
	{
		return awardLevel;
	}
	
	public void setAwardLevel(int awardLevel)
	{
		this.awardLevel = awardLevel;
	}
	
	public double getAwardOdds()
	{
		return awardOdds;
	}
	
	public void setAwardOdds(double awardOdds)
	{
		this.awardOdds = awardOdds;
	}
	
	public double getMaxAwardAmount()
	{
		return maxAwardAmount;
	}
	
	public void setMaxAwardAmount(double maxAwardAmount)
	{
		this.maxAwardAmount = maxAwardAmount;
	}
	
	public int getCanCash()
	{
		return canCash;
	}
	
	public void setCanCash(int canCash)
	{
		this.canCash = canCash;
	}
	
	public String getSecurityCheck()
	{
		return securityCheck;
	}
	
	public void setSecurityCheck(String securityCheck)
	{
		this.securityCheck = securityCheck;
	}
	
	public Date getCreateTime()
	{
		return createTime;
	}
	
	public void setCreateTime(Date createTime)
	{
		this.createTime = createTime;
	}
	
	@Override
	public String toString()
	{
		return "OpenAnnouncement [id=" + id + ", periodNumber=" + periodNumber + ", betNumber=" + betNumber + ", awardLevel=" + awardLevel + ", awardOdds=" + awardOdds + ", maxAwardAmount="
				+ maxAwardAmount + ", canCash=" + canCash + ", securityCheck=" + securityCheck + ", createTime=" + createTime + "]";
	}
	
	@Override
	public int compareTo(OpenAnnouncement o)
	{
		if (this.awardLevel < o.getAwardLevel())
		{
			return 1;
		}
		else if (this.awardLevel > o.getAwardLevel())
		{
			return -1;
		}
		return 0;
	}
	
}
