package com.example.fillmemory_beta;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.util.Log;



public class FileIO {
	public static String CurrentTime(){
		Date now = new Date();
		return new java.text.SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(now);
	}	
	
	public static String CurrentTime2(){
		Date now = new Date();
		return new java.text.SimpleDateFormat("yyyy-MM-dd HHmmss").format(now);
	}	
	
	
	public static void CreateResultFile(){
		try{
			
			
			File file = new File(PathAndFlag.path_result);
			if(file.exists()){
				file.delete();
				}
			else
			file.getParentFile().mkdir();
			file.createNewFile();
			//Log.d(PathAndFlag.LogcatTAG,"Create "+PathAndFlag.path_APIResult);
			//boolean create = file.createNewFile();			
			//Log.d(PathAndFlag.LogcatTAG,"Result of creating = "+String.valueOf(create));
		}
		catch(Exception e){
			//WriteLogcat("Create API result file error : " + e.getMessage());
		}
	}
	
	//
	public static void WriteResult(String log, boolean writeLogcat){
		if(writeLogcat){
			WriteLogcat(log);
		}
		try{
			BufferedWriter bw = null;
			try{
				FileWriter fw = new FileWriter(PathAndFlag.path_result, true);
		        bw = new BufferedWriter(fw); //
		        log = "["+CurrentTime()+"] [Action] "+log;
		        bw.write(log);
		        bw.newLine();
		        bw.flush();
		        bw.close();
		    }catch(IOException e){
		    	WriteLogcat("Write the log to file error" + e.getMessage());
		    }
		}
		catch(Exception e){
		}
	}
	public static void WriteLogcat(String log){
		Log.d(PathAndFlag.LogcatTAG,log);
	}
	
}
