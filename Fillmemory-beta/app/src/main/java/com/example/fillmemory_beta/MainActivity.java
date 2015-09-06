package com.example.fillmemory_beta;




import java.io.File;
import java.util.HashMap;






import android.support.v7.app.ActionBarActivity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {
  
	ActivityManager.MemoryInfo outInfo = new ActivityManager.MemoryInfo();
	long totalFill = 0;
	long lastTotalFill = 0;
    long AvailMemorySize=0;
	final String myMessage = "";
	final int MY_NOTIFICATION_ID = 1;
	NotificationManager notificationManager;
	Notification myNotification;
	Memory_Info Memory;
	Button Start_Button;
	EditText editMemorySize;
	TextView text_EnterFillsize,text_Fillmemory,text_TotalMemory,text_AvailMemory,text_MemoryInfoAvail,text_memoryInfoTotal,text_UseMemorySize;
	SharedPreferences isRunningFlag;
	HashMap<String,Object>  map=new HashMap<String,Object>() ;
	Message message;
	Thread tMonitor;
	private boolean MonitorFlag = true;

	public static Context MainActivityContext;
	public static Handler messageHandler = new Handler();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		 MainActivityContext=this.getApplicationContext();
		Memory=new Memory_Info();
		AvailMemorySize=(Memory.getAvailMemory()/(1024*1024));//Size MB
		Log.i(PathAndFlag.Tag,"系統可用記憶體:"+(AvailMemorySize)+"MB");
	
		editMemorySize= (EditText)findViewById(R.id.editMemorySize);
		text_EnterFillsize=(TextView)findViewById(R.id.text_EnterFillsize);
		text_Fillmemory=(TextView)findViewById(R.id.text_Fillmemory);
		Start_Button= (Button) findViewById(R.id.Start_Button);
		text_TotalMemory=(TextView)findViewById(R.id.text_TotalMemory);
		text_AvailMemory=(TextView)findViewById(R.id.text_AvailMemory);
		text_UseMemorySize=(TextView)findViewById(R.id.	text_UseMemorySize);
		long MemoryTotalSize=(Memory.getTotalMemory()/(1024*1024));
		
		text_TotalMemory.setText(String.valueOf(MemoryTotalSize)+"MB");
		text_AvailMemory.setText(String.valueOf(AvailMemorySize)+"MB");
		text_UseMemorySize.setText(0+"MB");
		messageHandler = new Handler()
		{
		    @Override
		    public void handleMessage(Message msg)
		    {
		        // TODO Auto-generated method stub
		        super.handleMessage(msg);
		        //Log.i(PathAndFlag.Tag,"進入handleMessage");
		        switch (msg.what)
		        {
		            case 1:
		            	text_AvailMemory.setText(String.valueOf((Memory.getAvailMemory()/(1024*1024)))+"MB");
		            
		                break;
		            case 2:
		            	Bundle GetDataMsg = msg.getData();
		            	long BefortMemory=GetDataMsg.getLong("Befort AvailMemory");
		            	long AfterMemory=GetDataMsg.getLong("After AvailMemory");
		            	
		            	text_UseMemorySize.setText(String.valueOf((BefortMemory-AfterMemory)+"MB"));
		            	
		            default:
		                break;
		        }
		    }
		};
		Start_Button.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Log.i(PathAndFlag.LogcatTAG,"FillupMemory Start fill up internal storage");
				Thread tStorage = new Thread(new Runnable() {
					@Override
					public void run() {
					
						try {
							String FillMemorySize=editMemorySize.getText().toString();
							UpdateUI(1);
							Intent i = new Intent(MainActivity.this,FillMempryServiceCall.class);
							i.putExtra("FillupSize", FillMemorySize);
						    i.putExtra("Hash", map);
							startService(i);
						} catch (Exception ee) {
							Log.e(PathAndFlag.LogcatTAG,"FillupMemory Start_Button : "+ ee.getMessage());
						}
					}
				});
				tStorage.start();
				
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	protected void onResume() {
		// TODO Auto-generated method stub
		
		super.onResume();
		isRunningFlag = this.getSharedPreferences(
				"isFillupRunning", 0);
		isRunningFlag.edit().putBoolean("isRunning", false).commit();		

		tMonitor = new Thread(new MemoryInfoMonitor());
		tMonitor.start();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		map.get("array");
		
		super.onDestroy();
		
	}
	
	private void UpdateUI(int event) {
		message = new Message();
		message.what = event;
	
		MainActivity.this.messageHandler.sendMessage(message);
	}
	class MemoryInfoMonitor implements Runnable {

		Message message;
		@Override
		public void run() {
			// TODO Auto-generated method stub
		
			try {

				while (MonitorFlag) {
	
					UpdateUI(1);
					Thread.sleep(1000);
					if (!MonitorFlag)
						break;

				}
			} catch (Exception ee) {
				Log.e(PathAndFlag.LogcatTAG,
						"FillupStorage UsageMonitor thread Error : "
								+ ee.getMessage());
			}
		}

	}
	
}


