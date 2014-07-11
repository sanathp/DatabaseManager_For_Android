package com.pskapps.dialonce;

//all required import files
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData.Item;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class DataViewer extends Activity implements OnItemClickListener {
	
//a static class to save cursor,table values etc which can be used any function in the program.Using this class functions talk to each other ( i mean sharing data)
	static class indexInfo
    {
    	public static int index = 10;
    	public static int numberofpages = 0;
    	public static int currentpage = 0;
    	public static String table_name="";
    	public static Cursor maincursor;
    	public static ArrayList<String> value_string;
    	public static ArrayList<String> tableheadernames;
    }
	
// all global variables
	DatabaseManager dbm;
	TableLayout tableLayout;
	TableRow.LayoutParams tableRowParams;
	HorizontalScrollView hsv;
	LinearLayout tempfirst;
	TextView tvmessage;
	Button previous;
	Button next;
	indexInfo info = new indexInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//the main linear layout to which all tables spinners etc will be added.In this activity every element is created dynamically  to avoid using xml file
		tempfirst = new LinearLayout(DataViewer.this);
		tempfirst.setOrientation(LinearLayout.VERTICAL);
		tempfirst.setBackgroundColor(Color.WHITE);

		setContentView(tempfirst);
               //the first row of layout which has a text view and spinner
		LinearLayout firstrow = new LinearLayout(DataViewer.this);
		LinearLayout.LayoutParams firstrowlp = new LinearLayout.LayoutParams(0, 100);
		firstrowlp.weight = 1;
		
		TextView maintext = new TextView(DataViewer.this);
		maintext.setText("Select Table");
		maintext.setLayoutParams(firstrowlp);
		Spinner select_table=new Spinner(DataViewer.this);
		select_table.setLayoutParams(firstrowlp);
		firstrow.addView(maintext);
		firstrow.addView(select_table);
		tempfirst.addView(firstrow);
		ArrayList<Cursor> alc ;
               
                //the horizantal scroll view for table if the table content doesnot fit into screen
		 hsv = new HorizontalScrollView(DataViewer.this);
		 
	//the main table layout where the content of the sql tables will be displayed when user selects a table	
    	tableLayout = new TableLayout(DataViewer.this);
    	tableLayout.setHorizontalScrollBarEnabled(true);
    	
    	hsv.addView(tableLayout);

		//the database manager object using which we speak to sqllite database of the app
		dbm = new DatabaseManager(getApplicationContext());

		//the second row of the layout which shows number of reocrds in the table selected by user
		LinearLayout secondrow = new LinearLayout(DataViewer.this);
		LinearLayout.LayoutParams secondrowlp = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		secondrowlp.weight = 1;
		TextView secondrowtext = new TextView(DataViewer.this);
		secondrowtext.setText("No. Of Records : ");
		secondrowtext.setLayoutParams(secondrowlp);
		final TextView tv =new TextView(DataViewer.this);
		tv.setLayoutParams(secondrowlp);
		secondrow.addView(secondrowtext);
		secondrow.addView(tv);
		tempfirst.addView(secondrow);
		
                // the spinner which gives user a option to add new row , drop or delete table
		final Spinner spinnertable =new Spinner(DataViewer.this);
		tempfirst.addView(spinnertable);
		tempfirst.addView(hsv);
		//the third layout which has buttons for the pagination of content from database
		LinearLayout thirdrow = new LinearLayout(DataViewer.this);
		previous = new Button(DataViewer.this);
		previous.setText("Previous");
		previous.setLayoutParams(secondrowlp);
		next = new Button(DataViewer.this);
		next.setText("Next");
		next.setLayoutParams(secondrowlp);
		thirdrow.addView(previous);
		thirdrow.addView(next);
		tempfirst.addView(thirdrow);

		//the text view at the bottom of the screen which displays error or success messages after a query is executed
		tvmessage =new TextView(DataViewer.this);
		
		tvmessage.setText("Error Messages will be displayed here");
		String Query = "SELECT name _id FROM sqlite_master WHERE type ='table'";
		tempfirst.addView(tvmessage);

	//layout parameters for each row in the table
       tableRowParams = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
       tableRowParams.setMargins(0, 0, 2, 0);
	
	// a query which returns a cursor with the list of tables in the database.We use this cursor to populate spinner in the first row
		alc = dbm.getData(Query);
		final Cursor c=alc.get(0);
		Cursor Message =alc.get(1);
		Message.moveToLast();
		String msg = Message.getString(0);
		Log.d("Message from sql = ",msg);
		
		if(c!=null)
		{
		Log.d("count ",""+c.getCount());
		
		}
		c.moveToFirst();
		while(c.moveToNext())
		{
			
			Log.d("Table Name ",""+c.getString(0));
		}
		
		 SimpleCursorAdapter countrycursor = new SimpleCursorAdapter(this,
					android.R.layout.simple_list_item_1, c,
		                new String[] { "_id"},
		                new int[] { android.R.id.text1 }, 0);
			 
		 if(countrycursor!=null)
		 {
		 select_table.setAdapter(countrycursor);
		 }
		
		 select_table.setOnItemSelectedListener(new OnItemSelectedListener() {

	            @Override
	            public void onItemSelected(AdapterView<?> parent,
	                    View view, int pos, long id) {
	            	c.moveToPosition(pos);
	            	//displaying the content of the table which is selected in the select_table spinner
	            	Log.d("selected table name is",""+c.getString(0));
	            	indexInfo.table_name=c.getString(0);
	            	tvmessage.setText("Error Messages will be displayed here");
	               
	            	 //removes any data if present in the table layout
                       tableLayout.removeAllViews();
	            	ArrayList<String> spinnertablevalues = new ArrayList<String>();
	            	spinnertablevalues.add("Click here to change this table");
	                spinnertablevalues.add("Add row");
	                spinnertablevalues.add("Delete");
	                spinnertablevalues.add("Drop");
	                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnertablevalues);
	                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
	               
			// a array adapter which add values to the spinner which helps in user making changes to the table
	                ArrayAdapter<String> adapter = new ArrayAdapter<String>(DataViewer.this,
	                		android.R.layout.simple_spinner_item, spinnertablevalues) {

	                    public View getView(int position, View convertView, ViewGroup parent) {
	                            View v = super.getView(position, convertView, parent);
	                            
	                            v.setBackgroundColor(Color.WHITE);
	                         
	                            return v;
	                    }


	                    public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
	                             View v =super.getDropDownView(position, convertView, parent);

	                            v.setBackgroundColor(Color.WHITE);

	                            return v;
	                    }
	            };


	                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);                                 
	                spinnertable.setAdapter(adapter);
	            	String Query2 ="select * from "+c.getString(0);
	            	Log.d("",""+Query2);
	            	
	            	//getting contents of the table which user selected from the select_table spinner
	            	ArrayList<Cursor> alc2=dbm.getData(Query2);
	            	final Cursor c2=alc2.get(0);
	            	//saving cursor to the static indexinfo class which can be resued by the other functions
	            	indexInfo.maincursor=c2;
	            	
	            	// if the cursor returened form tha database is not null we display the data in table layout
	            	if(c2!=null)
	            	{
	            	int counts = c2.getCount();
	            	
	            	Log.d("counts",""+counts);
	            	tv.setText(""+counts);
	                
	                
	            	
	            	
	            	
	            	
	            	//the spinnertable has the 3 items to drop , delete , add row to the table selected by the user
	            	//here we handle the 3 operations.
	            	spinnertable.setOnItemSelectedListener((new AdapterView.OnItemSelectedListener() {
	            	    @Override
	            	    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
	            	     
	                    	
	                    	((TextView)parentView.getChildAt(0)).setTextColor(Color.rgb(0,0,0));
	                    	//when user selects to drop the table the below code in if block will be executed
	                    	if(spinnertable.getSelectedItem().toString().equals("Drop"))
	                    	{
	                    		// an alert dialog to confirm user selection
	                    		runOnUiThread(new Runnable() {
	                    			   @Override
	                    			   public void run() {
	                    				if(!isFinishing()){
	                    					
		                    						        new AlertDialog.Builder(DataViewer.this)
		                    							.setTitle("Are you sure ?")
		                    							.setMessage("Pressing yes will remove the table from the database")
		                    							.setPositiveButton("yes", 
		                          							new DialogInterface.OnClickListener() {
		                          							// when user confirms by clicking on yes we drop the table by executing drop table query 	
		                  								public void onClick(DialogInterface dialog, int which) {
		                  			                        
									                    		String Query6 = "Drop table "+indexInfo.table_name;
									                    		ArrayList<Cursor> aldropt=dbm.getData(Query6);
																	Cursor tempc=aldropt.get(1);
																	tempc.moveToLast();
																	Log.d("Drop table Mesage",tempc.getString(0));
																	if(tempc.getString(0).equalsIgnoreCase("Success"))
																	{
																		tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
																		tvmessage.setText(indexInfo.table_name+"Dropped successfully");
																	}
																	else
																	{
																	//if there is any error we displayd the error message at the bottom of the screen	
																	tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
																	tvmessage.setText("Error:"+tempc.getString(0));
																	}
		                  								}})
		                  								.setNegativeButton("No", 
			                          							new DialogInterface.OnClickListener() {
			                  								public void onClick(DialogInterface dialog, int which) {
			                  			                                         						
			                  								}

			  												
			                  							})
			                   							.create().show();
			                   						     
			                   				   }
			                   			   }
			                   			});
	                    		
	                    	}
	                    	//when user selects to drop the table the below code in if block will be executed
	                    	if(spinnertable.getSelectedItem().toString().equals("Delete"))
	                    	{	// an alert dialog to confirm user selection
	                    		runOnUiThread(new Runnable() {
	                    			   @Override
	                    			   public void run() {
	                    				if(!isFinishing()){
	                    					
		                    						        new AlertDialog.Builder(DataViewer.this)
		                    							.setTitle("Are you sure")
		                    							.setMessage("are you sure asshole ?")
		                    							.setPositiveButton("yes", 
		                          							new DialogInterface.OnClickListener() {
		                          								
		                          							// when user confirms by clicking on yes we drop the table by executing delete table query 
		                  								public void onClick(DialogInterface dialog, int which) {
		                  			                        
																	                    		
									                    		String Query7 = "Delete  from "+indexInfo.table_name;
									                    		Log.d("delete table query",Query7);
									                    		ArrayList<Cursor> aldeletet=dbm.getData(Query7);
																	Cursor tempc=aldeletet.get(1);
																	tempc.moveToLast();
																	Log.d("Delete table Mesage",tempc.getString(0));
																	if(tempc.getString(0).equalsIgnoreCase("Success"))
																	{
																		tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
																		tvmessage.setText(indexInfo.table_name+" table content deleted successfully");
																	}
																	else
																	{
																	tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
																	tvmessage.setText("Error:"+tempc.getString(0));
																	}
														
		                  								
		                  								}})
														.setNegativeButton("No", 
						                          							new DialogInterface.OnClickListener() {
						                  								public void onClick(DialogInterface dialog, int which) {
						                  			                                         						
						                  								}

						  												
						                  							})
						                   							.create().show();
						                   						     
	                    				   }
		                   			   }
		                   			});
                    		
	                    	}
	                    	
	                    	//when user selects to add row to the table the below code in if block will be executed
	                    	if(spinnertable.getSelectedItem().toString().equals("Add row"))
	                    	{
	                    		//we create a layout which has textviews with column names of the table and edittexts where
	                    		//user can enter value which will be inserted into the datbase.
	                    		final LinkedList<TextView> addnewrownames = new LinkedList<TextView>();
	                        	  final LinkedList<EditText> addnewrowvalues = new LinkedList<EditText>();
	                        	  
	                        	  for(int i=0;i<c2.getColumnCount();i++)
	                        	  {
	                        	  String cname = c2.getColumnName(i);
	                        	  TextView tv = new TextView(getApplicationContext());
	                        	  tv.setText(cname);
	                        	  addnewrownames.add(tv);
	                        	  
	                        	  }  
	                        	for(int i=0;i<addnewrownames.size();i++)
	                        	  {
	                        	  EditText et = new EditText(getApplicationContext());
	                        	  
	                        	  addnewrowvalues.add(et);
	                        	  }
	                        	
	  								int lastrid = 0;
	                            final RelativeLayout addnewlayout = new RelativeLayout(DataViewer.this);
	                        	 RelativeLayout.LayoutParams addnewparams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	                        	addnewparams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
	                        	    for(int i=0;i<addnewrownames.size();i++)
	                        	    {	
	                        	    TextView tv =addnewrownames.get(i);
	                        	    EditText et=addnewrowvalues.get(i);
	                        	    int t = i+400;
	                        	    int k = i+500;
	                        	    int lid = i+600;
	                        	   
	                        	    tv.setId(t);
	                        	    tv.setTextColor(Color.parseColor("#000000"));
	                                et.setBackgroundColor(Color.parseColor("#FFFFFF"));
	                                et.setTextColor(Color.parseColor("#000000"));
	                        	    et.setId(k);
	                        	    final LinearLayout ll = new LinearLayout(DataViewer.this);
	                            	
	                        	    LinearLayout.LayoutParams tvl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	                        	    tvl.weight=(float)0.43;
	                        	    
	                        	    ll.addView(tv,tvl);
	                        	    
	                        	    LinearLayout.LayoutParams etl = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
	                        	    etl.weight=(float)0.67;
	                                ll.addView(et,etl);
	                                ll.setId(lid);
	                                
	                        	    Log.d("Edit Text Value",""+et.getText().toString());
	                        	    
	                        	    RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	                          	  	rll.addRule(RelativeLayout.BELOW,ll.getId()-1 );
	                        	    
	                          	  	lastrid=ll.getId();
	                        	    addnewlayout.addView(ll, rll);
	                        	    
	                        	    }
	                          	
	                          	Log.d("Button Clicked", "");
	                          	//the above form layout which we have created above will be displayed in an alert dialog
	                          	runOnUiThread(new Runnable() {
	                    			   @Override
	                    			   public void run() {
	                    				if(!isFinishing()){
	                    					
	                    							
	                    					
	                    						        new AlertDialog.Builder(DataViewer.this)
	                    							.setTitle("values")
	                    							.setCancelable(false)
	                    							.setView(addnewlayout)
	                    							.setPositiveButton("Add", 
	                          							new DialogInterface.OnClickListener() {
	                          							// after entering values if user clicks on add we take the values and run a ainsert query
	                  								public void onClick(DialogInterface dialog, int which) {
	                  			                        
	                  									indexInfo.index = 10;
	                  									//tableLayout.removeAllViews();
	                  									//trigger select table listner to be triggerd
	                  									String Query4 ="Insert into "+indexInfo.table_name+" (";
	                  									for(int i=0 ; i<addnewrownames.size();i++)
	                  									{
	                  										
	                  										TextView tv = addnewrownames.get(i);
	                  										tv.getText().toString();
	                  										if(i==addnewrownames.size()-1)
	                  										{
	                  										
	                  											Query4=Query4+tv.getText().toString();
	                  										
	                  										}
	                  										else
	                  										{
	                  											Query4=Query4+tv.getText().toString()+", ";
	                  										}
	                  										
	                  										
	                  										
	                  									}
	                  									Query4=Query4+" ) VALUES ( ";
	                  									for(int i=0 ; i<addnewrownames.size();i++)
	                  									{
	                  										EditText et = addnewrowvalues.get(i);
	                  										et.getText().toString();
	                  										
	                  										if(i==addnewrownames.size()-1)
	                  										{

	                      										Query4=Query4+"'"+et.getText().toString()+"' ) ";	
	                  										}
	                  										else
	                  										{
	                  										Query4=Query4+"'"+et.getText().toString()+"' , ";
	                  										}
	                  										
	                  										
	                  									}
	                  									//this is the insert query which has been generated
	                  									Log.d("Insert Query",Query4);
	                  									ArrayList<Cursor> altc=dbm.getData(Query4);
	                  									Cursor tempc=altc.get(1);
	                  									tempc.moveToLast();
	                  									Log.d("Add New Row",tempc.getString(0));
	                  									if(tempc.getString(0).equalsIgnoreCase("Success"))
														{
															tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
															tvmessage.setText("New Row added succesfully to "+indexInfo.table_name);
														}
														else
														{
														tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
														tvmessage.setText("Error:"+tempc.getString(0));
														}
	                  									
	                  									}
	                  							})
	                    							.setNegativeButton("close", 
	                          							new DialogInterface.OnClickListener() {
	                  								public void onClick(DialogInterface dialog, int which) {
	                  			                                         						
	                  								}

	  												
	                  							})
	                   							.create().show();
	                   						     
	                   				   }
	                   			   }
									
	                   			});
	                    	}

	                    }
	                    public void onNothingSelected(AdapterView<?> arg0) { }
	                }));
	            	
	            	//display the first row of the table with column names of the table selected by the user
	                TableRow tableheader = new TableRow(getApplicationContext());
	                
	                tableheader.setBackgroundColor(Color.BLACK);
            		tableheader.setPadding(0, 2, 0, 2);
	                for(int k=0;k<c2.getColumnCount();k++)
	                {
	                	LinearLayout cell = new LinearLayout(DataViewer.this);
	               	 cell.setBackgroundColor(Color.WHITE);
	               	 cell.setLayoutParams(tableRowParams);
	                final TextView tableheadercolums = new TextView(getApplicationContext());
	               // tableheadercolums.setBackgroundDrawable(gd);
	                tableheadercolums.setPadding(0, 0, 4, 3);
	                tableheadercolums.setText(""+c2.getColumnName(k)); 
	                tableheadercolums.setTextColor(Color.parseColor("#000000"));
	               
	                //columsView.setLayoutParams(tableRowParams);
	                cell.addView(tableheadercolums);
	                tableheader.addView(cell);
	                
	                }
	                tableLayout.addView(tableheader);
	                c2.moveToFirst();
	               
	               //after displaying columnnames in the first row  we display data in the remaining columns
	               //the below paginatetbale function will display the first 10 tuples of the tables
	               //the remaning tuples can be viewed by clicking on the next button
	                paginatetable(c2.getCount());
	            	}
	            	else{
	            	//if the cursor returned from the database is empty we show that table is empty 	
	            		tableLayout.removeAllViews();
	            		TableRow tableheader2 = new TableRow(getApplicationContext());
	            		tableheader2.setBackgroundColor(Color.BLACK);
	            		tableheader2.setPadding(0, 2, 0, 2);
	            		
		                
	            			LinearLayout cell = new LinearLayout(DataViewer.this);
	   	               	 cell.setBackgroundColor(Color.WHITE);
	   	               	 cell.setLayoutParams(tableRowParams);
		                final TextView tableheadercolums = new TextView(getApplicationContext());

		                
		                tableheadercolums.setPadding(0, 0, 4, 3);
		                tableheadercolums.setText("   Table   Is   Empty   "); 
		                tableheadercolums.setTextSize(30);
		                tableheadercolums.setTextColor(Color.RED);
		                
		                cell.addView(tableheadercolums);
		                tableheader2.addView(cell);
		                
		                
		                tableLayout.addView(tableheader2);
		                
	            		tv.setText(""+0);
	            	}
	            	
	            	
	            }
				@Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	           

	            }
	        });
		 
	
	}
	//displays alert dialog from which use can update or delete a row 
	public void updateDeletePopup(int row)
	{
		String text="";
		
		Cursor c2=indexInfo.maincursor;
	// a spinner which gives options to update or delete the row which user has selected
  	  ArrayList<String> spinnerArray = new ArrayList<String>();
  	    spinnerArray.add("Click Here to Change this row");
  	    spinnerArray.add("Update this row");
  	    spinnerArray.add("Delete this row");

	//create a layout with text values which has the column names and 
	//edit texts which has the values of the row which user has selected
      	final ArrayList<String> value_string = indexInfo.value_string;
  	  final LinkedList<TextView> columnames = new LinkedList<TextView>();
  	  final LinkedList<EditText> columvalues = new LinkedList<EditText>();
  	  
  	  for(int i=0;i<c2.getColumnCount();i++)
  	  {
  	  String cname = c2.getColumnName(i);
  	  TextView tv = new TextView(getApplicationContext());
  	  tv.setText(cname);
  	  columnames.add(tv);
  	  
  	  }  
  	for(int i=0;i<columnames.size();i++)
  	  {
  	  
  	  
  	  String cv =value_string.get(i);
  	  EditText et = new EditText(getApplicationContext());
  	  value_string.add(cv);
  	  et.setText(cv);
  	  columvalues.add(et);
  	  }
  	
  	int lastrid = 0;
  	// all text views , edit texts are added to this relative layout lp
      final RelativeLayout lp = new RelativeLayout(DataViewer.this);
      lp.setBackgroundColor(Color.WHITE);
  	 RelativeLayout.LayoutParams lay = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
  	    lay.addRule(RelativeLayout.ALIGN_PARENT_TOP);
  	    for(int i=0;i<columnames.size();i++)
  	    {	
  	    TextView tv =columnames.get(i);
  	    EditText et=columvalues.get(i);
  	    int t = i+100;
  	    int k = i+200;
  	    int lid = i+300;
  	   
  	    tv.setId(t);
  	    tv.setTextColor(Color.parseColor("#000000"));
          et.setBackgroundColor(Color.parseColor("#F2F2F2"));
          
          et.setTextColor(Color.parseColor("#000000"));
  	    et.setId(k);
  	    Log.d("text View Value",""+tv.getText().toString());
  	    final LinearLayout ll = new LinearLayout(DataViewer.this);
      	ll.setBackgroundColor(Color.parseColor("#FFFFFF"));
      	ll.setId(lid);
      	LinearLayout.LayoutParams lpp = new LinearLayout.LayoutParams(0, 100);
        lpp.weight = 1;
        tv.setLayoutParams(lpp);
        et.setLayoutParams(lpp);
  	    ll.addView(tv);
  	    ll.addView(et);
  	    
  	    Log.d("Edit Text Value",""+et.getText().toString());
  	    
  	    RelativeLayout.LayoutParams rll = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	  	rll.addRule(RelativeLayout.BELOW,ll.getId()-1 );
    	  	rll.setMargins(0, 20, 0, 0);
    	  	lastrid=ll.getId();
  	    lp.addView(ll, rll);
  	    
  	    }
  	    
  	    LinearLayout lcrud = new LinearLayout(DataViewer.this);
      	
  	    LinearLayout.LayoutParams paramcrudtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  	    
  	  paramcrudtext.setMargins(0, 20, 0, 0);
  	  
  	  //spinner which displaye update , delete options
          final Spinner crud_dropdown = new Spinner(getApplicationContext());
          
          ArrayAdapter<String> crudadapter = new ArrayAdapter<String>(DataViewer.this,
          		android.R.layout.simple_spinner_item, spinnerArray) {

              public View getView(int position, View convertView, ViewGroup parent) {
                      View v = super.getView(position, convertView, parent);

                      v.setBackgroundColor(Color.WHITE);
                      
                      return v;
              }


              public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
                       View v =super.getDropDownView(position, convertView, parent);

                      v.setBackgroundColor(Color.WHITE);

                      return v;
              }
      };


      crudadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
          
        
          
          crud_dropdown.setAdapter(crudadapter);
          lcrud.addView(crud_dropdown,paramcrudtext);

          RelativeLayout.LayoutParams rlcrudparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	  	rlcrudparam.addRule(RelativeLayout.BELOW,lastrid);
  	    
  	    lp.addView(lcrud, rlcrudparam);
  	   
        //after the layout has been created display it in a alert dialog  
  	runOnUiThread(new Runnable() {
		   @Override
		   public void run() {
			if(!isFinishing()){
				
				
					        new AlertDialog.Builder(DataViewer.this)
						.setTitle("values")
						.setView(lp)
						.setCancelable(false)
						.setPositiveButton("Ok", 
  							new DialogInterface.OnClickListener() {
  							
  							//this code will be executed when user changes values of edit text or spinner and clicks on ok button	
							public void onClick(DialogInterface dialog, int which) {
								
								//get spinner value
								String spinner_value = crud_dropdown.getSelectedItem().toString();
								
								//it he spinner value is update this row get the values from 
								//edit text fields generate a update query and execute it
								if(spinner_value.equalsIgnoreCase("Update this row"))
								{
									indexInfo.index = 10;
								String Query3="UPDATE "+indexInfo.table_name+" SET ";
								
								for(int i=0;i<columnames.size();i++)
								{
									TextView tvc = columnames.get(i);
									EditText etc = columvalues.get(i);
									
									if(!etc.getText().toString().equals("null"))
									{

										Query3=Query3+tvc.getText().toString()+" = ";
										
									if(i==columnames.size()-1)
									{
										
										Query3=Query3+"'"+etc.getText().toString()+"'";
										
									}
									else{
										
											Query3=Query3+"'"+etc.getText().toString()+"' , ";
										
									}
									}
									
								}
								Query3=Query3+" where ";
								for(int i=0;i<columnames.size();i++)
								{
									TextView tvc = columnames.get(i);
									if(!value_string.get(i).equals("null"))
									{

									Query3=Query3+tvc.getText().toString()+" = ";
									
									if(i==columnames.size()-1)
									{

									Query3=Query3+"'"+value_string.get(i)+"' ";
									
									}
									else
									{
										Query3=Query3+"'"+value_string.get(i)+"' and ";
									}
									
									}
								}
								
								
								Log.d("Update Query",Query3);
								
								//dbm.getData(Query3);
								ArrayList<Cursor> aluc=dbm.getData(Query3);
								
								Cursor tempc=aluc.get(1);
								
								tempc.moveToLast();
								Log.d("Update Mesage",tempc.getString(0));
								
								if(tempc.getString(0).equalsIgnoreCase("Success"))
								{
									tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
									tvmessage.setText(indexInfo.table_name+" table Updated Successfully");
								}
								else
								{
								tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
								tvmessage.setText("Error:"+tempc.getString(0));
								}
								
								
								}
								//it he spinner value is delete this row get the values from 
								//edit text fields generate a delete query and execute it
								
								if(spinner_value.equalsIgnoreCase("Delete this row"))
								{
									
									indexInfo.index = 10;
									String Query5="DELETE FROM "+indexInfo.table_name+" WHERE ";
									
									for(int i=0;i<columnames.size();i++)
									{
										TextView tvc = columnames.get(i);
										if(!value_string.get(i).equals("null"))
										{

										Query5=Query5+tvc.getText().toString()+" = ";
										
										if(i==columnames.size()-1)
										{

										Query5=Query5+"'"+value_string.get(i)+"' ";
										
										}
										else
										{
											Query5=Query5+"'"+value_string.get(i)+"' and ";
  									}
										
										}
									}
									
									
									Log.d("Delete Query",Query5);
									
									dbm.getData(Query5);

									ArrayList<Cursor> aldc=dbm.getData(Query5);
									Cursor tempc=aldc.get(1);
									tempc.moveToLast();
									Log.d("Update Mesage",tempc.getString(0));
									
									if(tempc.getString(0).equalsIgnoreCase("Success"))
									{
										tvmessage.setBackgroundColor(Color.parseColor("#2ecc71"));
										tvmessage.setText("Row deleted from "+indexInfo.table_name+"table");
									}
									else
									{
									tvmessage.setBackgroundColor(Color.parseColor("#e74c3c"));
									tvmessage.setText("Error:"+tempc.getString(0));
									}
								
								
								}
							}
							
						})
						.setNegativeButton("close", 
  							new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
		                                         						
							}

							
						})
						.create().show();
					     
			   }
		   }
		});
  	
		
		
		
		
		
	}
	
	//the function which displays tuples from database in a table layout
	public void paginatetable(final int number)
		{
		
		 int index =indexInfo.index ;
		 final Cursor c3 = indexInfo.maincursor;
		 indexInfo.numberofpages=(c3.getCount()/10)+1;
		 indexInfo.currentpage=1;
		 c3.moveToFirst();
		 int currentrow=0;
		 
		 //disaply the first 10 tuples of the table selected by user
			 do
			{
				 
			final TableRow tableRow = new TableRow(getApplicationContext());
				//tableRow.setBackgroundDrawable(gd);
			tableRow.setBackgroundColor(Color.BLACK);
			tableRow.setPadding(0, 2, 0, 2);
        	   
			//columsView.setLayoutParams(tableRowParams);
             for(int j=0 ;j<c3.getColumnCount();j++)
             {
            	 LinearLayout cell = new LinearLayout(this);
            	 cell.setBackgroundColor(Color.WHITE);
            	 cell.setLayoutParams(tableRowParams);
               final TextView columsView = new TextView(getApplicationContext());

               
            	   //columsView.setBackgroundDrawable(gd);
             
               columsView.setText(""+c3.getString(j)); 
               columsView.setTextColor(Color.parseColor("#000000"));
               columsView.setPadding(0, 0, 4, 3);
               //columsView.setLayoutParams(tableRowParams);
               cell.addView(columsView);
               tableRow.addView(cell);
               Log.d("table values",""+c3.getString(j));
               
             }
			
             tableRow.setVisibility(View.VISIBLE);
             currentrow=currentrow+1;
             //we create listener for each table row when clicked a alert dialog will be displayed 
             //from where user can update or delete the row 
             tableRow.setOnClickListener(new OnClickListener(){
                 public void onClick(View v) {
               	  
               	  final ArrayList<String> value_string = new ArrayList<String>();
               	  for(int i=0;i<c3.getColumnCount();i++)
               	  {
               		LinearLayout llcolumn = (LinearLayout) tableRow.getChildAt(i);
               	  TextView tc =(TextView)llcolumn.getChildAt(0);
               	  
               	  String cv =tc.getText().toString();
               	  value_string.add(cv);
               	  
               	  }
               	  indexInfo.value_string=value_string;
               	  //the below function will display the alert dialog
               	  updateDeletePopup(0);
                 }
             });
             tableLayout.addView(tableRow);
             
	      
		 }while(c3.moveToNext()&&currentrow<10);
		 
			 indexInfo.index=currentrow;
		 
		 
		 // when user clicks on the previous button update the table with the previous 10 tuples from the database
			previous.setOnClickListener(new View.OnClickListener() 
		    {
		        @Override
		        public void onClick(View v) 
		        {
		        	int tobestartindex=(indexInfo.currentpage-2)*10;
		        	int startindex = (indexInfo.currentpage-1)*10;
		        	int endindex=indexInfo.index;
		        	//if the tbale layout has the first 10 tuples then toast that this is the first page
		            if(indexInfo.currentpage==1)
		            {
		            	Toast.makeText(getApplicationContext(), "This is the first page", Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		            	indexInfo.currentpage=indexInfo.currentpage-1;
		            	c3.moveToPosition(tobestartindex);
		            	
		            	boolean decider=true;
		            	for(int i=1;i<tableLayout.getChildCount();i++)
		            	{	
		            		 TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
		            		 
		            		 
		            		 if(decider)
		            		 {
		            			 tableRow.setVisibility(View.VISIBLE);
		            		 for(int j=0;j<tableRow.getChildCount();j++)
		            		 {
		            			 LinearLayout llcolumn = (LinearLayout) tableRow.getChildAt(j);
		            			 TextView columsView = (TextView) llcolumn.getChildAt(0);
		            			 
		            			 columsView.setText(""+c3.getString(j)); 
		            			 
		            			 
		            		 
		            		 }
		            		 decider=!c3.isLast();
		            		 if(!c3.isLast()){c3.moveToNext();}
		            		 }
		            		 else
		            		 {
		            			 tableRow.setVisibility(View.GONE);
		            		 }
		            		 
		            		
		            	}
		            	
		            	indexInfo.index=tobestartindex;
		            	
		            	Log.d("index =",""+indexInfo.index);
		            	
		            	
		            	
		            }
		        	
		        	
		        	
		        	
		        } 
		    });
		 
		 // when user clicks on the next button update the table with the next 10 tuples from the database
		 next.setOnClickListener(new View.OnClickListener() 
		    {
		        @Override
		        public void onClick(View v) 
		        {
		        	int startindex = indexInfo.currentpage*10;
		        	int endindex = startindex+10;
		        //if there are no tuples to be shown toast that this the last page	
		            if(indexInfo.currentpage>=indexInfo.numberofpages)
		            {
		            	Toast.makeText(getApplicationContext(), "This is the last page", Toast.LENGTH_LONG).show();
		            }
		            else
		            {
		            	indexInfo.currentpage=indexInfo.currentpage+1;
		            	boolean decider=true;
		            	
		            	
		            	for(int i=1;i<tableLayout.getChildCount();i++)
		            	{	
		            		 TableRow tableRow = (TableRow) tableLayout.getChildAt(i);
		            		
		            		 
		            		 if(decider)
		            		 {
		            			 tableRow.setVisibility(View.VISIBLE);
		            		 for(int j=0;j<tableRow.getChildCount();j++)
		            		 {
		            			 LinearLayout llcolumn = (LinearLayout) tableRow.getChildAt(j);
		            			 TextView columsView =(TextView)llcolumn.getChildAt(0);
		            			 
		            			 columsView.setText(""+c3.getString(j)); 
		            			
		            		 }
		            		 decider=!c3.isLast();
		            		 if(!c3.isLast()){c3.moveToNext();}
		            		 }
		            		 else
		            		 {
		            			 tableRow.setVisibility(View.GONE);
		            		 }
		            		 
		            		
		            	}
		            	 
		            		 
		            	
		            }
		        } 
		    });

	 
		 }
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
	}
	 
}

