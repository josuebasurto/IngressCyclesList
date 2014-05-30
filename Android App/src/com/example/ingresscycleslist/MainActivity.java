package com.example.ingresscycleslist;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.example.ingresscycleslist.R.layout;

import android.os.Bundle;
import android.app.Activity;
import android.text.format.Time;
import android.view.Menu;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioButton;

public class MainActivity extends Activity {

	private ListView cycleList;
	private Button refreshButton;
	
	private RadioGroup radioGroupCyclesOrCheckpoints;
	private RadioButton radioButtonCycles;
	private RadioButton radioButtonCheckpoints;
	
	private ArrayAdapter<String> listAdapter ;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        cycleList = (ListView) findViewById(R.id.listView1);
//        refreshButton=(Button) findViewById(R.id.button1);   
        radioGroupCyclesOrCheckpoints = (RadioGroup) findViewById(R.id.radioGroupCyclesOrCheckpoints);
    	radioButtonCheckpoints = (RadioButton) findViewById(R.id.radioButtonCheckpoints);
    	radioButtonCycles = (RadioButton) findViewById(R.id.radioButtonCycles);
    	
        listAdapter = new ArrayAdapter<String>(this, R.layout.simplerow, new ArrayList<String>());  

        refreshList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
    public void refreshButtonClick(View view){
    	refreshList();
    }
    
    public void radioButtonCyclesClick(View view) {
    	radioButtonCheckpoints.setChecked(false);
    	radioButtonCycles.setChecked(true);
    	refreshList();
    }
    
    public void radioButtonCheckpointsClick(View view) {
    	radioButtonCheckpoints.setChecked(true);
    	radioButtonCycles.setChecked(false);
    	refreshList();
    }
    
    public void refreshList(){

    	long referenceCycleMillis=1392300000000L;
    	long nowMillis=System.currentTimeMillis();
    	int checkpointHours=5;
    	int checkpointsPerCycle=35;
    	long checkpointStep=checkpointHours*60*60*1000;
    	long cycleStep=checkpointStep*checkpointsPerCycle;
    	int listSize=100;

    	
    	listAdapter.clear();
    	
    	Calendar cal = Calendar.getInstance();
    	cal.setTimeZone(TimeZone.getDefault());
    	cal.setTimeInMillis(referenceCycleMillis);
    	Date mydate = cal.getTime();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd.MM.yyyy, HH:mm", Locale.US);

    	long myTime=referenceCycleMillis;    	
    	if(radioButtonCheckpoints.isChecked()==true){
	    	int checkpointCounter=0;
	    	while(myTime<=nowMillis+listSize*checkpointStep) {
	       		myTime+=checkpointStep;
	       		checkpointCounter++;
	       		if(checkpointCounter==checkpointsPerCycle)
	       			checkpointCounter=0;
	       		if(myTime>(nowMillis-checkpointStep)){
	       			cal.setTimeInMillis(myTime);
	            	mydate = cal.getTime();
	            	if(checkpointCounter==0){
	            		listAdapter.add(dateFormat.format(mydate)+" (Cycle end)");   
	            	}
	            	else if(myTime<nowMillis) {
	            		listAdapter.add(dateFormat.format(mydate)+ String.format(" (%d, last)",checkpointCounter));   
	            	}
	            	else {
	            		listAdapter.add(dateFormat.format(mydate)+ String.format(" (%d)",checkpointCounter));   
	            	}
	       		}
	    	}
    	}
    	else {
	    	while(myTime<=nowMillis+listSize*cycleStep) {
	       		myTime+=cycleStep;
	       		if(myTime>(nowMillis-cycleStep)){
	       			cal.setTimeInMillis(myTime);
	            	mydate = cal.getTime();
	            	if(myTime<nowMillis) {
	            		listAdapter.add(dateFormat.format(mydate)+" (Last cycle end)");  
	            	}
	            	else {
		            	listAdapter.add(dateFormat.format(mydate)+" (Cycle end)");  
	            	}
	       		}
	    	}   		
    	}
	   	   
	    
	    // Set the ArrayAdapter as the ListView's adapter.  
	    cycleList.setAdapter( listAdapter );
    }
}
