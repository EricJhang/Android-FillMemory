package com.example.fillmemory_beta;


import java.math.BigDecimal;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.StatFs;
import android.util.Log;

public class Memory_Info {
	
	private Activity MActivity;
	final ActivityManager activityManager=(ActivityManager) MainActivity.MainActivityContext.getSystemService(Context.ACTIVITY_SERVICE);
	
	public long getTotalMemory()
	{
		
		ActivityManager.MemoryInfo info= new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
	
		//Log.i(PathAndFlag.Tag,"系統總共記憶體:"+(info.totalMem/(1024*1024))+"MB");
		//FileIO.WriteResult("系統總共記憶體:"+(info.totalMem/(1024*1024))+"MB",false);
		return info.totalMem;
	}
	public long getAvailMemory()
	{
		
		
		ActivityManager.MemoryInfo info= new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
	
		//Log.i(PathAndFlag.Tag,"系統剩餘記憶體:"+(info.availMem /(1024*1024))+"MB");
		//FileIO.WriteResult("系統剩餘記憶體:"+(info.availMem /(1024*1024))+"MB",false);
		return info.availMem;
	}
	
	public boolean getlowMemorystate()
	{
		
		ActivityManager.MemoryInfo info= new ActivityManager.MemoryInfo();
		activityManager.getMemoryInfo(info);
		
		
		Log.i(PathAndFlag.Tag,"系統是否處於low Memory 狀態:"+info.lowMemory);
		FileIO.WriteResult("系統是否處於low Memory 狀態:"+info.lowMemory,false);
		return info.lowMemory;
	}
	
	
}
