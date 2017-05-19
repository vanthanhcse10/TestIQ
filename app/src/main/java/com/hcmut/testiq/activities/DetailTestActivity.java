package com.hcmut.testiq.activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmut.testiq.R;
import com.hcmut.testiq.http_requests.HttpRequest;
import com.hcmut.testiq.models.Package;
import com.hcmut.testiq.models.Question;
import com.hcmut.testiq.utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class DetailTestActivity extends FragmentActivity implements View.OnClickListener{
    static final int NUM_ITEMS = 10;
    ImageButton btnPrevious, btnNext;
    TextView txtIndexQuestion;
    Button btnCancel, btnSubmitResult;

    ViewPager viewPager;
    QuestionFragmentPagerAdapter questionFragmentPagerAdapter;
    static ArrayList<Question> questions;

    ProgressDialog progressDialog;

    static String[] results;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail_test);

        addControls();
        addEvents();
    }

    private void addControls() {
        Intent intent = getIntent();
        String indexDe = intent.getStringExtra("ID");
        //Toast.makeText(this, ""+ (indexDe +1), Toast.LENGTH_SHORT).show();

        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext = (ImageButton) findViewById(R.id.btnNext);
        txtIndexQuestion = (TextView) findViewById(R.id.txtIndexQuestion);
        viewPager = (ViewPager) findViewById(R.id.pager);
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnSubmitResult = (Button) findViewById(R.id.btnSubmitResult);

        viewPager.beginFakeDrag();

        progressDialog = new ProgressDialog(DetailTestActivity.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải câu hỏi, vui lòng chờ ...");
        progressDialog.setCanceledOnTouchOutside(false);

        questions = new ArrayList<>();
        results = new String[10];
        Arrays.fill(results, "");

        String url = "https://iqquiz.herokuapp.com/package/questions?id=" + indexDe;
        new LoadQuestionTask(url, "GET").execute(new HashMap[]{});

    }

    private void addEvents() {
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSubmitResult.setOnClickListener(this);
    }

    public static class QuestionFragmentPagerAdapter extends FragmentPagerAdapter{
        ArrayList<Question> questions;
        public QuestionFragmentPagerAdapter(FragmentManager fm, ArrayList<Question> questions)
        {
            super(fm);
            this.questions = questions;
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment(questions);
            return SwipeFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            return NUM_ITEMS;
        }
    }

    public static class SwipeFragment extends Fragment {
        static ArrayList<Question> questions;

        public SwipeFragment(ArrayList<Question> questions) {
            this.questions = questions;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imgQuestion = (ImageView) swipeView.findViewById(R.id.imgQuestion);
            ImageView imgResultA = (ImageView) swipeView.findViewById(R.id.imgResultA);
            ImageView imgResultB = (ImageView) swipeView.findViewById(R.id.imgResultB);
            ImageView imgResultC = (ImageView) swipeView.findViewById(R.id.imgResultC);
            ImageView imgResultD = (ImageView) swipeView.findViewById(R.id.imgResultD);

            //RadioGroup qualityRadioGroup = (RadioGroup) swipeView.findViewById(R.id.qualityRadioGroup);
            //int idChecked = qualityRadioGroup.getCheckedRadioButtonId();
            RadioButton radioA = (RadioButton) swipeView.findViewById(R.id.radioA);
            RadioButton radioB = (RadioButton) swipeView.findViewById(R.id.radioB);
            RadioButton radioC = (RadioButton) swipeView.findViewById(R.id.radioC);
            RadioButton radioD = (RadioButton) swipeView.findViewById(R.id.radioD);

            Bundle args = getArguments();
            final int position = args.getInt("position");

            radioA.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity().getApplication(), "A" + position, Toast.LENGTH_SHORT).show();
                    results[position] = "a";
                }
            });

            radioB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity().getApplication(), "B", Toast.LENGTH_SHORT).show();
                    results[position] = "b";
                }
            });
            radioC.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity().getApplication(), "C", Toast.LENGTH_SHORT).show();
                    results[position] = "c";
                }
            });
            radioD.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(getActivity().getApplication(), "D", Toast.LENGTH_SHORT).show();
                    results[position] = "d";
                }
            });

            Picasso.with(getActivity().getApplication()).load(questions.get(position).getContent()).into(imgQuestion);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsA()).into(imgResultA);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsB()).into(imgResultB);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsC()).into(imgResultC);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsD()).into(imgResultD);


            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            SwipeFragment swipeFragment = new SwipeFragment(questions);
            Bundle args = new Bundle();
            args.putInt("position", position);
            swipeFragment.setArguments(args);
            return swipeFragment;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPrevious:
                viewPager.setCurrentItem(viewPager.getCurrentItem()-1);
                txtIndexQuestion.setText((viewPager.getCurrentItem()+1)+"/10");
                break;
            case R.id.btnNext:
                viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
                txtIndexQuestion.setText((viewPager.getCurrentItem()+1) +"/10");
                break;
            case R.id.btnCancel:
                finish();
                break;
            case R.id.btnSubmitResult:
                processSubmitResult();
                break;
            default:
                break;
        }

    }

    private void processSubmitResult() {
        int answer = 0;
        for(int i = 0; i < 10; i++)
        {
            if(questions.get(i).getAns().equals(results[i]))
                answer++;
        }
        String typeResult = "";
        switch (answer) {
            case 10:
                typeResult = "Excellent";
                break;
            case 9:
            case 8:
                typeResult = "Very Good";
                break;
            case 7:
            case 6:
                typeResult = "Average";
                break;
            case 5:
            case 4:
                typeResult = "Below Average";
                break;
            default:
                typeResult = "Failing";
                break;
        }

        final Dialog alertDialog = new Dialog(DetailTestActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View convertView = (View) inflater.inflate(R.layout.dialog_result, null);
        alertDialog.setContentView(convertView);
        TextView textTypeResult = (TextView) alertDialog.findViewById(R.id.txtTypeResult);
        TextView txtResult = (TextView) alertDialog.findViewById(R.id.txtResult);
        alertDialog.setTitle("Result");
        alertDialog.findViewById(R.id.btnClose).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                finish();
            }
        });
        textTypeResult.setText(typeResult);
        txtResult.setText(answer + "/10");
        alertDialog.show();
    }


    public class LoadQuestionTask extends AsyncTask<HashMap<String, String>, Void, ArrayList<Question>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.show();
        }

        private String url;
        private String method;

        public LoadQuestionTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(ArrayList<Question> question) {
            super.onPostExecute(question);
            progressDialog.dismiss();
            questions = question;

            questionFragmentPagerAdapter = new QuestionFragmentPagerAdapter(getSupportFragmentManager(),question);
            viewPager.setAdapter(questionFragmentPagerAdapter);
        }

        @Override
        protected ArrayList<Question> doInBackground(HashMap<String, String>... params) {
            ArrayList<Question> questionArrayList = null;

            HashMap<String, String> paramList = null;
            if (params.length > 0){
                paramList = params[0];
            }
            try {
                String data = new HttpRequest().getDataFromAPI(this.url,this.method,paramList);
                questionArrayList = JSONParser.getQuestionList(data);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return questionArrayList;
        }
    }
}
