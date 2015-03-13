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
 * 计算器 这里添加了一个打开记事本的功能，为了方便计算的同时记录这些数据。
 * 
 * @author 黄英苗
 * @version 1.0
 * 
 * */
public class MyCalculatorActivity extends Activity {

	private Button[] btnNum = new Button[11];// 数字按钮
	private Button[] btnCommand = new Button[4];// 符号按钮
	private TextView tvRecord, tvResult;// 辅助显示 ,结果显示。
	private String command;// 记录运算符，在输入数字时，先判断前面的运算符，然后进行计算。
	private Double result;// 运算结果，将显示到tvResult上。
	private Double number;// 获取tvResult上数字字符串，再转成Double类型，方便运算
	private Button btnClear, btnResult, btnOpen, btnSave, btnRead;//功能按钮，清除，等于号，打开，保存。
	private TextView tvFileName, tvFileContent;//显示用的文件名称，文件内容。
	private EditText etFileName, etFileContent;//编辑用的文件名称，文件内容。
	private int openCount;//打开记事本的次数。
	private Boolean isNextCommand,isFirst,isNewNumber,isNextResult;//各种条件判断，避免计算出错。

	public MyCalculatorActivity(){
		
		number = 0.0;
		command = "+";
		result = 0.0;
		openCount = 0;
		isNextCommand = true;
		isFirst=true;
		isNewNumber=true;
		isNextResult = false;
		//计算各项数值初始化。
	}
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);


		
		btnCommand[0] = (Button) findViewById(R.id.btnAdd);
		btnCommand[1] = (Button) findViewById(R.id.btnSub);
		btnCommand[2] = (Button) findViewById(R.id.btnMul);
		btnCommand[3] = (Button) findViewById(R.id.btnDiv);
		//符号按钮数组
		
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
		//数字按钮数组

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
		//给数字数字，符号数字设置监听事件。

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
		//上面三个是功能按钮，另外设置。
		
		etFileName = (EditText) findViewById(R.id.etFileName);
		etFileContent = (EditText) findViewById(R.id.etFileContent);
		tvFileName = (TextView) findViewById(R.id.tvFileName);
		tvFileContent = (TextView) findViewById(R.id.tvFileContent);
		btnSave = (Button) findViewById(R.id.btnSave);
		btnSave.setOnClickListener(new ButtonClickListener());
		btnRead = (Button) findViewById(R.id.btnRead);
		btnRead.setOnClickListener(new ButtonClickListener());
		//记事本的一些控件，跟计算器无关。

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
					Toast.makeText(getApplicationContext(), "储存成功", 1).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "储存失败", 1).show();
				}
				break;
			case R.id.btnRead:
				try {
					String data = service.read(filename);
					contentText.setText(data);
					Toast.makeText(getApplicationContext(), "读取成功", 1).show();
				} catch (Exception e) {
					e.printStackTrace();
					Toast.makeText(getApplicationContext(), "读取失败", 1).show();
				}
				break;
			}
			
		}
	}
	//记事本的监听器
	
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
	//数字按钮的监听器，为了避免计算出错，使用了大量判断语句。

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
	//符号按钮的监听器

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
				Toast.makeText(getApplicationContext(), "除数不能为零", 1).show();
			}
		}
	}
	//计算。

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
	//记事本的显示和隐藏，跟计算器无关。
}