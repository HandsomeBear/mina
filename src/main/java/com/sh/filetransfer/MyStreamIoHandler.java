package com.sh.filetransfer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.mina.core.session.IoSession;
import org.apache.mina.handler.stream.StreamIoHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MyStreamIoHandler extends StreamIoHandler{

	private static final Logger logger = LoggerFactory.getLogger(MyStreamIoHandler.class);

	@Override
	protected void processStreamIo(IoSession session, InputStream in, OutputStream out) {
		logger.info("Execute processStreamIo");
		
		//最小数量:3;最大数量:6;空闲时间:3s
		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(3,6,3,TimeUnit.SECONDS,
				//缓冲队列
				new ArrayBlockingQueue<Runnable>(3),
				//抛弃就得任务
				new ThreadPoolExecutor.DiscardOldestPolicy());
		FileOutputStream fos = null;
		File receiveFile = new File("D:\\ztest\\test.txt");
		try{
			fos = new FileOutputStream(receiveFile);
		}catch(FileNotFoundException e){
			e.printStackTrace();
		}
		threadPool.execute(new IoStreamThreadWork(in,fos));
	}
	
	
}
