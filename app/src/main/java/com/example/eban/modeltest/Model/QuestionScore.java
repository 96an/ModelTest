package com.example.eban.modeltest.Model;

/**
 * Created by Eban on 12/17/2017.
 */

public class QuestionScore {

    private String Question_score;
    private String Score;
    private String User;

    public QuestionScore() {
    }

    public QuestionScore(String question_score, String score, String user) {
        Question_score = question_score;
        Score = score;
        User = user;
    }

    public String getQuestion_score() {
        return Question_score;
    }

    public void setQuestion_score(String question_score) {
        Question_score = question_score;
    }

    public String getScore() {
        return Score;
    }

    public void setScore(String score) {
        Score = score;
    }

    public String getUser() {
        return User;
    }

    public void setUser(String user) {
        User = user;
    }
}
