package com.haibeey.android.sticky.models;

import android.database.Cursor;

public class Note {
    private String title;
    private String description;
    private long id;
    private String email;
    private boolean recurEveryday;
    private int interval;
    private int day;
    private int week;
    private int month;
    private int year;
    private int hour;
    private String label;
    private boolean show;

    public Note(Cursor cursor) {
        id=cursor.getInt(0);
        title=cursor.getString(1);
        description=cursor.getString(2);
        recurEveryday=cursor.getInt(3)==1;
        year=cursor.getInt(4);
        day=cursor.getInt(5);
        hour=cursor.getInt(6);
        month=cursor.getInt(7);
        interval=cursor.getInt(8);
        week=cursor.getInt(9);
        email=cursor.getString(10);
        label=cursor.getString(11);
    }

    public Note() {

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public String getEmail() {
        return email;
    }

    public long getId() {
        return id;
    }

    public int getDay() {
        return day;
    }

    public int getInterval() {
        return interval;
    }

    public String getDescription() {
        return description;
    }

    public int getWeek() {
        return week;
    }

    public String getTitle() {
        return title;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setShow(boolean show) {
        this.show = show;
    }

    public boolean isShow() {
        return show;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setRecurEveryday(boolean recurEveryday) {
        this.recurEveryday = recurEveryday;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public boolean isRecurEveryday() {
        return recurEveryday;
    }
}
