package com.example.navtest;

public class OlympaidPointTableHandle {
    String email;
    double score;

    public OlympaidPointTableHandle() {
    }

    public OlympaidPointTableHandle(String email, double score) {
        this.email = email;
        this.score = score;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
