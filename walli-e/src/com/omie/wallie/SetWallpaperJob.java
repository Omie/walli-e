package com.omie.wallie;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Random;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

public class SetWallpaperJob extends BroadcastReceiver {
	
	public static final String PREFS_FILE = "walliePrefsFile";
	String SelectedPath;
	Boolean ShuffleMode;
	static int FileNumber=0;
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		
    	try {
            //load preferences
    		SharedPreferences settings =  context.getSharedPreferences(PREFS_FILE, 0);
            SelectedPath = settings.getString("SelectedPath", "/system");
            ShuffleMode =  settings.getBoolean("ShuffleMode", false); 
            
            String FileName;
            
            File file = new File(SelectedPath);
            
            FilenameFilter only = new OnlyExt("jpg");
            
            File[] imageFiles = file.listFiles(only);
            
            if(imageFiles.length > 0 )
            {
            	
            	if(ShuffleMode)
            	{
            		final Random myRandom = new Random();
            		FileNumber = myRandom.nextInt(imageFiles.length);
            		FileName = imageFiles[FileNumber].getName();            		
            	}
            	else
            	{
            		FileName = imageFiles[FileNumber++].getName();
            	}
            	if(FileNumber == imageFiles.length)
        			FileNumber=0;
            	
            	final WallpaperManager wallpaperManager = 
        			WallpaperManager.getInstance(context);           	
            	
        	
        		Bitmap myBitmap =
        			BitmapFactory.decodeFile(SelectedPath + "/" + FileName);    	    		
            
                wallpaperManager.setBitmap(myBitmap);
                            	
            }
            else
            {
            	throw new Exception("No Images Found - walli-e");            	
            }
    		
        }
        catch(Exception ae){
        	Toast.makeText(context,ae.getMessage(), Toast.LENGTH_LONG).show();
        }

	}//end onReceive


	private class OnlyExt implements FilenameFilter
	{
		String ext;
	
		public OnlyExt(String ext)
		{
			this.ext = "." + ext;
		}
		public boolean accept(File dir, String name)
		{
			return name.endsWith(ext);
		}
	}
	
}//end main class
