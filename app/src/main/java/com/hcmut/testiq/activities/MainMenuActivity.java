package com.hcmut.testiq.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.hcmut.testiq.R;

public class MainMenuActivity extends AppCompatActivity {

    Button btnStartTest, btnDiscussion, btnResults, btnLogOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main_menu);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnStartTest = (Button) findViewById(R.id.btnStartTest);
        btnDiscussion = (Button) findViewById(R.id.btnDiscussion);
        btnResults = (Button) findViewById(R.id.btnResults);
        btnLogOut = (Button) findViewById(R.id.btnLogOut);
    }

    private void addEvents() {
        btnStartTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, StartTestActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        btnDiscussion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, DiscussionMainActivity.class);
                startActivity(intent);
            }
        });
    }
}
