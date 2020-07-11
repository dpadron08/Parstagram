package com.example.parstagram;

import android.app.Application;

import com.parse.Parse;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Post.registerSubclass(Post.class);
        Comment.registerSubclass(Comment.class);
        // set applicationId, and server server based on the values in the Heroku settings.
        // clientKey is not needed unless explicitly configured
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("davidpc-parstagram") // should correspond to APP_ID env variable
                .clientKey("CodepathMoveFastParseDavid")  // set explicitly unless clientKey is explicitly configured on Parse server
                .server("https://davidpc-parstagram.herokuapp.com/parse").build());
    }
}
