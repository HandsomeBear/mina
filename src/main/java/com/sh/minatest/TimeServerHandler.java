package com.sh.minatest;

import java.util.Date;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimeServerHandler extends IoHandlerAdapter{
	
	private static final Logger logger = LoggerFactory.getLogger(TimeServerHandler.class);

	//捕捉到异常时的处理
	@Override
	public void exceptionCaught(IoSession session,Throwable cause) throws Exception{
		logger.error("Excute exceptionCaught...");
		cause.printStackTrace();
	}
	
	//收到消息后的处理
	@Override
	public void messageReceived(IoSession session,Object message) throws Exception{
		logger.info("Excute messageReceived...");
		
		String str = message.toString();
		if(str.trim().equalsIgnoreCase("quit")){
			logger.info("quit");
			session.close(true);
			return;
		}
		Date date = new Date();
		session.write(date.toString());
		
		logger.info("Message Written...");
	}
	
	//会话处于空闲时的处理
	@Override
	public void sessionIdle(IoSession session,IdleStatus status) throws Exception{
		logger.info("Idle "+session.getIdleCount(status));
	}
}
