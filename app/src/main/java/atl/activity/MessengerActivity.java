package atl.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import atl.R;

public class MessengerActivity  extends Activity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myBD = database.getReference("message");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        Button btn = findViewById(R.id.btnSend);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myBD.setValue("Hello WORDL!!!");
            }
        });
    }
}