package com.hcmut.testiq.utils;

import com.hcmut.testiq.models.Comment;
import com.hcmut.testiq.models.Package;
import com.hcmut.testiq.models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ThanhNguyen on 5/7/2017.
 */

public class JSONParser {

    public static ArrayList<Package> getPackageList(String data) throws JSONException{
        ArrayList<Package> packageArrayList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(data);
        JSONArray packagesJson = jsonObject.getJSONArray("result");
        int length = packagesJson.length();

        for (int i = 0; i < length; i++){
            Package pack = new Package();
            pack.setId(packagesJson.getJSONObject(i).getString("id"));
            pack.setName(packagesJson.getJSONObject(i).getString("name"));

            packageArrayList.add(pack);
        }

        return packageArrayList;
    }

    public static ArrayList<Question> getQuestionList(String data) throws JSONException{
        ArrayList<Question> questionArrayList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(data);
        JSONArray questionsJson = jsonObject.getJSONArray("result");

        for (int i = 0; i < questionsJson.length(); i++){
            Question question = new Question();
            JSONObject questionObject = questionsJson.getJSONObject(i);

            question.setId(questionObject.getString("id"));
            question.setContent(questionObject.getString("content"));
            question.setAnsA(questionObject.getString("ansA"));
            question.setAnsB(questionObject.getString("ansB"));
            question.setAnsC(questionObject.getString("ansC"));
            question.setAnsD(questionObject.getString("ansD"));
            question.setAns(questionObject.getString("ans"));

            questionArrayList.add(question);
        }

        return questionArrayList;
    }

    public static ArrayList<Comment> getCommentList(String data) throws JSONException{
        ArrayList<Comment> commentArrayList = new ArrayList<>();

        JSONObject jsonObject = new JSONObject(data);
        JSONArray commentsJson = jsonObject.getJSONArray("result");

        for(int i = 0; i < commentsJson.length(); i++){
            Comment comment = new Comment();
            JSONObject commentObject = commentsJson.getJSONObject(i);

            comment.setId(commentObject.getString("id"));
            comment.setContent(commentObject.getString("content"));
            comment.setDate(commentObject.getString("createdAt"));
            comment.setUsername(commentObject.getString("UserName"));

            commentArrayList.add(comment);

        }

        return commentArrayList;
    }
}
