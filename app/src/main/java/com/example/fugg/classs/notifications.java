package com.example.fugg.classs;

import java.util.ArrayList;

public class notifications {
    String leader_name;
    String leader_email;
    String from;
    String message;
    String to;
    String type;
    ArrayList<String>arrayList;
    ArrayList<String>arrayListlist1;
    ArrayList<String>arrayListlist2;
    String project_name;
    String project_description;

    public notifications( String project_name,String leader_name, String leader_email, ArrayList<String> arrayListlist1, ArrayList<String> arrayListlist2) {
        this.project_name = project_name;
        this.leader_name = leader_name;
        this.leader_email = leader_email;
        this.arrayListlist1 = arrayListlist1;
        this.arrayListlist2 = arrayListlist2;

    }

    public notifications(String message, String project_name, ArrayList<String> arrayListlist1, ArrayList<String> arrayListlist2) {
        this.message = message;
        this.project_name = project_name;
        this.arrayListlist1 = arrayListlist1;
        this.arrayListlist2 = arrayListlist2;
    }

    public notifications(String project_name, String leader_name, String leader_email) {
        this.project_name = project_name;
        this.leader_name = leader_name;
        this.leader_email = leader_email;
    }

    public String getLeader_name() {
        return leader_name;
    }

    public void setLeader_name(String leader_name) {
        this.leader_name = leader_name;
    }

    public String getLeader_email() {
        return leader_email;
    }

    public void setLeader_email(String leader_email) {
        this.leader_email = leader_email;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public notifications(String project_name, String project_description,ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        this.project_name = project_name;
        this.project_description = project_description;
    }

    public notifications(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getArrayListlist1() {
        return arrayListlist1;
    }

    public void setArrayListlist1(ArrayList<String> arrayListlist1) {
        this.arrayListlist1 = arrayListlist1;
    }

    public ArrayList<String> getArrayListlist2() {
        return arrayListlist2;
    }

    public void setArrayListlist2(ArrayList<String> arrayListlist2) {
        this.arrayListlist2 = arrayListlist2;
    }
}
