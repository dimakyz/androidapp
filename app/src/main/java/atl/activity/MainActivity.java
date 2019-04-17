package atl.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import atl.R;
import atl.activity.fragments.FragmentStyle;
import atl.app.AppConfig;
import atl.app.AppController;
import atl.helper.SQLiteHandler;
import atl.helper.SessionManager;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
	public static final int REQUEST_CODE_GET_PHOTO = 101;
	public static final int REQUEST_VIDEO_CAPTURE = 1;
	private static final String TAG = MainActivity.class.getSimpleName();
	private AppCompatImageView mPhoto;
	//private User mUser;
	private TextView currentDateTime;
	private TextView currentDate;
	private Calendar dateAndTime=Calendar.getInstance();

	private View.OnClickListener mOnPhotoClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View view) {
			openGallery();
		}
	};
	private ProgressDialog pDialog;
	private EditText txtName;
	private TextView txtEmail;
	private Button btnLogout;
	private Button btnSave;
	private EditText txtSport;
	private EditText txtRole;
	private EditText txtHeight;

	private SQLiteHandler db;
	private SessionManager session;



	private void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(intent, REQUEST_CODE_GET_PHOTO);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE_GET_PHOTO
				&& resultCode == Activity.RESULT_OK
				&& data != null) {
			Uri photoUri = data.getData();
			mPhoto.setImageURI(photoUri);
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		//setSupportActionBar(toolbar);

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
			this, drawer,/* toolbar,*/ R.string.navigation_drawer_open, R.string.navigation_drawer_close);
		drawer.addDrawerListener(toggle);
		toggle.syncState();
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
		navigationView.setNavigationItemSelectedListener(this);
		FragmentStyle fstyle = new FragmentStyle();
		View header = navigationView.getHeaderView(0);
		TextView tvName = (TextView)header.findViewById(R.id.name);
		TextView tvEmail = (TextView)header.findViewById(R.id.email);


		txtName = (EditText) findViewById(R.id.name);
		txtSport = (EditText) findViewById(R.id.tvSport);
		txtEmail = (TextView) findViewById(R.id.email);
		txtRole = (EditText)  findViewById(R.id.tvRole);
		txtHeight = (EditText)  findViewById(R.id.tvHeight);
		btnLogout = (Button) findViewById(R.id.btnLogout);
		btnSave = (Button) findViewById(R.id.btnSave);
		mPhoto = findViewById(R.id.ivPhoto);

		// SqLite database handler
		db = new SQLiteHandler(getApplicationContext());

		// Progress dialog
		pDialog = new ProgressDialog(this);
		pDialog.setCancelable(false);

		// session manager
		session = new SessionManager(getApplicationContext());

		if (!session.isLoggedIn()) {
			logoutUser();
		}

		// Fetching user details from SQLite
		HashMap<String, String> user = db.getUserDetail();

		String name = user.get("name");
		final String email = user.get("email");
		String role = user.get("role");
		String sport = user.get("sport");
		String created_at = user.get("created_at");
		String height = user.get("height");
		String salt = user.get("salt");
		final String updated_at = user.get("updated_at");
		String birthday = user.get("birthday");
		final String uid = user.get("uid");


		// Displaying the user details on the screen
		txtName.setText(name);
		txtEmail.setText(email);
		txtHeight.setText(height);
		txtRole.setText(role);
		txtSport.setText(sport);
		tvName.setText(name);
		tvEmail.setText(email);
		//currentDateTime.setText(created_at);

		// Logout button click event
		btnLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				logoutUser();
			}
		});
		// Save Button Click event
		btnSave.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String name = txtName.getText().toString().trim();
				String sport = txtSport.getText().toString().trim();
				String role = txtRole.getText().toString().trim();
				String height = txtHeight.getText().toString().trim();
				if (!name.isEmpty()) {
					saveUser(name, uid, email, updated_at, sport, role, height);
				} else {
					Toast.makeText(getApplicationContext(),
							"Заполните пустые поля!", Toast.LENGTH_LONG)
							.show();
				}
			}
		});

		Bundle bundle = getIntent().getExtras();
		currentDateTime=(EditText)findViewById(R.id.currentDateTime);
		currentDate=(TextView)findViewById(R.id.currentDate);
		setInitialDate();
		setInitialDateTime();
		mPhoto.setOnClickListener(mOnPhotoClickListener);


	}

	// установка начальной даты
	private void setInitialDateTime() {

		currentDate.setText(DateUtils.formatDateTime(this,
				dateAndTime.getTimeInMillis(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
		));
	}

	// установка начальной даты
	private void setInitialDate() {

		currentDateTime.setText(DateUtils.formatDateTime(this,
				dateAndTime.getTimeInMillis(),
				DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_YEAR
		));
	}
	// отображаем диалоговое окно для выбора даты
	public void setDate(View v) {
		new DatePickerDialog(MainActivity.this, d,
				dateAndTime.get(Calendar.YEAR),
				dateAndTime.get(Calendar.MONTH),
				dateAndTime.get(Calendar.DAY_OF_MONTH))
				.show();
	}
	// установка обработчика выбора даты
	DatePickerDialog.OnDateSetListener d=new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			setInitialDateTime();
		}
	};
	public void setDateT(View v) {
		new DatePickerDialog(MainActivity.this, t,
				dateAndTime.get(Calendar.YEAR),
				dateAndTime.get(Calendar.MONTH),
				dateAndTime.get(Calendar.DAY_OF_MONTH))
				.show();
	}
	DatePickerDialog.OnDateSetListener t=new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
			dateAndTime.set(Calendar.YEAR, year);
			dateAndTime.set(Calendar.MONTH, monthOfYear);
			dateAndTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
			setInitialDate();
		}
	};
