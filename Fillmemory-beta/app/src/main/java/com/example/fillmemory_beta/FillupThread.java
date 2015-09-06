package com.example.fillmemory_beta;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.Vector;










import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;



public class FillupThread  extends Thread{
	
	private Context _context;
	private Intent _intent;
	private HashMap<String,Object> _Map;
	private long _MemorySize;
	private final String INTERNAL_PATH_FOLDER = "/data/data/";
 
	static long totalFill = 0;
	static long lastTotalFill = 0;

	private boolean D = true; // Debug flag

	static long totalFill1000MCount = 0;
	static long totalFill100MCount = 0;
	static long totalFill10MCount = 0;
	static long totalFill1MCount = 0;

	static long lastTotalFill1000MCount = 0;
	static long lastTotalFill100MCount = 0;
	static long lastTotalFill10MCount = 0;
	static long lastTotalFill1MCount = 0;
    public  Object[] array = new Object[1000]; 
    public  Object[] array_char = new Object[1000];
	long currentThreadID;
	String currentThreadName;
	long lastThreadID;
	private String APIResult;
	Message message;
	Thread tMonitor;
	Memory_Info Memory;
	final int MY_NOTIFICATION_ID = 1;
	NotificationManager notificationManager;
	Notification myNotification;
	final String myMessage = "";
	Runtime rt = Runtime.getRuntime();

	SharedPreferences Shared_mAllocations;
	SharedPreferences Shared_mAllocations_1;
	SharedPreferences Shared_mAllocations_2;
	 private ArrayList<ByteArrayWrapper> mAllocations_8 = new ArrayList<ByteArrayWrapper>();
	public Activity MActivity;
	/*private ArrayList<ByteArrayWrapper> mAllocations = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_1 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_2 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_3 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_4 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_5 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_6 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_7 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_8 = new ArrayList<ByteArrayWrapper>();
	private ArrayList<ByteArrayWrapper> mAllocations_9 = new ArrayList<ByteArrayWrapper>();
	*/
	public ArrayList<ByteArrayWrapper> mAllocations_10 ;
	public Map<String,byte[]> map_memory=new HashMap<String,byte[]>();

	public FillupThread(Context context,Intent intent,Memory_Info Memory_Info,HashMap<String,Object> map,long MemorySize)
	{
		new FillupThread(context,intent,Memory_Info,map,null, MemorySize);
	
	}
	
	public FillupThread(Context context,Intent intent,Memory_Info Memory_Info,HashMap<String,Object> mapp, ArrayList<ByteArrayWrapper> m9,long MemorySize)
	{
		
		
		this._context = context;
		this._intent = intent;
		this._Map=mapp;
		this._MemorySize=MemorySize;
		this.mAllocations_10 =m9; 
		
	}
	
	
	
	public void run() {
		try {
			
			FileIO.WriteLogcat("Run()");
			String Fillup = "80";
			String timeout = "10";
			int FillupSize=80;
			int Fillup_timeout=10;
			Fillup = this._intent.getExtras().getString("Fillup");
			timeout = this._intent.getExtras().getString("Timeout");

			Memory=new Memory_Info();

			FileIO.WriteLogcat("Internal before  fullup function call");

			Memory_servicecall(FillupSize, Memory,
					Fillup_timeout);
			FileIO.WriteLogcat("Internal after  fullup function call");
		
		} catch (Exception e) {
			FileIO.WriteLogcat("Run() error" + e.getMessage());
			e.getMessage();
		}

	}
	public void Memory_servicecall(int percentage,Memory_Info Memory,
			 int timeout) {
		 Memory= new Memory_Info();
		BigDecimal bd = new BigDecimal(percentage);
		double local_percentage = bd.setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
		FileIO.WriteLogcat("command percenttage" + local_percentage);

		String path = INTERNAL_PATH_FOLDER;
		SharedPreferences APUsedMemory;
		SharedPreferences isRunningFlag;
		SharedPreferences FillupThread;

		isRunningFlag = this._context.getSharedPreferences("isFillupRunning", 0);

		boolean Flag = isRunningFlag.getBoolean("isRunning", false);
		Log.i(PathAndFlag.LogcatTAG, "Flag should be true=" + Flag);
		FileIO.WriteLogcat("set Running flag is false");
		isRunningFlag.edit().putBoolean("isRunning", false).commit();

		currentThreadID = Thread.currentThread().getId();
		currentThreadName = Thread.currentThread().getName();
	
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		Thread[] threadArray = threadSet.toArray(new Thread[threadSet.size()]);
		for (int i = 0; i < threadArray.length; i++) {
			Log.i(PathAndFlag.LogcatTAG,"[In thread] thread ID" + threadArray[i].getId()+ "thread name" + threadArray[i].getName());

		}

		APUsedMemory = this._context.getSharedPreferences("FillupMemory_pref", 0);
		FillupThread = this._context.getSharedPreferences("FillupThreadID", 0);
		FillupThread.edit().putLong("lastFillupThreadID", currentThreadID).commit();

		
		FileIO.WriteLogcat("FillupMemorySummary " + Memory.getAvailMemory());


		try {
			FileIO.WriteLogcat("==========fill up start========");

		

			//
			FileIO.WriteLogcat("check File Coun(first)");
			  Bundle DataMsg = new Bundle();
			  Message mesg = new Message();   
			  long Befort_AvailMemory=(Memory.getAvailMemory() /(1024*1024));
			  long After_AvailMemory=(Memory.getAvailMemory() /(1024*1024));
			  DataMsg.putLong("Befort AvailMemory", (Befort_AvailMemory));
			//write1MBlock(percentage, Memory, 1);
			  while((Befort_AvailMemory-After_AvailMemory)< _MemorySize)
			  {
		     	write10MBlock( percentage, Memory, 1);
		     	After_AvailMemory=(Memory.getAvailMemory() /(1024*1024));
			  }
			// write10MBlock_1(percentage, Memory, 1);
			 DataMsg.putLong("After AvailMemory", (Memory.getAvailMemory() /(1024*1024)));
			 mesg.setData(DataMsg);
			 mesg.what=2;
			 MainActivity.messageHandler.sendMessage(mesg);
			FileIO.WriteLogcat("check file count(last)");
			
			FileIO.WriteLogcat("==========fill up finish========");

		} catch (Exception e) {
			FileIO.WriteLogcat("==========fill up exception========");
			 Log.i(PathAndFlag.Tag,"==========fill up exception========"+  e.getMessage());
			FileIO.WriteLogcat("fill up error:" + e.getMessage());
			APIResult = "Fill up Exception";
			FileIO.WriteLogcat("==========fill up exception========");

		} finally {
			mAllocations_10=null;
			FileIO.WriteLogcat("==========finally start========");
			 Log.i(PathAndFlag.Tag,"塞Memory結束，系統剩餘記憶體:"+( Memory.getAvailMemory() /(1024*1024))+"MB");
			// mAllocations_10=null;
			isRunningFlag = this._context.getSharedPreferences(
					"isFillupRunning", 0);

			Flag = isRunningFlag.getBoolean("isRunning", false);
			Log.i(PathAndFlag.Tag, "Flag should be true=" + Flag);
			FileIO.WriteLogcat("set Running flag is false");
			isRunningFlag.edit().putBoolean("isRunning", false).commit();

			//System.gc();

			FileIO.WriteLogcat("==========finally finish========");

		}

	}

