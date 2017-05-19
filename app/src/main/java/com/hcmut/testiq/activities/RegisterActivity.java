package com.hcmut.testiq.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmut.testiq.R;
import com.hcmut.testiq.http_requests.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    TextView btnLogin;
    Button btnRegister;
    EditText txtUsername, txtPassword;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        addControls();
        addEvents();
    }

    private void addControls() {
        btnLogin = (TextView) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtUsername = (EditText) findViewById(R.id.txtUsename);
        txtPassword = (EditText) findViewById(R.id.txtPassword);
    }

    private void addEvents() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String registerUrl = "https://iqquiz.herokuapp.com/user/register";
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                Matcher matcher_email = VALID_EMAIL_ADDRESS_REGEX .matcher(userName);

                if (userName.equals("") || userName.equals(null)) {
                    Toast.makeText(RegisterActivity.this, "Please input email", Toast.LENGTH_LONG).show();
                } else if(matcher_email.find()==false){
                    Toast.makeText(RegisterActivity.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password.equals(null)) {
                    Toast.makeText(RegisterActivity.this, "Please input password", Toast.LENGTH_LONG).show();
                }else if(password.length()<6){
                    Toast.makeText(RegisterActivity.this, "Password must be at least 6 characters long!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String> dataLogin = new HashMap<>();
                    dataLogin.put("name", userName);
                    dataLogin.put("password", password);

                    new RegisterActivity.RegisterAsyncTask(registerUrl, "POST").execute(new HashMap[]{dataLogin});

                }
            }
        });

    }

    class RegisterAsyncTask extends AsyncTask<HashMap<String, String>, Void, String> {
        private String url;
        private String method;

        public RegisterAsyncTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            if (response.equals("Register success!")){
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
            Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            String res="";
            HashMap<String, String> paramList = null;
            if (params.length > 0){
                paramList = params[0];
            }
            try {
                res = new HttpRequest().getDataFromAPI(this.url,this.method,paramList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return res;
        }
    }
}
