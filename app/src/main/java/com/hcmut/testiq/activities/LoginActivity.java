package com.hcmut.testiq.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmut.testiq.R;
import com.hcmut.testiq.http_requests.HttpRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity {

    TextView btnRegister, btnGetPassword;
    Button btnLogin;
    EditText txtUsername, txtPassword;

    CheckBox cbSaveUser;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        addControls();
        addEvents();

    }

    public LoginActivity() {
        super();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("Email", txtUsername.getText().toString());
        editor.putString("Password", txtPassword.getText().toString());
        editor.putBoolean("Save", cbSaveUser.isChecked());
        editor.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
        if(sharedPreferences.getBoolean("Save",false))
        {
            txtUsername.setText(sharedPreferences.getString("Email",""));
            txtPassword.setText(sharedPreferences.getString("Password", ""));
            cbSaveUser.setChecked(true);
        }
    }

    private void addControls() {
        btnRegister = (TextView) findViewById(R.id.btnRegister);
        btnGetPassword = (TextView) findViewById(R.id.btnGetPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        txtUsername = (EditText) findViewById(R.id.txtUsername);
        txtPassword = (EditText) findViewById(R.id.txtPassword);

        cbSaveUser = (CheckBox) findViewById(R.id.cbSaveUser);

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang kiểm tra, vui lòng đợi ...");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    private void addEvents() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
        btnGetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, GetPasswordActivity.class);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginUrl = "https://iqquiz.herokuapp.com/user/login";
                String userName = txtUsername.getText().toString();
                String password = txtPassword.getText().toString();

                Matcher matcher_email = VALID_EMAIL_ADDRESS_REGEX .matcher(userName);

                if (userName.equals("") || userName.equals(null)) {
                    Toast.makeText(LoginActivity.this, "Please input email", Toast.LENGTH_LONG).show();
                } else if(matcher_email.find()==false){
                    Toast.makeText(LoginActivity.this, "Invalid email address!", Toast.LENGTH_SHORT).show();
                } else if (password.equals("") || password.equals(null)) {
                    Toast.makeText(LoginActivity.this, "Please input password", Toast.LENGTH_LONG).show();
                }else if(password.length()<6){
                    Toast.makeText(LoginActivity.this, "Password must be at least 6 characters long!",
                            Toast.LENGTH_SHORT).show();
                } else {

                    HashMap<String, String> dataLogin = new HashMap<>();
                    dataLogin.put("name", userName);
                    dataLogin.put("password", password);

                    new LoginAsyncTask(loginUrl, "POST").execute(new HashMap[]{dataLogin});

                }

            }
        });
    }

    class LoginAsyncTask extends AsyncTask<HashMap<String, String>, Void, String>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();

        }

        private String url;
        private String method;

        public LoginAsyncTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            progressDialog.dismiss();

            if (response.equals("Login success!")){
                // save info to shared preference
                SharedPreferences saveLogin = LoginActivity.this.getSharedPreferences("mydata", MODE_PRIVATE);
                SharedPreferences.Editor editor = saveLogin.edit();
                editor.putBoolean("LOGGEDIN", true);

                Intent intent = new Intent(LoginActivity.this, MainMenuActivity.class);
                startActivity(intent);
            }

            Toast.makeText(LoginActivity.this, response, Toast.LENGTH_SHORT).show();
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
