package com.ymiaohuang.mycalculator;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileService {
	/**
	 * 保存文件
	 * @param filename 文件名称
	 * @param content 文件内容
	 * 
	 * */
	private Context context;
	public FileService(Context context){
		this.context = context;
	}
	
	public void save(String filename, String content) throws Exception {
		//私有操作模式：创建出来的文件只能被本应用访问，其他应用无法访问该文件。
		//写入的内容会覆盖原文件的内容。
		FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
		write(content, fos);
		
	}
	
	private void write(String content, FileOutputStream fos) throws IOException {
		fos.write(content.getBytes());
		fos.close();
	}
	
	
	/**
	 * 读取文件内容
	 * @param filename 文件名称
	 * @return 文件内容
	 * @throws Exception
	 */
	public String read(String filename)throws Exception{
		FileInputStream fis = context.openFileInput(filename);
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024]; 
		int len;
		while((len=fis.read(buf))!=-1){
			outStream.write(buf,0,len);
		};
		byte[] data = outStream.toByteArray();
		return new String(data);
		
	}

}
