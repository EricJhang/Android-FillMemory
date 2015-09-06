package com.example.fillmemory_beta;

import android.os.Build;

public class PathAndFlag {
final static String Tag = "spirentBatterLifeTest";
	
	//API Tab in Android log cat
	public static String LogcatTAG="spirentBatterLifeTest";
	//Root directory of Application
	//public static String path_spirent = "/data/data/com.asus.at.spirentBatterLifeTest/";
	public static String path_spirent = "/sdcard/";
	
	
	//Path of test Result log 
	public static String path_result = path_spirent + FileIO.CurrentTime2()+"Result.txt";
	

}
