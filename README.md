DatabaseManager_For_Android
===========================

Managing android app's SQLite database is tough while developing .You cannot view the tables ,you dont know what is getting inserted into the tables,you cant update the data and see how your application responds to it .

What if you had a database manager like oracle sqldevelopr , mysql work bench for your applications SQLlite database ? This library gives you that .

With this library you can manage the database of your android app from the app itself. 

You can view ,insert ,delete ,update the tables of your apps SQLite database from your app .

Then entire library is written into a single activity class so you just need to add this activity to you android app.

This is how it looks - watch this 1 minute video for demo : http://youtu.be/P5vpaGoBlBY?t=3m40s

Setup :
======

Setup takes hardly 3 minutes Just follow the four simple steps below : 

you can watch 3 mins demonstration video here : http://youtu.be/P5vpaGoBlBY where i setup this library for my app .

	
1) Clone or download this repository and add AndroidDatabaseManager.java file to your application .

   at lines 54 and 73 change yourSqlliteopenhelper to the class name of your custom SQLiteOpenHelper class i.e, the          class which you are using to create tables , insert data to your app SQLlitedatabase.


2) Open helperFunction.txt file copy the entire text and paste the function in you custom SQLiteOpenHelper class .i.e, the    class which you are using to create tables , insert data to your app SQLlitedatabase.

3) Add the following code to your android manifest .Enter the name of the package where AndroidDatabaseManager.java is located.

    <activity android:name="yourpackagename.AndroidDatabaseManager"  />

4) Now you have to start the AndroidDatabaseManager activity . You can do this anyway you wish,Below 3 are the simple ways
   to start the activity choose anyone as per your convenience.
   
       (i)  add onclick listener to a textview .You can use the textview which is already present in your layout or add a 	    textview elemtn to your xml.
    	
	   TextView tv =(TextView)findViewByID(R.id.yourtexviewid);
	    	
	   tv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
	
	      Intent dbmanager = new Intent(getActivity(),AndroidDatabaseManager.class);
				startActivity(dbmanager);
	         }
			});
	(ii) add onclick listener to a Button .You can use the button which is already present in your layout or  add a 			Button element to your xml.
    	
	   Button button =(Button)findViewByID(R.id.yourbuttonid);
	    	
	   button.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
	
	      Intent dbmanager = new Intent(getActivity(),AndroidDatabaseManager.class);
				startActivity(dbmanager);
	         }
			});
	
	(iii) If you are using an action bar add an item  to the action bar and start activity when action bar item is 			       clicked.
	 
Thats it . Now you can manage your application database directly from your app.

When app development is done remove the activity and publish your app .

With this library you can do all these :

	1) view all your tables data in tabluar format
	2) Insert rows to your tables
	3) update rows
	4) delete rows
	5) delete tables
	6) Drop tables
	7) Write your own custom queries and get the results .(Create statments , joins etc)
	8) change data in the tables and see how you application responds

In a nut shell , You can manage your app database easily . 



