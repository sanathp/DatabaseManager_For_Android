DatabaseManager_For_Android
===========================

Managing Android apps' SQLite databases is tough while developing. You cannot view the tables, you don't know what is getting inserted into the tables and you can't update the data and see how your application responds to it.

What if you had a database manager like oracle sqldevelopr, mysql work bench for your application's SQLite database? This library gives you that.

With this library you can manage the database of your android app from the app itself. 

You can view, insert, delete, update the tables of your app's SQLite database from your app.

Then entire library is written into a single Activity class so you just need to add this Activity to your Android app.

This is how it looks - watch this 1 minute video for demo: http://youtu.be/P5vpaGoBlBY?t=3m40s

Setup:
======

Setup takes hardly 3 minutes. Just follow the four simple steps below: 

You can watch 3 mins demonstration video here: http://youtu.be/P5vpaGoBlBY where I setup this library for my app.
	
* Clone or download this repository and add `AndroidDatabaseManager.java` file to your application.
* Add your package name to the top of `AndroidDtabaseManager.java`.
* At lines 54 and 73 change `yourSqlliteopenhelper` to the class name of your custom `SQLiteOpenHelper` subclass (i.e, the          class which you are using to create tables, insert data to your app SQLite database). After changing the name, some errors will still exist! Don't worry, they will be gone after the next step.
* Open `helperFunction.txt`, copy the entire text and paste the function in you custom `SQLiteOpenHelper` subclass (same as above).
* If there is a compiler error at `MatrixCursor`, add `import android.database.MatrixCursor;` statement at the start of your class file
* Add the following code to your android manifest:

        <activity android:name=".AndroidDatabaseManager" 
    	      android:theme="@style/Theme.AppCompat.Light"/>

* Now you have to start the `AndroidDatabaseManager` activity. You can do this any way you wish. Below are three simple ways to start the activity, choose any one as per your convenience.
 1. add `OnClickListener` to a `TextView`. You can use a `TextView` which is already present in your layout or add a new `TextView` element to your xml.

        TextView tv = (TextView)findViewById(R.id.yourtextviewid);
        tv.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent dbmanager = new Intent(getActivity(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });
			
 2. Add `OnClickListener` to a `Button`. You can use a `Button` which is already present in your layout or add a 			new `Button` element to your xml.

        Button button =(Button)findViewByID(R.id.yourbuttonid);
        button.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                Intent dbmanager = new Intent(getActivity(),AndroidDatabaseManager.class);
                startActivity(dbmanager);
            }
        });

 3. If you are using an action bar add an item to the action bar and start the activity when the action bar item is clicked.

That's it. Now you can manage your application database directly from your app.

When app development is done remove the activity and publish your app.

With this library you can do all these:

1. View all your tables data in tabular format
2. Insert rows to your tables
3. Update rows
4. Delete rows
5. Delete tables
6. Drop tables
7. Write your own custom queries and view the results. (Create statements, joins, etc)
8. Change data in the tables and see how you application responds

In a nut shell, you can manage your app database easily.
