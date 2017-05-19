package com.hcmut.testiq.models;

/**
 * Created by ThanhNguyen on 5/7/2017.
 */

public class Question {
    private String id;
    private String content;
    private String ansA;
    private String ansB;
    private String ansC;
    private String ansD;
    private String ans;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnsA() {
        return ansA;
    }

    public void setAnsA(String ansA) {
        this.ansA = ansA;
    }

    public String getAnsB() {
        return ansB;
    }

    public void setAnsB(String ansB) {
        this.ansB = ansB;
    }

    public String getAnsC() {
        return ansC;
    }

    public void setAnsC(String ansC) {
        this.ansC = ansC;
    }

    public String getAnsD() {
        return ansD;
    }

    public void setAnsD(String ansD) {
        this.ansD = ansD;
    }

    public String getAns() {
        return ans;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id='" + id + '\'' +
                ", content='" + content + '\'' +
                ", ansA='" + ansA + '\'' +
                ", ansB='" + ansB + '\'' +
                ", ansC='" + ansC + '\'' +
                ", ansD='" + ansD + '\'' +
                ", ans='" + ans + '\'' +
                '}';
    }
}
