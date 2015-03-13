package com.ymiaohuang.mycalculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * ������ ���������һ���򿪼��±��Ĺ��ܣ�Ϊ�˷�������ͬʱ��¼��Щ���ݡ�
 * 
 * @author ��Ӣ��
 * @version 1.0
 * 
 * */
public class MyCalculatorActivity extends Activity {

	private Button[] btnNum = new Button[11];// ���ְ�ť
	private Button[] btnCommand = new Button[4];// ���Ű�ť
	private TextView tvRecord, tvResult;// ������ʾ ,�����ʾ��
	private String command;// ��¼�����������������ʱ�����ж�ǰ����������Ȼ����м��㡣
	private Double result;// ������������ʾ��tvResult�ϡ�
	private Double number;// ��ȡtvResult�������ַ�������ת��Double���ͣ���������
	private Button btnClear, btnResult, btnOpen, btnSave, btnRead;//���ܰ�ť����������ںţ��򿪣����档
	private TextView tvFileName, tvFileContent;//��ʾ�õ��ļ����ƣ��ļ����ݡ�
	private EditText etFileName, etFileContent;//�༭�õ��ļ����ƣ��ļ����ݡ�
	private int openCount;//�򿪼��±��Ĵ�����
	private Boolean isNextCommand,isFirst,isNewNumber,isNextResult;//���������жϣ�����������

	public MyCalculatorActivity(){
		
		number = 0.0;
		command = "+";
		result = 0.0;
		openCount = 0;
		isNextCommand = true;
		isFirst=true;
		isNewNumber=true;
		isNextResult = false;
		//���������ֵ��ʼ����
	}
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		
		btnCommand[0] = (Button) findViewById(R.id.btnAdd);
		btnCommand[1] = (Button) findViewById(R.id.btnSub);
		btnCommand[2] = (Button) findViewById(R.id.btnMul);
		btnCommand[3] = (Button) findViewById(R.id.btnDiv);
		//���Ű�ť����
		
		btnNum[0] = (Button) findViewById(R.id.btn0);
		btnNum[1] = (Button) findViewById(R.id.btn1);
		btnNum[2] = (Button) findViewById(R.id.btn2);
		btnNum[3] = (Button) findViewById(R.id.btn3);
		btnNum[4] = (Button) findViewById(R.id.btn4);
		btnNum[5] = (Button) findViewById(R.id.btn5);
		btnNum[6] = (Button) findViewById(R.id.btn6);
		btnNum[7] = (Button) findViewById(R.id.btn7);
		btnNum[8] = (Button) findViewById(R.id.btn8);
		btnNum[9] = (Button) findViewById(R.id.btn9);
		btnNum[10] = (Button) findViewById(R.id.btnPoint);
		//���ְ�ť����

		tvRecord = (TextView) findViewById(R.id.tvRecord);
		tvResult = (TextView) findViewById(R.id.tvResult);

		btnResult = (Button) findViewById(R.id.btnResult);
		btnClear = (Button) findViewById(R.id.btnClear);
		btnOpen = (Button) findViewById(R.id.btnOpen);

		for (Button bl : btnNum) {
			bl.setOnClickListener(new MyNumberListener());
		}
		for (Button bl : btnCommand) {
			bl.setOnClickListener(new MyCommandListener());
		}
		//���������֣������������ü����¼���

