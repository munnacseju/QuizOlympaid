package com.example.navtest;

public class QuestionSetUpHandle {
    private String question, option1, option2, option3, option4, curretOption;

    public QuestionSetUpHandle() {
    }

    public QuestionSetUpHandle(String question, String option1, String option2, String option3, String option4, String curretOption) {
        this.question = question;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.curretOption = curretOption;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOption1() {
        return option1;
    }

    public void setOption1(String option1) {
        this.option1 = option1;
    }

    public String getOption2() {
        return option2;
    }

    public void setOption2(String option2) {
        this.option2 = option2;
    }

    public String getOption3() {
        return option3;
    }

    public void setOption3(String option3) {
        this.option3 = option3;
    }

    public String getOption4() {
        return option4;
    }

    public void setOption4(String option4) {
        this.option4 = option4;
    }

    public String getCurretOption() {
        return curretOption;
    }

    public void setCurretOption(String curretOption) {
        this.curretOption = curretOption;
    }
}
