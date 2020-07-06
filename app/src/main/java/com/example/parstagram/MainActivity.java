package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EditText etDescription;
    private Button btnCaptureImage;
    private ImageView ivPostImage;
    private Button btnSubmit;
    Button btnLogout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogout = findViewById(R.id.btnLogout);
        etDescription = findViewById(R.id.etDescription);
        btnCaptureImage = findViewById(R.id.btnCaptureImage);
        ivPostImage = findViewById(R.id.ivPostImage);
        btnSubmit = findViewById(R.id.btnSubmit);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                if (ParseUser.getCurrentUser() == null) {
                    Log.i(TAG, "Successfully logged out");
                }
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}