/*	private void dispatchTakeVideoIntent() {
		Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
			startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
			Uri videoUri = intent.getData();
			mVideoView.setVideoURI(videoUri);
		}
	}  */

	private void saveUser(final String name, final String uid, final String email, final String updated_at, final String sport, final String role, final String height) {
		// Tag used to cancel the request
		String tag_string_req = "req_save";

		pDialog.setMessage("Сохранение ...");
		showDialog();

		StringRequest strReq = new StringRequest(Request.Method.POST,
				AppConfig.URL_SAVE, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {
				Log.d(TAG, "UPDATE Response: " + response.toString());
				hideDialog();

				try {
					JSONObject jObj = new JSONObject(response);
					boolean error = jObj.getBoolean("error");
					if (!error) {
						// User successfully saved in MySQL
						// Now store the user in sqlite
						String uid = jObj.getString("uid");

						JSONObject user = jObj.getJSONObject("user");
						/*String name = ((EditText)findViewById(R.id.name)).getText().toString();*/
						String name = user.getString("name");
						String updated_at = user
								.getString("updated_at");
						String email = user.getString("email");
						String sport = user.getString("sport");
						String role = user.getString("role");
						String height = user.getString("height");
						/*String sport = ((EditText)findViewById(R.id.tvSport)).getText().toString();
						String role = ((EditText)findViewById(R.id.tvRole)).getText().toString();
						String height = ((EditText)findViewById(R.id.tvHeight)).getText().toString();*/

						// updating users table
						db.updateUser(name,uid, email, updated_at, sport, role, height);

						Toast.makeText(getApplicationContext(), "Успешно сохранено", Toast.LENGTH_LONG).show();

						//reload main activity
						Intent intent = new Intent(
								MainActivity.this,
								MainActivity.class);
						startActivity(intent);
						} else {

						// Error occurred in save. Get the error
						// message
						String errorMsg = jObj.getString("error_msg");
						Toast.makeText(getApplicationContext(),
								errorMsg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
		}, new Response.ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError error) {
				Log.e(TAG, "Save Error: " + error.getMessage());
				Toast.makeText(getApplicationContext(),
						error.getMessage(), Toast.LENGTH_LONG).show();
				hideDialog();
			}
		}) {

			@Override
			protected Map<String, String> getParams() {
				// Posting params to Save url
				Map<String, String> params = new HashMap<String, String>();
				params.put("name", name);
				params.put("email", email);
				params.put("sport", sport);
				params.put("role", role);
				params.put("height", height);

				return params;
			}

		};

		// Adding request to request queue
		AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
	}
	/**
	 * Logging out the user. Will set isLoggedIn flag to false in shared
	 * preferences Clears the user data from sqlite users table
	 * */
	private void logoutUser() {
		session.setLogin(false);

		db.deleteUsers();

		// Launching the login activity
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	private void showDialog() {
		if (!pDialog.isShowing())
			pDialog.show();
	}

	private void hideDialog() {
		if (pDialog.isShowing())
			pDialog.dismiss();
	}
	@Override
	public void onBackPressed() {
		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	@SuppressWarnings("StatementWithEmptyBody")
	@Override
	public boolean onNavigationItemSelected(MenuItem item) {
		// Handle navigation view item clicks here.
		int id = item.getItemId();
		FragmentTransaction ftrans = getFragmentManager().beginTransaction();
		if (id == R.id.nav_profile) {
			// Handle the camera action
		} else if (id == R.id.nav_style) {
			Intent intent = new Intent(MainActivity.this, CompetitiveStyleActivity.class);
			startActivity(intent);
			finish();
		} else if (id == R.id.nav_goal) {
			Intent intent = new Intent(MainActivity.this, GoalActivity.class);
			startActivity(intent);
			finish();
		} else if (id == R.id.nav_media) {

		} else if (id == R.id.nav_anamnesis) {

		} else if (id == R.id.nav_additionally) {
			Intent intent = new Intent(MainActivity.this, AdditionallyActivity.class);
			startActivity(intent);
			finish();
		} else if (id == R.id.nav_training) {

		} else if (id == R.id.nav_information) {

		}

		DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}

