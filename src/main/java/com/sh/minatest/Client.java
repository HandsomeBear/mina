package com.sh.minatest;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

public class Client {

	private static final int CONNECT_TIMEOUT = 3000;
	private static final  boolean USE_CUSTOM_CODEC = true;
	
	public void connect() throws InterruptedException{
		NioSocketConnector connector = new NioSocketConnector();
		connector.setConnectTimeoutMillis(CONNECT_TIMEOUT);
		
		if(USE_CUSTOM_CODEC){
//			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolCodecFactory()));
		}else{
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ObjectSerializationCodecFactory()));
		}
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(null);
		
		IoSession session;
		for(;;){
			try{
				//连接，是异步的
				ConnectFuture future = connector.connect(new InetSocketAddress("192.168.2.154", 9123));
				//阻塞到成功获取连接为止
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			}catch(RuntimeIoException e){
				e.printStackTrace();
				TimeUnit.SECONDS.sleep(5);
			}
		}
		//等到完成关闭session
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
}
