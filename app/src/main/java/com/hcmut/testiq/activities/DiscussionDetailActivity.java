package com.hcmut.testiq.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.hcmut.adapter.CommentAdapter;
import com.hcmut.testiq.R;
import com.hcmut.testiq.http_requests.HttpRequest;
import com.hcmut.testiq.models.Comment;
import com.hcmut.testiq.models.Question;
import com.hcmut.testiq.utils.JSONParser;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;

public class DiscussionDetailActivity extends AppCompatActivity implements View.OnClickListener {

    static final int NUM_ITEMS = 10;
    ImageButton btnPrevious, btnNext;
    TextView txtIndexQuestion;
    Button btnCancel, btnSubmitResult;

    ViewPager viewPager;
    QuestionFragmentPagerAdapter questionFragmentPagerAdapter;
    static ArrayList<Question> questions;

    ListView lvComments;
    ArrayList<Comment> arrComment;
    CommentAdapter commentAdapter;

    TextView txtNotify;
    EditText txtContent;
    ImageView btnCommitComment;

    ProgressDialog progressDialog;

    String questionId = "1";
    String content = "heheheehheeheheheheh";
    String username = "thachhuynh";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_discussion_detail);

        this.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        addControls();
        addEvents();
    }

    private void addControls(){
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

        lvComments = (ListView) findViewById(R.id.lvComments);
        arrComment = new ArrayList<>();
        commentAdapter = new CommentAdapter(DiscussionDetailActivity.this, R.layout.item_detail_comment, arrComment);
        lvComments.setAdapter(commentAdapter);

        txtNotify = (TextView) findViewById(R.id.txtNotify);
        txtContent = (EditText) findViewById(R.id.txtContent);
        btnCommitComment = (ImageView) findViewById(R.id.btnCommitComment);

        progressDialog = new ProgressDialog(DiscussionDetailActivity.this);
        progressDialog.setTitle("Thông báo");
        progressDialog.setMessage("Đang tải câu hỏi, vui lòng chờ ...");
        progressDialog.setCanceledOnTouchOutside(false);

        questions = new ArrayList<>();

        String url = "https://iqquiz.herokuapp.com/package/questions?id=" + indexDe;
        new LoadQuestionTask(url, "GET").execute(new HashMap[]{});

        //?content="Content"&questionid="QuestionId"&username="Username


    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private void addEvents() {
        btnPrevious.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        btnCommitComment.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPrevious:
                viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                txtIndexQuestion.setText((viewPager.getCurrentItem() + 1) + "/10");

                questionId = questions.get(viewPager.getCurrentItem()).getId();

                // get comments list from api
                String url = "https://iqquiz.herokuapp.com/question/comments?questionid=" + questionId;
                new LoadCommentTask(url, "GET").execute(new HashMap[]{});

                break;
            case R.id.btnNext:
                viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                txtIndexQuestion.setText((viewPager.getCurrentItem() + 1) + "/10");

                questionId = questions.get(viewPager.getCurrentItem()).getId();

                // get comments list from api
                String url1 = "https://iqquiz.herokuapp.com/question/comments?questionid=" + questionId;
                Log.e("URL", url1);

                new LoadCommentTask(url1, "GET").execute(new HashMap[]{});
                break;
            case R.id.btnCommitComment:
                questionId = questions.get(viewPager.getCurrentItem()).getId();
                if(txtContent.getText().equals(""))
                    Toast.makeText(this, "Comment is not empty", Toast.LENGTH_SHORT).show();
                else {
                    String input = txtContent.getText().toString();
                    try {
                        content = URLEncoder.encode(input, "utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences("account", MODE_PRIVATE);
                    username = sharedPreferences.getString("Email","").split("@")[0];
                    txtContent.setText("");
                    // add comment to database
                    String url2 = "https://iqquiz.herokuapp.com/comment/add?content=" + content
                            + "&questionid=" + questionId + "&username=" + username;
                    Log.e("URL1", url2);
                    new AddCommentTask(url2, "GET").execute(new HashMap[]{});

                }
                break;
            default:
                break;
        }
    }

    public static class QuestionFragmentPagerAdapter extends FragmentPagerAdapter {
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
            View swipeView = inflater.inflate(R.layout.item_detail_discussion, container, false);
            ImageView imgQuestion = (ImageView) swipeView.findViewById(R.id.imgQuestion);
            ImageView imgResultA = (ImageView) swipeView.findViewById(R.id.imgResultA);
            ImageView imgResultB = (ImageView) swipeView.findViewById(R.id.imgResultB);
            ImageView imgResultC = (ImageView) swipeView.findViewById(R.id.imgResultC);
            ImageView imgResultD = (ImageView) swipeView.findViewById(R.id.imgResultD);
            TextView txtAns = (TextView) swipeView.findViewById(R.id.txtAns);

            Bundle args = getArguments();
            final int position = args.getInt("position");

            Picasso.with(getActivity().getApplication()).load(questions.get(position).getContent()).into(imgQuestion);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsA()).into(imgResultA);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsB()).into(imgResultB);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsC()).into(imgResultC);
            Picasso.with(getActivity().getApplication()).load(questions.get(position).getAnsD()).into(imgResultD);

            txtAns.setText(questions.get(position).getAns().toUpperCase());

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

            questionId = questions.get(0).getId();

            // get comments list from api
            String url = "https://iqquiz.herokuapp.com/question/comments?questionid=" + questionId;
            new LoadCommentTask(url, "GET").execute(new HashMap[]{});
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

    public class AddCommentTask extends AsyncTask<HashMap<String, String>, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private String url;
        private String method;

        public AddCommentTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            Log.d("COMMENT_ADD", "" + response.toString());
            // get comments list from api
            String url = "https://iqquiz.herokuapp.com/question/comments?questionid=" + questionId;
            new LoadCommentTask(url, "GET").execute(new HashMap[]{});
        }

        @Override
        protected String doInBackground(HashMap<String, String>... params) {
            String data = "";

            HashMap<String, String> paramList = null;
            if (params.length > 0){
                paramList = params[0];
            }
            try {
                data = new HttpRequest().getDataFromAPI(this.url,this.method,paramList);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }
    }

    public class LoadCommentTask extends AsyncTask<HashMap<String, String>, Void, ArrayList<Comment>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private String url;
        private String method;

        public LoadCommentTask(String url, String method) {
            super();
            this.url = url;
            this.method = method;
        }

        @Override
        protected void onPostExecute(ArrayList<Comment> comments) {
            super.onPostExecute(comments);
            //Log.e("COMMENT", "" + comments.toString());
            commentAdapter.clear();
            commentAdapter.addAll(comments);
            if(comments.size()==0) {
                txtNotify.setText("No comment");
                //Toast.makeText(DiscussionDetailActivity.this, "" + comments.size(), Toast.LENGTH_SHORT).show();
            }
            else
                txtNotify.setText("");
            setListViewHeightBasedOnChildren(lvComments);
        }

        @Override
        protected ArrayList<Comment> doInBackground(HashMap<String, String>... params) {
            ArrayList<Comment> commentsArrayList = null;

            HashMap<String, String> paramList = null;
            if (params.length > 0){
                paramList = params[0];
            }
            try {
                String data = new HttpRequest().getDataFromAPI(this.url,this.method,paramList);
                commentsArrayList = JSONParser.getCommentList(data);
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return commentsArrayList;
        }
    }
}
