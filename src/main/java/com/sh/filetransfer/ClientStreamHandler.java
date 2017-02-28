package com.sh.filetransfer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClientStreamHandler extends StreamIoHandler{

	private static final Logger logger = LoggerFactory.getLogger(ClientStreamHandler.class);
	
	@Override
	protected void processStreamIo(IoSession session, InputStream in, OutputStream out) {
		logger.info("Exxcute processStreamIo");
		
		File sendFile = new File("G:\\test.txt");
		FileInputStream fis = null;
		try{
			fis = new FileInputStream(sendFile);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		new IoStreamThreadWork(fis,out).start();
		return;
	}

	
}