	public void write1000MBlock( int percentage,
			Memory_Info Memory, int Block) {
		try {
		
			byte[] _10M = new byte[1024 * 1024 * 10]; // 10 MB
			for (int i = 1; i <= 10; i++) {

			}

	
		} catch (Exception e) {
			FileIO.WriteLogcat("InternalThread write1000MBlock ex:"
					+ e.getMessage());
		}
	}

	public void write100MBlock( int percentage,
			Memory_Info Memory, int Block) {
		try {

			byte[] _10M = new byte[1024 * 1024 * 10]; // 10 MB
			for (int i = 1; i <= 10; i++) {

			}

		} catch (Exception e) {
			FileIO.WriteLogcat("InternalThread write100MBlock ex:"
					+ e.getMessage());
		}
	}

	public void write10MBlock( int percentage,
			Memory_Info Memory, int Block) {
		try {

			byte[] bytes_1 = new byte[1024 * 1024 * 1];
			
            new Random().nextBytes(bytes_1);
            mAllocations_10.add(new ByteArrayWrapper(bytes_1));
    
		} catch (Exception e) {
			FileIO.WriteLogcat("InternalThread write10MBlock ex:"
					+ e.getMessage());
		}
	}
	public void write10MBlock_1( int percentage,
			Memory_Info Memory, int Block) {
		try {

			byte[] bytes_1 = new byte[1024 * 1024 * 10];
			
            new Random().nextBytes(bytes_1);
             mAllocations_8.add(new ByteArrayWrapper(bytes_1));

		} catch (Exception e) {
			FileIO.WriteLogcat("InternalThread write10MBlock ex:"
					+ e.getMessage());
		}
	}
	public void write1MBlock(String path, int percentage,
			Memory_Info Memory, int Block) {
		try {
		
			rt.freeMemory();
			
			 Log.i(PathAndFlag.Tag,"執行前系統剩餘記憶體:"+( Memory.getAvailMemory() /(1024*1024))+"MB");
			 Log.i(PathAndFlag.Tag,"執行前系統剩餘記憶體 use Runtime:"+( rt.freeMemory() /(1024*1024))+"MB");
			
			for (int i=1;i<=10;i++)
			{

            
            	 Memory =new Memory_Info();
            	 byte[] bytes = new byte[1024 * 1024 * 6];
                 new Random().nextBytes(bytes);
                FillMempryServiceCall.mAllocations.add(new ByteArrayWrapper(bytes));
            
			}
			 Log.i(PathAndFlag.Tag,"執行後系統剩餘記憶體:"+( Memory.getAvailMemory() /(1024*1024))+"MB");
			 Log.i(PathAndFlag.Tag,"執行後系統剩餘記憶體 use Runtime:"+( rt.freeMemory() /(1024*1024))+"MB");
		} catch (Exception e) {
			FileIO.WriteLogcat("InternalThread write1MBlock ex:"
					+ e.getMessage());
		}
	}
    public int Pilot(int a,int b)
    {
    	int name=a;
    	int age=b;
    	return a+b;
    }
    
}
