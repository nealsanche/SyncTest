package com.robotsandpencils.synctest.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by nealsanche on 2016-11-15.
 */

public class Message extends RealmObject implements CanBeNew {

    @SerializedName("id")
    @PrimaryKey
    @Expose
    private long id;

    @SerializedName("date")
    @Expose
    private Date date;

    @SerializedName("to")
    @Expose
    private String to;

    @SerializedName("message")
    @Expose
    private String mesage;

    @SerializedName("subject")
    @Expose
    private String subject;

    private boolean isNew;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getMesage() {
        return mesage;
    }

    public void setMesage(String mesage) {
        this.mesage = mesage;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public boolean isNew() {
        return isNew;
    }
}
