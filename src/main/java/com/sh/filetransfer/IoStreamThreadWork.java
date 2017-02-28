package com.sh.filetransfer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class IoStreamThreadWork extends Thread{

	public static final int BUFFER_SIZE = 1024*2;
	
	private BufferedInputStream bis = null;
	private BufferedOutputStream bos = null;
	
	public IoStreamThreadWork(InputStream in,OutputStream out){
		bis = new BufferedInputStream(in);
		bos = new BufferedOutputStream(out);
	}
	
	public synchronized void run(){
		byte[] bufferByte = new byte[BUFFER_SIZE];
		int tempData = 0;
		try{
			while((tempData = bis.read(bufferByte)) != -1){
				bos.write(bufferByte,0,tempData);
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(bis != null){
					bis.close();
				}
				if(bos != null){
					bos.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}
	
	public BufferedInputStream getBis() {
		return bis;
	}
	public void setBis(BufferedInputStream bis) {
		this.bis = bis;
	}
	public BufferedOutputStream getBos() {
		return bos;
	}
	public void setBos(BufferedOutputStream bos) {
		this.bos = bos;
	}
	
	
}
