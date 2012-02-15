package com.omie.wallie;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectFolder extends ListActivity{

	 	private List<String> item = null;
	 	private List<String> path = null;
	 	private String root="/";
	 	private TextView myPath;
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	    	super.onCreate(savedInstanceState);
	    	setContentView(R.layout.main2);
        	
	    	try
	        {	        	
	        	Button button = (Button)findViewById(R.id.show_select);
	            button.setOnClickListener(selectClickLister);
	            
	            myPath = (TextView)findViewById(R.id.path);
	            
	            File f = new File("/sdcard/");
	            if(f.exists())
	            	getDir(root + "sdcard");
	            else
	            	getDir(root);
	        }
	        catch(Exception e)
	        {	        	
	        	Toast.makeText(SelectFolder.this, e.toString(), Toast.LENGTH_SHORT).show();
	        }
	    }//on create
	    
	    private OnClickListener selectClickLister = new OnClickListener() {
	        public void onClick(View v) {
	        	//select current directory
	        	try
	        	{
	        		Intent resultIntent = new Intent();
	        		resultIntent.putExtra("SelectedPath", myPath.getText());
	        		setResult(RESULT_OK, resultIntent);
	        		finish();
	        	}
	        	catch(Exception e)
	        	{
	        		Intent resultIntent = new Intent();	        		
	        		setResult(RESULT_CANCELED, resultIntent);
	        		finish();
	        		//Toast.makeText(SelectFolder.this, e.toString(), Toast.LENGTH_SHORT).show();
	        	}//try-catch
	        	
	        }
	    };//selectClickListener
	    
	    private void getDir(String dirPath)
	    {
	    	try
	    	{
			     myPath.setText(dirPath);
			     
			     item = new ArrayList<String>();
			     path = new ArrayList<String>();
			     
			     File f = new File(dirPath);
			     File[] files = f.listFiles();
			     
			     if(!dirPath.equals(root))
			     {
		
			      item.add(root);
			      path.add(root);
			      
			      item.add("../");
			      path.add(f.getParent());
			            
			     }
			     
			     for(int i=0; i < files.length; i++)
			     {
			       File file = files[i];			       
			       if(file.isDirectory() && file.canRead())
			    	  {
			    	   path.add(file.getPath());
			    	   item.add(file.getName() + "/");
			    	  }
			     }
		
			     ArrayAdapter<String> fileList =
			      new ArrayAdapter<String>(this, R.layout.row, item);
			     setListAdapter(fileList);
	    	}
	        catch(Exception e)
	        {	        	
	        	Toast.makeText(SelectFolder.this, e.toString(), Toast.LENGTH_SHORT).show();
	        }
	    }

	 @Override
	 protected void onListItemClick(ListView l, View v, int position, long id) {
	  
	  File file = new File(path.get(position));
	  
	  if (file.isDirectory())
	  {
	   if(file.canRead())
	    getDir(path.get(position));
	   else
	   {
		   Toast.makeText(SelectFolder.this,"Cannot Open Folder", Toast.LENGTH_SHORT).show();
	   }
	  }//end if File.IsDirectory
	 }//end onListItemClick	
	
}//end class
