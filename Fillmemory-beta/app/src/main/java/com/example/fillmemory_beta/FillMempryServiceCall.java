package com.example.fillmemory_beta;

import java.io.File;




import java.util.ArrayList;
import java.util.HashMap;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Environment;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

public class FillMempryServiceCall extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	SharedPreferences FillupRunning;
	SharedPreferences FillupThread;
	boolean isRunning = false;
	long currentThreadID;
	long lastThreadID;

	final static int myID = 5;	

	public static Context MainServiceContext;
	private Thread Internalthread;
	private Thread Internalthread_1;
	
	long totalFill = 0;
	long lastTotalFill = 0;

	private boolean D = true; // Debug flag
	
	// temp set isSDCardMounted final
	
	
	static long totalFill1000MCount = 0;
	static long totalFill100MCount = 0;
	static long totalFill10MCount = 0;
	static long totalFill1MCount = 0;

	static long lastTotalFill1000MCount = 0;
	static long lastTotalFill100MCount = 0;
	static long lastTotalFill10MCount = 0;
	static long lastTotalFill1MCount = 0;
	public static ArrayList<ByteArrayWrapper> mAllocations = new ArrayList<ByteArrayWrapper>();
	public static Object mAllocations_1 = new Object[1000];
	public static  ArrayList<ByteArrayWrapper> mAllocations_2 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_3 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_4 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_5 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_6 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_7 = new ArrayList<ByteArrayWrapper>();
	public static  ArrayList<ByteArrayWrapper> mAllocations_8 = new ArrayList<ByteArrayWrapper>();
	public  ArrayList<ByteArrayWrapper> mAllocations_9 = new ArrayList<ByteArrayWrapper>();
	
	public static  ArrayList<ByteArrayWrapper> mAllocations_10 = new ArrayList<ByteArrayWrapper>();
	
	Message message;
	Thread tMonitor;
	Memory_Info Memory;
	
	public FillMempryServiceCall() {
		// super("FillupStorage_ServiceCall");
		FileIO.WriteLogcat("FillMempryServiceCall SERVICE Constructor");
		
		
	}

	@Override
	public void onLowMemory() {
		// TODO Auto-generated method stub
		FileIO.WriteLogcat("FillMempryServiceCall onLowMemory()");
		super.onLowMemory();
	}

	@Override
	public void onTrimMemory(int level) {
		// TODO Auto-generated method stub

		FileIO.WriteLogcat("FillMempryServiceCall onTrimMemory()");
		super.onTrimMemory(level);
	}

	@Override
	public void onDestroy() {
		FileIO.WriteLogcat("FillMempryServiceCall onDestroy()");		
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		FileIO.WriteLogcat("FillupStorage_ServiceCall onStartCommand");
		super.onStartCommand(intent, flags, startId);

		try {
			Memory=new Memory_Info();
			String name = intent.getStringExtra("FillupSize"); 
			long MemorySize=Integer.valueOf(name);
			FillupRunning = FillMempryServiceCall.this.getSharedPreferences("isFillupRunning", 0);
			FillupThread = FillMempryServiceCall.this.getSharedPreferences("FillupThreadID", 0);
			lastThreadID = FillupThread.getLong("lastFillupThreadID", -1);			
			HashMap< String,Object> Map=(HashMap< String,Object>)intent.getSerializableExtra("Hash");
			isRunning = FillupRunning.getBoolean("isRunning", false);
			if (isRunning) {
				FileIO.WriteLogcat("isRunning is true ,do nothing return");
				FileIO.WriteLogcat("isRunningTest(should be true) = "+ isRunning);
				FileIO.WriteLogcat("return START_NOT_STICKY");
				return START_NOT_STICKY;

			} else {
				FileIO.WriteLogcat("isRunning is false ,do fillup and set isRunning is true");
				FillupRunning.edit().putBoolean("isRunning", true).commit();
			}

		
			MainServiceContext = this.getApplicationContext();


			// 提高Service的優先權不被系統殺掉
			Intent intent2 = new Intent(this, FillMempryServiceCall.class);
			intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
					| Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
			FileIO.WriteLogcat("new Internalthread and start");
			Internalthread = new FillupThread(getApplicationContext(),intent,Memory,Map,mAllocations_9, MemorySize);
			
			Internalthread.setPriority(Thread.MAX_PRIORITY);
			Internalthread.start();
			//Internalthread_1 = new FillupThread_2(getApplicationContext(),intent,Memory,Map,mAllocations_9);
			
			//Internalthread_1.setPriority(Thread.MAX_PRIORITY);
			//Internalthread_1.start();
			mAllocations_10=mAllocations_9;
			//mAllocations_9=null;
			//mAllocations_9=new ArrayList<ByteArrayWrapper>();
			
		} catch (Exception e) {
			Log.w(PathAndFlag.Tag, "FillupStorage_ServiceCall onStartCommand Error" + e.getMessage());
			FileIO.WriteLogcat("set Running flag is false");
			FillupRunning.edit().putBoolean("isRunning", false).commit();
		}
		
		
		
		FileIO.WriteLogcat("return START_NOT_STICKY");
		return START_NOT_STICKY;
	}	
	
	
}
