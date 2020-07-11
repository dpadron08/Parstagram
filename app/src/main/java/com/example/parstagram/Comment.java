package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

@ParseClassName("Comment")
public class Comment extends com.parse.ParseObject {

    public static final String KEY_TEXT = "text";
    public static final String KEY_POST = "postOwner";
    public static final String KEY_CREATED_KEY = "createdAt";

    public Comment() {}

    public String getText() {
        return getString(KEY_TEXT);
    }

    public void setText(String description) {
        put(KEY_TEXT, description);
    }

    public ParseObject getPost() {
        return getParseObject(KEY_POST);
    }
    public void setPost(ParseObject obj) {
        put(KEY_POST, obj);
    }

}
