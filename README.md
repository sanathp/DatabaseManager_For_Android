DatabaseManager_For_Android
===========================

Managing android app's sqllite database is tough while developing .You cant see the tables ,you dont know what is getting inserted into the tables,you cant update the data and see how your application responds to it .
What if you had a database manager like oracle sqldevelopr , mysql work bench for your application database ? This library gives you that .

With this library you can manage the database of your android app from the app itself. 
You can view ,insert ,delete ,update the tables of your apps sqllite database from your app .

Then entire library is written into a single activity class so you just need to add a single java file to your app .


Just four simple steps :

	for live demo watch this 1 minute youtube video :
	
1) Clone or download this repository and add AndroidDatabaseManager.java file to your application .

   at lines 54 and 73 change yourSqlliteopenhelper to the class name of your custom SQLiteOpenHelper class

2) Open helperFunction.txt file copy the entire text and paste the function in you custom SQLiteOpenHelper class.
   Using this functions the AndroidDatabaseManager activity get data from your database.

3) add the following code to your android manifest .Enter the name of the package where AndroidDatabaseManager.java is located.

    <activity android:name="yourpackagename.AndroidDatabaseManager"
            android:label="@string/app_name" />

4) some where in your app add a button or something to start the AndroidDatabaseManager activty .
    you can add onclick listener as shown below to a textview element or a button some where in your app .
    if you are using an action bar you can add extra item to launch this activity. or an extra button any where in your 	   layout.
  
    object.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

      Intent dbmanager = new Intent(getActivity(),AndroidDatabaseManager.class);
			startActivity(dbmanager);
         }
		});
		
		
With this library you can do all these :
1) view all your tables data in tabluar format
2) Insert rows to your tables
3) update rows
4) delete rows
5) delete tables
6) Drop tables
7) Write your own custom queries and get the results .(Create statments , joins etc)
8) change data in the tables and see how you application responds

In a nut shell , You can manage your database easily which was tough  .



