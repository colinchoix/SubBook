package com.example.colin.subbook;

import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Date;

import static android.icu.lang.UProperty.INT_START;

/**
 * Created by Colin on 2018-02-02.
 */

public class Subscription {
    private String name;
    private String startDate;
    private Double price;
    private String comment;

    Subscription(String name, String date, Double price){
        this.name = name;
        this.startDate = date;
        this.price = price;
        //this.comment??
    }

    Subscription(String name, String date, Double price, String comment){
        this.name = name;
        this.startDate = date;
        this.price = price;
        this.comment = comment;

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPrice(Double price){
        this.price = price;
    }

    public Double getPrice(){
        return this.price;
    }

    public void setDate(String date){
        this.startDate = date;
    }

    public String getDate(){
        return this.startDate;
    }

    public void setComment(String comment){
        this.comment = comment;
    }

    public String getComment(){
        return this.comment;
    }

    public String toString(){
        DecimalFormat cents = new DecimalFormat("#0.00");

        return String.format("%-20s \n\n%-45s%s",this.name,this.startDate,("$"+cents.format(this.price)));
        //return this.name +""+this.startDate+"\n$"+cents.format(this.price);
        //return ("%20s %s\n",this.name,this.startDate);

    }
}
