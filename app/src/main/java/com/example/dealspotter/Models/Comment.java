package com.example.dealspotter.Models;

public class Comment {

    String properdyId;
    String userId;
    String comment;

    public String getDateofpost() {
        return dateofpost;
    }

    public void setDateofpost(String dateofpost) {
        this.dateofpost = dateofpost;
    }

    String dateofpost;

   /* public Comment(String properdyId, String userId, String comment) {
        this.properdyId = properdyId;
        this.userId = userId;
        this.comment = comment;
    } */
    public Comment(){}

    public Comment(String properdyId, String userId, String comment, String dateofpost) {
        this.properdyId = properdyId;
        this.userId = userId;
        this.comment = comment;
        this.dateofpost = dateofpost;
    }

    public String getProperdyId() {
        return properdyId;
    }

    public void setProperdyId(String properdyId) {
        this.properdyId = properdyId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