		btnResult.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				operator(number);
				tvRecord.setText("");
				tvResult.setText(""+result);
				number = 0.0;
				command = "+";	
				isNextCommand = true;
				isNextResult = true;
				isNewNumber = false;

			}
		});

		btnClear.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				tvRecord.setText("");
				tvResult.setText("0");
				number = 0.0;
				command = "+";
				result = 0.0;
				isNextCommand = true;
				isFirst=true;
			//	isNewNumber=true;
			}
		});
		btnOpen.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if (openCount == 0) {
					setVisible();
					openCount++;
				} else {
					setGone();
					openCount--;
				}

			}
		});
		//���������ǹ��ܰ�ť���������á�
		
		etFileName = (EditText) findViewById(R.id.etFileName);
		etFileContent = (EditText) findViewById(R.id.etFileContent);
		tvFileName = (TextView) findViewById(R.id.tvFileName);
		tvFileContent = (TextView) findViewById(R.id.tvFileContent);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new ButtonClickListener());
		btnRead = (Button) findViewById(R.id.btnRead);
		btnRead.setOnClickListener(new ButtonClickListener());
		//���±���һЩ�ؼ������������޹ء�

	}

	private final class ButtonClickListener implements View.OnClickListener {
		public void onClick(View v) {
			
			EditText filenameText = (EditText) findViewById(R.id.etFileName);
			EditText contentText = (EditText) findViewById(R.id.etFileContent);
			String filename = filenameText.getText().toString();
			String content = contentText.getText().toString();
			FileService service = new FileService(getApplicationContext());
			
			switch (v.getId()) {
			case R.id.btnSave:
				try {
					service.save(filename, content);
					Toast.makeText(getApplicationContext(), "����ɹ�", 1).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "����ʧ��", 1).show();
				}
				break;
			case R.id.btnRead:
				try {
					String data = service.read(filename);
					contentText.setText(data);
					Toast.makeText(getApplicationContext(), "��ȡ�ɹ�", 1).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "��ȡʧ��", 1).show();
				}
				break;
			}
			
		}
	}
	//���±��ļ�����
	
	private class MyNumberListener implements OnClickListener {

		public void onClick(View v) {
			if(isNextResult){
				result = 0.0;
			}
			isNewNumber = true;
			Button b = (Button) v;
			String inputNumber = b.getText().toString();
			String tvCurrent = tvResult.getText().toString();
			if(isNextCommand){
				if(inputNumber.equals(".")){
					tvResult.setText("0.");
					isNextCommand = false;
					return;
				}else if(inputNumber.equals("0")){
					tvResult.setText("0");
				}else{
					tvResult.setText(inputNumber);
					isNextCommand = false;	
				}
			}else{
				if(tvCurrent.contains(".") && inputNumber.equals(".")){
					return;
				}
				tvResult.setText(tvCurrent + inputNumber);
				tvCurrent = tvResult.getText().toString();
				
			}
			number = Double.parseDouble(tvResult.getText().toString());
		}
	}
	//���ְ�ť�ļ�������Ϊ�˱���������ʹ���˴����ж���䡣

	private class MyCommandListener implements OnClickListener {

		public void onClick(View v) {
			
			if(isNewNumber){
				operator(number);
			}
			Button b = (Button) v;
			command = b.getText().toString();
			tvRecord.setText(command);
			isNextCommand = true;
			if(isFirst==false){
				tvResult.setText(""+result);	
			}
			isFirst = false;
			isNewNumber = false;
			isNextResult = false;
		}
	}
	//���Ű�ť�ļ�����

	public void operator(Double number) {
		if (command.equals("+")) {
			result = result + number;
		}
		if (command.equals("-")) {
			result = result - number;
		}
		if (command.equals("*")) {
			result = result * number;
		}
		if (command.equals("/")) {
			if(number!=0){
				result = result / number;	
			}else{
				tvResult.setText("0");
				result = 0.0;
				Toast.makeText(getApplicationContext(), "��������Ϊ��", 1).show();
			}
		}
	}
	//���㡣

	private void setGone() {
		etFileName.setVisibility(View.GONE);
		etFileContent.setVisibility(View.GONE);
		tvFileName.setVisibility(View.GONE);
		tvFileContent.setVisibility(View.GONE);
		btnSave.setVisibility(View.GONE);
		btnRead.setVisibility(View.GONE);
	}
	private void setVisible() {
		etFileName.setVisibility(View.VISIBLE);
		etFileContent.setVisibility(View.VISIBLE);
		tvFileName.setVisibility(View.VISIBLE);
		tvFileContent.setVisibility(View.VISIBLE);
		btnSave.setVisibility(View.VISIBLE);
		btnRead.setVisibility(View.VISIBLE);
	}
	//���±�����ʾ�����أ����������޹ء�
}