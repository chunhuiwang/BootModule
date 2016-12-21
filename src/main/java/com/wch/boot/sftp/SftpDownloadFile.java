/*      						
 * Copyright 2010 Beijing Xinwei, Inc. All rights reserved.
 * 
 * History:
 * ------------------------------------------------------------------------------
 * Date    	|  Who  		|  What  
 * 2016-6-16	| wangchunhui 	| 	create the file                       
 */

package com.wch.boot.sftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.ChannelSftp.LsEntry;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

/**
 * 
 * SFTP下载工具类
 * 
 * <p>
 * 类详细描述
 * </p>
 * 
 * @author wangchunhui
 * 
 */
@Service
public class SftpDownloadFile
{
	private static final Logger logger = LoggerFactory.getLogger(SftpDownloadFile.class);
	
	@Value ("${sftp.host}")
	private String sftpHost;
	
	@Value ("${sftp.port}")
	private int sftpPort;
	
	@Value ("${sftp.username}")
	private String sftpUsername;
	
	@Value ("${sftp.password}")
	private String sftpPassword;
	
	@Value ("${sftp.remote.path}")
	private String remotePath;
	
	// 本地保存文件地址
	@Value ("${sftp.local.path}")
	private String localPath;
	
	/**
	 * 下载文件为字符串数据，方便解析使用
	 * 
	 * @param fileName
	 *            zip文件名称
	 * @return
	 */
	public List<String> downloadFile(String sftpPath, String fileName)
	{
		logger.debug("downloadFile fileName = " + fileName + " from (" + sftpHost + "," + sftpPort + "," + sftpUsername + "," + sftpPassword + ")");
		List<String> retList = new ArrayList<String>();
		JSch jsch = new JSch();
		Session session = null;
		ChannelSftp channelSftp = null;
		try
		{
			session = jsch.getSession(sftpUsername, sftpHost, sftpPort);
			// 设置登陆主机的密码
			session.setPassword(sftpPassword);// 设置密码
			// 设置第一次登陆的时候提示，可选值：(ask | yes | no)
			session.setConfig("StrictHostKeyChecking", "no");
			// 设置登陆超时时间
			session.connect(30000);
			
			// 使用SFTP
			channelSftp = (ChannelSftp) session.openChannel("sftp");
			channelSftp.connect(2000);
			channelSftp.cd(sftpPath);
			
			// 列出服务器指定的文件列表
			Vector<?> v = channelSftp.ls(fileName);
			logger.debug(fileName + " file size = " + v.size());
			for (int i = 0; i < v.size(); i++)
			{
				if (v.get(i) instanceof LsEntry)
				{
					LsEntry dEntry = (LsEntry) v.get(i);
					logger.debug("sftp filePath = " + channelSftp.pwd() + "/" + dEntry.getFilename());
					// 保存本地文件
					File localFile = new File(localPath + dEntry.getFilename());
					logger.debug("local filePath = " + localFile.getAbsolutePath());
					FileOutputStream aout = new FileOutputStream(localFile);
					channelSftp.get(dEntry.getFilename(), aout);
					aout.close();
					
					// ZipInputStream Zin = new
					// ZipInputStream(channelSftp.get(dEntry.getFilename()));
					ZipInputStream Zin = new ZipInputStream(new FileInputStream(localFile));
					Zin.getNextEntry();
					
					BufferedReader dr = new BufferedReader(new InputStreamReader(Zin));
					String content = dr.readLine();
					while (content != null)
					{
						// logger.debug("fileName = " + dEntry.getFilename() +
						// " content = " + content);
						retList.add(content);
						content = dr.readLine();
					}
					dr.close();
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			logger.error("downloadFile is error.", e);
		}
		finally
		{
			if (session != null && channelSftp != null)
			{
				session.disconnect();
				channelSftp.disconnect();
			}
		}
		// 如果没有内容返回空
		return retList.size() == 0 ? null : retList;
	}
}
