package com.seniorproject.rheanna.diabetesapp;

/**
 * Created by Rheanna on 2/28/2016.
 */
public class UserData {
    String username, date, hours, value;
    String number, tag, medication, HBA1C;
    public UserData(String username, String date, String hours){
        this.username = username;
        this.date = date;
        this.hours = hours;
    }

    public UserData(String username, String value){
        this.username = username;
        this.value = value;
    }

    public UserData(String username, String date, String number, String tag, String medication, String HBA1C){
        this.username = username;
        this.date = date;
        this.number = number;
        this.tag = tag;
        this.medication = medication;
        this.HBA1C = HBA1C;
    }

}
