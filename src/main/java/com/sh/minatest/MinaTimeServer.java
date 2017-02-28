package com.sh.minatest;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinaTimeServer {

	private static final Logger logger = LoggerFactory.getLogger(MinaTimeServer.class);
	private static final int PORT = 9123;
	
	public static void main(String[] args) throws IOException{
		logger.info("main start...");
		
		//创建一个非阻塞的接收器
		IoAcceptor acceptor = new NioSocketAcceptor();
		//设置过滤器
		LoggingFilter lf = new LoggingFilter();
		//目测不能覆盖log4j.properties的默认配置(设置为NONE有效，但是ERROR不会起效)
		lf.setSessionOpenedLogLevel(LogLevel.ERROR);
		lf.setMessageReceivedLogLevel(LogLevel.ERROR);
		lf.setSessionClosedLogLevel(LogLevel.NONE);
		acceptor.getFilterChain().addLast("logger", lf);
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));
		//这是处理业务逻辑的Handler
		acceptor.setHandler(new TimeServerHandler());
		//会话属性设置
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE,10);
		//绑定监听端口，开始服务
		acceptor.bind(new InetSocketAddress(PORT));
		
		//true--等待所有会话都完全的停止后停止服务
//		acceptor.dispose(true);
	}
}
