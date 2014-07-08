package com.pskapps.dialonce;

import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class DataViewer extends Activity implements OnItemClickListener {
	
	static class indexInfo
    {
    	public static int index = 10;
    	public static int numberofpages = 0;
    	public static int currentpage = 0;
    	public static String table_name="";
    	public static Cursor maincursor;
    	public static ArrayList<String> value_string;
    }
	
	DatabaseManager dbm;
	TableLayout tableLayout;
	indexInfo info = new indexInfo();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dataviewer);
		//for temporary table SELECT name FROM sqlite_temp_master WHERE type='table';
		final Spinner select_table = (Spinner) findViewById(R.id.tables_dropdown);
		

		ArrayList<Cursor> alc ;
    	tableLayout = (TableLayout)findViewById(R.id.table);
		dbm = new DatabaseManager(getApplicationContext());
		
		final TextView tv =(TextView)findViewById(R.id.table_count_result);
		final TextView tvmessage =(TextView)findViewById(R.id.message_text);
		tvmessage.setText("Jaffa");
		String Query = "SELECT name _id FROM sqlite_master WHERE type ='table'";
		
		final GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(5);
        gd.setStroke(1, 0xFF000000);
	
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
	            	
	            	Log.d("selected table name is",""+c.getString(0));

	                tableLayout.removeAllViews();
	            	final Spinner spinnertable =(Spinner)findViewById(R.id.table_action_spinner);
	            	ArrayList<String> spinnertablevalues = new ArrayList<String>();
	            	spinnertablevalues.add("Click here to change this table");
	                spinnertablevalues.add("Add row");
	                spinnertablevalues.add("Delete");
	                spinnertablevalues.add("Drop");
	                ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnertablevalues);
	                spinnertable.setAdapter(spinnerArrayAdapter);
	                //spinnertable.setTextColor(Color.parseColor("#000000"));
	            	String Query2 ="select * from "+c.getString(0);
	            	Log.d("",""+Query2);
	            	
	            	ArrayList<Cursor> alc2=dbm.getData(Query2);
	            	final Cursor c2=alc2.get(0);
	            	indexInfo.maincursor=c2;
	            	
	            	if(c2!=null)
	            	{
	            	int counts = c2.getCount();
	            	
	            	Log.d("counts",""+counts);
	            	tv.setText(""+counts);
	                
	                
	            	
	            	
	            	indexInfo.table_name=c.getString(0);
	            	
	            	spinnertable.setOnItemSelectedListener(new OnItemSelectedListener() {

	                    public void onItemSelected(AdapterView<?> arg0, View view, int position, long id) {
	                    	if(spinnertable.getSelectedItem().toString().equals("Drop"))
	                    	{
	                    		runOnUiThread(new Runnable() {
	                    			   @Override
	                    			   public void run() {
	                    				if(!isFinishing()){
	                    					
		                    						        new AlertDialog.Builder(DataViewer.this)
		                    							.setTitle("Are you sure")
		                    							.setMessage("are you sure asshole ?")
		                    							.setPositiveButton("yes", 
		                          							new DialogInterface.OnClickListener() {
		                  								public void onClick(DialogInterface dialog, int which) {
		                  			                        
									                    		String Query6 = "Drop table "+indexInfo.table_name;
									                    		ArrayList<Cursor> aldropt=dbm.getData(Query6);
																	Cursor tempc=aldropt.get(1);
																	tempc.moveToLast();
																	Log.d("Drop table Mesage",tempc.getString(0));
																	tvmessage.setText(tempc.getString(0));
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
	                    	if(spinnertable.getSelectedItem().toString().equals("Delete"))
	                    	{
	                    		runOnUiThread(new Runnable() {
	                    			   @Override
	                    			   public void run() {
	                    				if(!isFinishing()){
	                    					
		                    						        new AlertDialog.Builder(DataViewer.this)
		                    							.setTitle("Are you sure")
		                    							.setMessage("are you sure asshole ?")
		                    							.setPositiveButton("yes", 
		                          							new DialogInterface.OnClickListener() {
		                  								public void onClick(DialogInterface dialog, int which) {
		                  			                        
																	                    		
									                    		String Query7 = "Delete  from "+indexInfo.table_name;
									                    		
									                    		ArrayList<Cursor> aldeletet=dbm.getData(Query7);
																	Cursor tempc=aldeletet.get(1);
																	tempc.moveToLast();
																	Log.d("Drop table Mesage",tempc.getString(0));
																	tvmessage.setText(tempc.getString(0));}})
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
	                    	if(spinnertable.getSelectedItem().toString().equals("Add row"))
	                    	{
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
	                  								public void onClick(DialogInterface dialog, int which) {
	                  			                        
	                  									indexInfo.index = 10;
	                  									//tableLayout.removeAllViews();
	                  									select_table.setSelection(0);
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
	                  									
	                  									Log.d("Inset Query",Query4);
	                  									ArrayList<Cursor> altc=dbm.getData(Query4);
	                  									Cursor tempc=altc.get(1);
	                  									tempc.moveToLast();
	                  									Log.d("Update Mesage",tempc.getString(0));
	                  									tvmessage.setText(tempc.getString(0));
	                  									
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
	                });
	            	
	            	
	                TableRow tableheader = new TableRow(getApplicationContext());
	                
	                for(int k=0;k<c2.getColumnCount();k++)
	                {
	                
	                final TextView tableheadercolums = new TextView(getApplicationContext());
	                tableheadercolums.setBackgroundDrawable(gd);
	                tableheadercolums.setText(""+c2.getColumnName(k)); 
	                tableheadercolums.setTextColor(Color.parseColor("#000000"));
	                tableheader.addView(tableheadercolums);
	                
	                
	                }
	                tableLayout.addView(tableheader);
	                c2.moveToFirst();
	               
	                paginatetable(c2.getCount());
	            	}
	            	else{
	            		tableLayout.removeAllViews();
		                
	            		tv.setText(""+0);
	            	}
	            	
	            	
	            }

	            

				@Override
	            public void onNothingSelected(AdapterView<?> arg0) {
	           

	            }
	        });
		 
	
	}
	
	public void updateDeletePopup(int row)
	{
		String text="";
		
		Cursor c2=indexInfo.maincursor;
		final TextView tvmessage = (TextView)findViewById(R.id.message_text);
  	  ArrayList<String> spinnerArray = new ArrayList<String>();
  	    spinnerArray.add("Dont Change");
  	    spinnerArray.add("Update");
  	    spinnerArray.add("Delete");

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
      final RelativeLayout lp = new RelativeLayout(DataViewer.this);
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
          et.setBackgroundColor(Color.parseColor("#FFFFFF"));
          et.setTextColor(Color.parseColor("#000000"));
  	    et.setId(k);
  	    Log.d("text View Value",""+tv.getText().toString());
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
  	    lp.addView(ll, rll);
  	    
  	    }
  	    
  	    LinearLayout lcrud = new LinearLayout(DataViewer.this);
      	
  	    LinearLayout.LayoutParams paramcrudtext = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
  	    
  	    TextView crud_text = new TextView(getApplicationContext());
  	    crud_text.setText("this row");
  	    
  	    lcrud.addView(crud_text, paramcrudtext);

          final Spinner crud_dropdown = new Spinner(getApplicationContext());
          ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
          crud_dropdown.setAdapter(spinnerArrayAdapter);
          lcrud.addView(crud_dropdown,paramcrudtext);
          RelativeLayout.LayoutParams rlcrudparam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    	  	rlcrudparam.addRule(RelativeLayout.BELOW,lastrid);
  	    
  	    lp.addView(lcrud, rlcrudparam);
  	    
          
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
							public void onClick(DialogInterface dialog, int which) {
								
								//get spinner value
								String spinner_value = crud_dropdown.getSelectedItem().toString();

								if(spinner_value.equalsIgnoreCase("Update"))
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
								tvmessage.setText(tempc.getString(0));
								}
								
								if(spinner_value.equalsIgnoreCase("Delete"))
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
									tvmessage.setText(tempc.getString(0));
								
								
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
	
	 public void paginatetable(final int number)
		{
		
		 int index =indexInfo.index ;
		 final Cursor c3 = indexInfo.maincursor;
		 indexInfo.numberofpages=(c3.getCount()/10)+1;
		 indexInfo.currentpage=1;
		 final GradientDrawable gd = new GradientDrawable();
	        gd.setCornerRadius(5);
	        gd.setStroke(1, 0xFF000000);
		 Button prev = (Button) findViewById(R.id.previous);
		 Button next = (Button) findViewById(R.id.next);
		 
		 c3.moveToFirst();
		 int currentrow=0;
			 do
			{
				 
			final TableRow tableRow = new TableRow(getApplicationContext());
			currentrow=currentrow+1;
             
         	
             for(int j=0 ;j<c3.getColumnCount();j++)
             {
               final TextView columsView = new TextView(getApplicationContext());
               columsView.setBackgroundDrawable(gd);
               columsView.setText(""+c3.getString(j)); 
               columsView.setTextColor(Color.parseColor("#000000"));
               tableRow.addView(columsView);
               Log.d("table values",""+c3.getString(j));
               
             }
             tableRow.setVisibility(View.VISIBLE);
             tableRow.setOnClickListener(new OnClickListener(){
                 public void onClick(View v) {
               	  
               	  final ArrayList<String> value_string = new ArrayList<String>();
               	  for(int i=0;i<c3.getColumnCount();i++)
               	  {
               	  TextView tc =(TextView)tableRow.getChildAt(i);
               	  
               	  String cv =tc.getText().toString();
               	  value_string.add(cv);
               	  
               	  }
               	  indexInfo.value_string=value_string;
               	  updateDeletePopup(0);
                 }
             });
             tableLayout.addView(tableRow);
	      
		 }while(c3.moveToNext()&&currentrow<10);
		 
			 indexInfo.index=currentrow;
		 
		 prev.setOnClickListener(new View.OnClickListener() 
		    {
		        @Override
		        public void onClick(View v) 
		        {
		        	int tobestartindex=(indexInfo.currentpage-2)*10;
		        	int startindex = (indexInfo.currentpage-1)*10;
		        	int endindex=indexInfo.index;
		        	
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
		            			 TextView columsView = (TextView) tableRow.getChildAt(j);
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
		 
		 next.setOnClickListener(new View.OnClickListener() 
		    {
		        @Override
		        public void onClick(View v) 
		        {
		        	int startindex = indexInfo.currentpage*10;
		        	int endindex = startindex+10;
		        	
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
		            			 TextView columsView = (TextView) tableRow.getChildAt(j);
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
