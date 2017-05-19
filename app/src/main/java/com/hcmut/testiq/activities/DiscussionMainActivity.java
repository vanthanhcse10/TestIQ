package com.hcmut.testiq.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import com.hcmut.adapter.DeAdapter;
import com.hcmut.testiq.R;
import com.hcmut.testiq.http_requests.HttpRequest;
import com.hcmut.testiq.models.Package;
import com.hcmut.testiq.utils.JSONParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscussionMainActivity extends AppCompatActivity {

    GridView gvDe;
    ArrayList<String> arrDe;
    DeAdapter deAdapter;

    Button btnCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discussion_main);

        addControls();
        addEvents();
    }
    private void addControls() {
        btnCancel = (Button) findViewById(R.id.btnCancel);
        gvDe = (GridView)findViewById(R.id.gvDe);
        arrDe = new ArrayList<>();

        String url = "https://iqquiz.herokuapp.com/package/all";
        new LoadTestTask(url, "GET").execute(new HashMap[]{});

        deAdapter = new DeAdapter(DiscussionMainActivity.this, R.layout.item_de_1, arrDe);
        gvDe.setAdapter(deAdapter);

    }

    private void addEvents() {
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gvDe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(DiscussionMainActivity.this,DiscussionDetailActivity.class);
                intent.putExtra("ID", arrDe.get(position));
                startActivity(intent);
            }
        });
    }
    class LoadTestTask extends AsyncTask<HashMap<String, String>, Void, ArrayList<Package>> {
        private String url;
        private String method;

        public LoadTestTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(ArrayList<Package> packages) {
            super.onPostExecute(packages);
            for(Package pack: packages){
                arrDe.add(pack.getId());
            }
            deAdapter.notifyDataSetChanged();
        }

        @Override
        protected ArrayList<Package> doInBackground(HashMap<String, String>... params) {
            ArrayList<Package> packageArrayList = null;

            HashMap<String, String> paramList = null;
            if (params.length > 0){
                paramList = params[0];
            }
            try {
                String data = new HttpRequest().getDataFromAPI(this.url,this.method,paramList);
                packageArrayList = JSONParser.getPackageList(data);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return packageArrayList;
        }
    }
}
