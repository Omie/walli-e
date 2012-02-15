package com.omie.wallie;
//
import java.util.Calendar;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class wallie extends Activity {
	Toast mToast;
	public static final int SELECT_FOLDER = 6541;
	public static final String PREFS_FILE = "walliePrefsFile";

	String SelectedPath;
	Boolean ShuffleMode;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);        
        setContentView(R.layout.main);
        try
        {   
            Button button = (Button)findViewById(R.id.show_save);
            button.setOnClickListener(saveClickLister);
            
            button = (Button)findViewById(R.id.show_dir );
            button.setOnClickListener(showDirClickLister);
            
            //load preferences
            SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
            SelectedPath = settings.getString("SelectedPath", "/system");
            ShuffleMode =  settings.getBoolean("ShuffleMode", false);
            
            Boolean temp;
            String tempStr;
            
            //Activated
            temp = settings.getBoolean("Activated", false);
            CheckBox tempCheckBox = (CheckBox)findViewById(R.id.show_control);
        	tempCheckBox.setChecked(temp);
        	//ShuffleMode
        	tempCheckBox = (CheckBox)findViewById(R.id.show_shuffle);
        	tempCheckBox.setChecked(ShuffleMode);        	
        	//Interval
        	EditText tempEditText = (EditText)findViewById(R.id.show_interval);
        	tempStr = settings.getString("Interval", "5"); 	            	
        	tempEditText.setText(tempStr);
        	//Location
            TextView displayLocation = (TextView)findViewById(R.id.show_disp_dir);
            displayLocation.setText(SelectedPath);
        }
        catch(Exception e)
        {
        	Toast.makeText(wallie.this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
            
    }//onCreate    
    
    private OnClickListener saveClickLister = new OnClickListener() {
        public void onClick(View v) {
        	//save preferences
        	final CheckBox checkbox = (CheckBox) findViewById(R.id.show_control);
        	Boolean do_show = checkbox.isChecked();
        	try
        	{
        		if(do_show)
        		{
	        		final EditText editText = (EditText) findViewById(R.id.show_interval);
	        		int interval = Integer.parseInt(editText.getText().toString());
	        		
	        		if(interval == 0)
	        			throw new Exception("Interval cannot be 0");
	        		
	        		CheckBox tempCheckBox = (CheckBox)findViewById(R.id.show_shuffle);
	            	ShuffleMode = tempCheckBox.isChecked();
	            	
	            	TextView tempTextView = (TextView)findViewById(R.id.show_disp_dir);
	            	SelectedPath = tempTextView.getText().toString();	            	
	        		//write settings
	        		SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    
                    editor.putString("SelectedPath", SelectedPath);
                    editor.putBoolean("ShuffleMode", ShuffleMode);
                    // Commit the edits!
                    editor.commit();	        		
                    
                    RegisterSlideShow(interval);
                    //exit                    
                    Toast.makeText(wallie.this, "Changes saved", Toast.LENGTH_LONG).show();
                    
        		}
        		else
        		{
        			UnRegisterSlideShow();
                    Toast.makeText(wallie.this, "Changes saved", Toast.LENGTH_LONG).show();
                    
        		}
        	}
        	catch(Exception e)
        	{
        		Toast.makeText(wallie.this, e.getMessage() , Toast.LENGTH_LONG).show();
        	}//try-catch
        	
        }
    };//saveClickListener
    
    @Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		Boolean temp;
		String tempStr;
		
		SharedPreferences settings = getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = settings.edit();		
		
		CheckBox tempCheckBox = (CheckBox)findViewById(R.id.show_control);
    	temp = tempCheckBox.isChecked();
    	editor.putBoolean("Activated", temp);
    	
    	tempCheckBox = (CheckBox)findViewById(R.id.show_shuffle);
    	temp = tempCheckBox.isChecked();
    	editor.putBoolean("ShuffleMode", temp);    	
    	
    	EditText tempEditText = (EditText)findViewById(R.id.show_interval);
    	tempStr = tempEditText.getText().toString();	            	
    	editor.putString("Interval", tempStr);
    	
    	TextView tempTextView = (TextView)findViewById(R.id.show_disp_dir);
    	SelectedPath = tempTextView.getText().toString();	            	
    	editor.putString("SelectedPath", SelectedPath);
		
        // Commit the edits!
        editor.commit();
	}

	//Open The New Screen
    private OnClickListener showDirClickLister = new OnClickListener() {
        public void onClick(View v) {
        	
        	//launch new activity
        	try
        	{
        		Intent intent = new Intent(wallie.this, com.omie.wallie.SelectFolder.class);
        	    startActivityForResult(intent, SELECT_FOLDER);
        	}
        	catch(Exception e)
        	{
        		Toast.makeText(wallie.this, e.getMessage(), Toast.LENGTH_LONG).show();
        	}//try-catch
        	
        }
    };//showDirClickListener
    
 // Listen for results.
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	try
    	{
	        switch (requestCode) {
	            case SELECT_FOLDER:
	                // This is the standard resultCode that is sent back if the
	                // activity crashed or didn't doesn't supply an explicit result.
	                if (resultCode != RESULT_CANCELED){
	                	//write preferences	                	
	                	SelectedPath = data.getStringExtra("SelectedPath");
	                	TextView displayLocation = (TextView)findViewById(R.id.show_disp_dir);
	                    displayLocation.setText(SelectedPath);
	                }
	            default:
	                break;
	        }
    	}
        catch(Exception e)
        {	        	
        	Toast.makeText(wallie.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
    
    private void RegisterSlideShow(int _interval)
    {
    	Intent intent = new Intent(wallie.this, SetWallpaperJob.class);
        PendingIntent sender = PendingIntent.getBroadcast(wallie.this,
                0, intent, 0);
       
        Calendar calendar = Calendar.getInstance();
        // Schedule the alarm!
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        
        calendar.add(Calendar.MINUTE, _interval);
        am.setRepeating(AlarmManager.RTC_WAKEUP ,
        		calendar.getTimeInMillis(), _interval*60000, sender);
    	
    }
    
    private void UnRegisterSlideShow()
    {
    	Intent intent = new Intent(wallie.this,SetWallpaperJob.class);
        PendingIntent sender = PendingIntent.getBroadcast(wallie.this,
                0, intent, 0);
        
        // cancel the alarm.
        AlarmManager am = (AlarmManager)getSystemService(ALARM_SERVICE);
        am.cancel(sender);
    }
}