package com.sh.filetransfer;

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.RuntimeIoException;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
	private static final Logger logger = LoggerFactory.getLogger(Client.class);
	
	public Client(){
		
	}
	
	public void connect() throws InterruptedException{
		NioSocketConnector connector = new NioSocketConnector();
		
		ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
		factory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
		factory.setEncoderMaxObjectSize(Integer.MAX_VALUE);
		
		connector.setConnectTimeoutMillis(3000);
		
		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.setHandler(new ClientStreamHandler());
		
		IoSession session;
		for(;;){
			try{
				ConnectFuture future = connector.connect(new InetSocketAddress("192.168.176.1", 9123));
				future.awaitUninterruptibly();
				session = future.getSession();
				break;
			}catch(RuntimeIoException e){
				logger.info("connect fail,retry");
				e.printStackTrace();
				TimeUnit.SECONDS.sleep(5);
			}
		}
		
		session.getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}
	
	public static void main(String[] args) throws Exception{
		Client client = new Client();
		client.connect();
	}
}
