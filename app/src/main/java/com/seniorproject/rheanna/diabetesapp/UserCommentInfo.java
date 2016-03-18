package com.seniorproject.rheanna.diabetesapp;

import android.content.Context;

/**
 * Created by Victor on 3/7/2016.
 */
public class UserCommentInfo {
    String PostID ,originalusername, title, comment,commentusername;

    public UserCommentInfo(String PostID,String originalusername, String title, String comment, String commentusername){
        this.PostID= PostID;
        this.originalusername= originalusername;
        this.title = title;
        this.comment=comment;
        this. commentusername =  commentusername;
    }
}
