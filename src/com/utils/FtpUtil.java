package com.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.junit.Test;

public class FtpUtil {
	//上传
	/**
	 * 
	
	@Test
	public void testUpLoadFromDisk(){
		try {
			FileInputStream in=new FileInputStream(new File("G:/ppp/123456.txt"));
			boolean flag = uploadFile("10.115.15.162", 21, "zhu", "123456", "G:/zzz", "text.txt", in);
			System.out.println(flag);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	 */
	//在ftp生成一个文件
	/**
	 * 
	
	
	@Test  
	public void testUpLoadFromString(){  
	    try {  
	        InputStream input = new ByteArrayInputStream("123456".getBytes("utf-8"));  
	        boolean flag = uploadFile("10.115.15.162", 21, "zhu", "123456", "G:/zzz/test", "123456.txt", input);  
	        System.out.println(flag);  
	    } catch (UnsupportedEncodingException e) {  
	        e.printStackTrace();  
	    }  
	} 
	
	
	//从ftp下载文件
	@Test  
	public void testDownLoadFromString(){  
	    //InputStream input = new ByteArrayInputStream("123456".getBytes("utf-8"));  
		//String url, int port,String username, String password, String remotePath,String fileName,String localPath
		
		boolean flag = downFile("10.115.15.162", 21, "zhu", "123456", "G:/ftp", "4.jpg", "G:/lpp");  
		System.out.println(flag);  
	} 
	 */

	/**
	 * Description: 向FTP服务器上传文件
	 * @Version1.0 Jul 27, 2008 4:31:09 PM by 崔红保（cuihongbao@d-heaven.com）创建
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param path FTP服务器保存目录
	 * @param filename 上传到FTP服务器上的文件名
	 * @param input 输入流
	 * @return 成功返回true，否则返回false
	 */
	public static boolean uploadFile(FtpBean ftpBean) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(ftpBean.getFtpUrl(), ftpBean.getPort());//连接FTP服务器
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(ftpBean.getUserName(), ftpBean.getPassWord());//登录
			ftp.setFileType(FTP.BINARY_FILE_TYPE);  //设置文件类型为binary
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(ftpBean.getFtpPath());
			//設置為被動模式
			//ftp.enterLocalPassiveMode();
			
			ftp.storeFile(ftpBean.getFileName(), ftpBean.getInput());			
			
			ftpBean.getInput().close();
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

	/**
	 * Description: 从FTP服务器下载文件
	 * @Version1.0 Jul 27, 2008 5:32:36 PM by 崔红保（cuihongbao@d-heaven.com）创建
	 * @param url FTP服务器hostname
	 * @param port FTP服务器端口
	 * @param username FTP登录账号
	 * @param password FTP登录密码
	 * @param remotePath FTP服务器上的相对路径
	 * @param fileName 要下载的文件名
	 * @param localPath 下载后保存到本地的路径
	 * @return
	 */
	public static boolean downFile(FtpBean ftpBean) {
		boolean success = false;
		FTPClient ftp = new FTPClient();
		try {
			int reply;
			ftp.connect(ftpBean.getFtpPath(), ftpBean.getPort());
			//如果采用默认端口，可以使用ftp.connect(url)的方式直接连接FTP服务器
			ftp.login(ftpBean.getUserName(), ftpBean.getPassWord());//登录
			reply = ftp.getReplyCode();
			if (!FTPReply.isPositiveCompletion(reply)) {
				ftp.disconnect();
				return success;
			}
			ftp.changeWorkingDirectory(ftpBean.getFtpPath());//转移到FTP服务器目录
			FTPFile[] fs = ftp.listFiles();
			for(FTPFile ff:fs){
				if(ff.getName().equals(ftpBean.getFileName())){
					File localFile = new File(ftpBean.getLocalPath()+"/"+ff.getName());
					
					OutputStream is = new FileOutputStream(localFile); 
					ftp.retrieveFile(ff.getName(), is);
					is.close();
				}
			}		
			ftp.logout();
			success = true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (ftp.isConnected()) {
				try {
					ftp.disconnect();
				} catch (IOException ioe) {
				}
			}
		}
		return success;
	}

}
