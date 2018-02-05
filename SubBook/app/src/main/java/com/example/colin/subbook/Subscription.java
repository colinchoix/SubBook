/*Subscription
 *
 * Version 1.0
 *
 * Feb 05, 2018
 *
 * Copyright 2018 Colin Choi, CMPUT301, University of Alberta - All Rights Reserved
 *
 */

package com.example.colin.subbook;

import java.text.DecimalFormat;

/**
 * Represents a subscription object in the app
 *
 * @author cechoi
 *
 * @version 1.0
 */
public class Subscription {
    private String name;
    private String startDate;
    private Double price;
    private String comment;

    /**
     * Constructs a subscription instance using the given parameters
     *
     * @param name
     * @param date
     * @param price
     * @param comment
     */
    Subscription(String name, String date, Double price, String comment) {
        this.name = name;
        this.startDate = date;
        this.price = price;
        this.comment = comment;
    }

    /**
     * Sets the name of the subscription
     *
     * @param name - Name of subscription
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the name of the subscription
     *
     * @return - String
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the price of the subscription
     *
     * @param price - Value of the price
     */
    public void setPrice(Double price) {
        this.price = price;
    }

    /**
     * Returns the price of the subscription
     *
     * @return - Double
     */
    public Double getPrice() {
        return this.price;
    }

    /**
     * Sets the date of the subscription
     *
     * @param date - Date of subscription in a string format
     */
    public void setDate(String date) {
        this.startDate = date;
    }

    /**
     * Returns the start date of the subscription
     *
     * @return - String
     */
    public String getDate() {
        return this.startDate;
    }

    /**
     * Adds a comment to the subscription
     *
     * @param comment - Comment of subscription
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * Returns the comment of the subscription
     *
     * @return - String
     */
    public String getComment() {
        return this.comment;
    }

    /**
     * Returns a formatted string representation of the contents of the subscription
     *
     * @return - String
     */
    public String toString() {
        DecimalFormat cents = new DecimalFormat("#0.00");
        return String.format("%-20s \n\n%-45s%s", this.name, this.startDate, ("$" + cents.format(this.price)));
    }
}
