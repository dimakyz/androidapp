package atl.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import atl.R;
import atl.helper.SQLiteHandler;
import atl.helper.SessionManager;

public class CompetitiveStyleActivity extends Activity {

    private static final String TAG = CompetitiveStyleActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private TextView Date;
    private TextView txtName;
    private TextView txtEmail;

    private EditText txtSeason;
    private EditText txtQualities;
    private EditText txtZone;

    private Button btnBack;
    private Button btnSave;

    private SQLiteHandler db;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_style);

        txtName = (TextView) findViewById(R.id.name);
        txtSeason = (EditText) findViewById(R.id.tvSeason);
        txtEmail = (TextView) findViewById(R.id.email);
        txtQualities = (EditText)  findViewById(R.id.tvQualities);
        txtZone = (EditText)  findViewById(R.id.tvZone);
        btnBack = (Button) findViewById(R.id.btnBack);
        btnSave = (Button) findViewById(R.id.btnSave);


        // SqLite database handler
        db = new SQLiteHandler(getApplicationContext());

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        // session manager
        session = new SessionManager(getApplicationContext());

        if (!session.isLoggedIn()) {
            backToMain();
        }

        // Fetching user details from SQLite
        HashMap<String, String> user = db.getUserDetail();

        String name = user.get("name");
        final String email = user.get("email");
        String season = user.get("season");
        String zone = user.get("zone");
        String qualities = user.get("qualities");

        final String uid = user.get("uid");


        // Displaying the user details on the screen
        txtName.setText(name);
        txtEmail.setText(email);
        txtSeason.setText(season);
        txtZone.setText(zone);
        txtQualities.setText(qualities);

        //currentDateTime.setText(created_at);

        // Logout button click event
        btnBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                backToMain();
            }
        });
        // Save Button Click event
       /*
       btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                //String name = txtName.getText().toString().trim();
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
        });*/


    }
    private void backToMain() {

        // Back to the main activity
        Intent intent = new Intent(CompetitiveStyleActivity.this, MainActivity.class);
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
}



