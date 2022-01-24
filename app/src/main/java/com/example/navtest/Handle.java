package com.example.navtest;

public class Handle {
    private String name, clas, phone, institution, imageDounloadUri;
    double practicePoint;
    double olympaidPoint;
    double currentPoint;
    int level;


    public Handle(String name, String clas, String phone, String institution, String imageDounloadUri, double practicePoint, double olympaidPoint, double currentPoint, int level) {
        this.name = name;
        this.clas = clas;
        this.phone = phone;
        this.institution = institution;
        this.imageDounloadUri = imageDounloadUri;
        this.practicePoint = practicePoint;
        this.olympaidPoint = olympaidPoint;
        this.currentPoint = currentPoint;
        this.level = level;
    }

    public Handle(String name, String clas, String phone, String institution, String imageDounloadUri, double practicePoint, double olympaidPoint, int level) {
        this.name = name;
        this.clas = clas;
        this.phone = phone;
        this.institution = institution;
        this.imageDounloadUri = imageDounloadUri;
        this.practicePoint = practicePoint;
        this.olympaidPoint = olympaidPoint;
        this.level = level;
    }

    public Handle(String name, String clas, String phone, String institution, String imageDounloadUri, double practicePoint, double olympaidPoint) {
        this.name = name;
        this.clas = clas;
        this.phone = phone;
        this.institution = institution;
        this.imageDounloadUri = imageDounloadUri;
        this.practicePoint = practicePoint;
        this.olympaidPoint = olympaidPoint;
    }

    public Handle(String name, String clas, String phone, String institution, String imageDounloadUri) {
        this.name = name;
        this.clas = clas;
        this.phone = phone;
        this.institution = institution;
        this.imageDounloadUri = imageDounloadUri;
    }

    public Handle() {
    }

    public double getCurrentPoint() {
        return currentPoint;
    }

    public void setCurrentPoint(double currentPoint) {
        this.currentPoint = currentPoint;
    }


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPracticePoint() {

        return practicePoint;
    }

    public void setPracticePoint(double practicePoint) {
        this.practicePoint = practicePoint;
    }

    public double getOlympaidPoint() {
        return olympaidPoint;
    }

    public void setOlympaidPoint(double olympaidPoint) {
        this.olympaidPoint = olympaidPoint;
    }

    public String getImageDounloadUri() {
        return imageDounloadUri;
    }

    public void setImageDounloadUri(String imageDounloadUri) {
        this.imageDounloadUri = imageDounloadUri;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }
}
