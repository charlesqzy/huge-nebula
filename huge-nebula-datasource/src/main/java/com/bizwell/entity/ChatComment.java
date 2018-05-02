package com.bizwell.entity;

import java.util.List;

/**
 * Created by liujian on 2018/1/10.
 */
public class ChatComment {
  private Long id;
  private String commentTime;
  private String star;
  private String comments;
  private String userId;
  private Double score;
    private String searchVal;
    private String orderVal;

    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getCommentTime() {
        return commentTime;
    }
    
    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
    }
    
    public String getStar() {
        return star;
    }
    
    public void setStar(String star) {
        this.star = star;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public Double getScore() {
        return score;
    }
    
    public void setScore(Double score) {
        this.score = score;
    }
    
    public String getSearchVal() {
        return searchVal;
    }
    
    public void setSearchVal(String searchVal) {
        this.searchVal = searchVal;
    }
    
    public String getOrderVal() {
        return orderVal;
    }
    
    public void setOrderVal(String orderVal) {
        this.orderVal = orderVal;
    }
 
}
