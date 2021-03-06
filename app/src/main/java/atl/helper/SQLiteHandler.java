package atl.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

	private static final String TAG = SQLiteHandler.class.getSimpleName();

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "android_api";

	// Login table name
	private static final String TABLE_USER = "user";

	// Login Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_EMAIL = "email";
	private static final String KEY_UID = "uid";
	private static final String KEY_ROLE = "role";
	private static final String KEY_SPORT = "sport";
	private static final String KEY_HEIGHT = "height";
	private static final String KEY_CREATED_AT = "created_at";
	private static final String KEY_UPDATED_AT = "updated_at";
	private static final String KEY_BIRTHDAY = "birthday";

	public SQLiteHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_EMAIL + " TEXT UNIQUE," + KEY_UID + " TEXT," + KEY_SPORT + " TEXT,"
				+ KEY_ROLE + " TEXT,"  + KEY_HEIGHT + " TEXT,"
				+ KEY_CREATED_AT + " TEXT" + KEY_UPDATED_AT + "TEXT" + KEY_BIRTHDAY + "TEXT" + ")";
		db.execSQL(CREATE_LOGIN_TABLE);

		Log.d(TAG, "Database tables created");
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

		// Create tables again
		onCreate(db);
	}

	/**
	 * Storing user details in database
	 * */
	public void addUser(String name, String email, String uid, String created_at, String sport, String role, String height) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, name); // Name
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_UID, uid); // Email
		values.put(KEY_CREATED_AT, created_at); // Created At
		values.put(KEY_SPORT, sport); // Sport
		values.put(KEY_ROLE, role); // Role
		values.put(KEY_HEIGHT, height); // Height

		// Inserting Row
		long id = db.insert(TABLE_USER, null, values);
		db.close(); // Closing database connection

		Log.d(TAG, "New user inserted into sqlite: " + id);
	}

	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetails() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0) {

			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("created_at", cursor.getString(4));
			//user.put("sport", cursor.getString(5));
			//user.put("role", cursor.getString(6));
			//user.put("height", cursor.getString(7));

		}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}

	/**
	 * Re crate database Delete all tables and create them again
	 * */
	public void deleteUsers() {
		SQLiteDatabase db = this.getWritableDatabase();
		// Delete All Rows
		db.delete(TABLE_USER, null, null);
		db.close();

		Log.d(TAG, "Deleted all user info from sqlite");
	}

	/**
	 * Updating user details in database
	 * */
	public void updateUser(String name, String uid, String email, String updated_at, String sport, String role, String height ) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
	   	values.put(KEY_UID, uid);
		values.put(KEY_NAME, name); // Name
		values.put(KEY_UPDATED_AT, updated_at); // Updated At
		values.put(KEY_EMAIL, email); // Email
		values.put(KEY_SPORT, sport);
		values.put(KEY_ROLE, role);
		values.put(KEY_HEIGHT, height);


	/*	int updCount = db.update(TABLE_USER, values, "id = ?",
				new String[] { email});*/
		/*long updCount = db.insert(TABLE_USER, null, values);*/
		//db.delete(TABLE_USER, "id="+ email, null);
		long updCount = db.insert(TABLE_USER, null, values);


		db.close(); // Closing database connection
		Log.d(TAG, "User saved new params in sqlite: " + updCount);
	}
	/**
	 * Getting user data from database
	 * */
	public HashMap<String, String> getUserDetail() {
		HashMap<String, String> user = new HashMap<String, String>();
		String selectQuery = "SELECT  * FROM " + TABLE_USER;

		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);
		// Move to first row
		cursor.moveToFirst();
		if(cursor.getCount() > 0) {

			user.put("name", cursor.getString(1));
			user.put("email", cursor.getString(2));
			user.put("uid", cursor.getString(3));
			user.put("sport", cursor.getString(4));
			user.put("role", cursor.getString(5));
			user.put("height", cursor.getString(6));
			user.put("created_at", cursor.getString(7));
			//user.put("birthday", cursor.getString(9));
					}
		cursor.close();
		db.close();
		// return user
		Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

		return user;
	}
}
