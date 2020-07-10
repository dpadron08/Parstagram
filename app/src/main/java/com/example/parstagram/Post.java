package com.example.parstagram;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseFileUtils;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcel;

@Parcel(analyze={Post.class})
@ParseClassName("Post")
public class Post extends com.parse.ParseObject  {

    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_USER = "user";
    public static final String KEY_CREATED_KEY = "createdAt";

    // for Parceler library
    public Post() {}

    public String getDescription() {
        return getString(KEY_DESCRIPTION);
    }

    public void setDescription(String description) {
        put(KEY_DESCRIPTION, description);
    }

    public ParseFile getImage() {
        return getParseFile(KEY_IMAGE);
    }
    public void setImage(ParseFile parseFile) {
        put(KEY_IMAGE, parseFile);
    }

    public ParseUser getUser() {
        return getParseUser(KEY_USER);
    }
    public void setUser(ParseUser user) {
        put(KEY_USER, user);
    }


    // for likes
    public ParseRelation<ParseUser> getUsersFavoritedRelation() {
        return getRelation("usersFavorited");
    }

    public void addUserFavorited(ParseUser user) {
        getUsersFavoritedRelation().add(user);
        saveInBackground();
    }

    public void removeUserFavorited(ParseUser user) {
        getUsersFavoritedRelation().remove(user);
        saveInBackground();
    }
    public ParseQuery getUserFavorited(ParseUser user) {
        ParseQuery query = getUsersFavoritedRelation().getQuery();
        return query;
    }
}
