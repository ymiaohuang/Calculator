package com.ymiaohuang.mycalculator;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;

public class FileService {
	/**
	 * �����ļ�
	 * @param filename �ļ�����
	 * @param content �ļ�����
	 * 
	 * */
	private Context context;
	public FileService(Context context){
		this.context = context;
	}
	
	public void save(String filename, String content) throws Exception {
		//˽�в���ģʽ�������������ļ�ֻ�ܱ���Ӧ�÷��ʣ�����Ӧ���޷����ʸ��ļ���
		//д������ݻḲ��ԭ�ļ������ݡ�
		FileOutputStream fos = context.openFileOutput(filename,Context.MODE_PRIVATE);
		write(content, fos);
		
	}
	
	private void write(String content, FileOutputStream fos) throws IOException {
		fos.write(content.getBytes());
		fos.close();
	}
	
	
	/**
	 * ��ȡ�ļ�����
	 * @param filename �ļ�����
	 * @return �ļ�����
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
