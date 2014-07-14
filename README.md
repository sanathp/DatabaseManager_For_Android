DatabaseManager_For_Android
===========================

With this library you can manage the database of your android app from the app itself. 
You can view ,insert ,delete ,update the tables your sqllite database from you app .

Then entire library is written into a single activity class so you just need to add a single java file to your app .

to know how it works you can watch this 1 minute youtube video :
where i show how it works for my app


Just four simple steps :

1) Clone or download this repository and add AndroidDatabaseManager.java file your pacakge .

2) Open helperFunction.txt file copy the entire text and paste the function in you custome SQLiteOpenHelper class.
   This function should be present in the database class.

3) add the following code to your android manifest .Enter the name of the packege where you kept hte main java file

    <activity android:name="yourpackagename.AndroidDatabaseManager"
            android:label="@string/app_name" />

4) some where in your app add a button or something to start the AndroidDatabaseManager activty .
    you can add onclick listener to a textview element some where in your app
  
    object.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {

      Intent dbmanager = new Intent(this,AndroidDatabaseManager.class);
			startActivity(dbmanager);
         }
		});
