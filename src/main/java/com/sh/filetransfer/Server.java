package com.sh.filetransfer;

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {
	
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	private int port = 9123;
	
	public Server(){
		
	}
	
	public Server(int port){
		this.port = port;
	}
	
	public void init() throws IOException{
		IoAcceptor acceptor = new NioSocketAcceptor();
		
		//设置可以接受大文件
		ObjectSerializationCodecFactory factory = new ObjectSerializationCodecFactory();
		factory.setEncoderMaxObjectSize(Integer.MAX_VALUE);
		factory.setDecoderMaxObjectSize(Integer.MAX_VALUE);
		
		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		
		acceptor.setHandler(new MyStreamIoHandler());
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
		acceptor.setDefaultLocalAddress(new InetSocketAddress(port));
		acceptor.bind();
	}
	
	public static void main(String[] args) throws IOException{
		Server server = new Server();
		server.init();
	}
	
}
