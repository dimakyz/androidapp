package atl.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import atl.R;
import atl.helper.SQLiteHandler;
import atl.helper.SessionManager;

public class AdditionallyActivity extends AppCompatActivity {

    private static final String TAG = CompetitiveStyleActivity.class.getSimpleName();

    private ProgressDialog pDialog;

    private TextView Date;
    private TextView txtAdditionally;
    private TextView txtDate;

    private Button btnBack;
    private Button btnSave;

    private SQLiteHandler db;
    private SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additionally);

        txtAdditionally = (EditText)  findViewById(R.id.tvAdditionally);
        txtDate = (EditText)  findViewById(R.id.additionallyDate);
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
    }
    private void backToMain() {

        // Back to the main activity
        Intent intent = new Intent(AdditionallyActivity.this, MainActivity.class);
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
