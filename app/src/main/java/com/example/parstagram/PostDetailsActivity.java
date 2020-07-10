package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    ImageView ivImage;
    ImageView ivProfilePic;
    TextView tvUsername;
    TextView tvTimestamp;
    TextView tvDescription;

    Button btnLike;
    ArrayList<ParseObject> listOfUsersFavorited = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ivImage = findViewById(R.id.ivImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfilePic = findViewById(R.id.ivProfilePic);
        btnLike = findViewById(R.id.btnLike);

        post  = (Post) Parcels.unwrap(getIntent().getParcelableExtra("post"));

        // populate fields
        assert post != null;
        ParseFile image = post.getImage();
        if (image != null) {
            Glide.with(this).load(image.getUrl()).into(ivImage);
        }

        ParseFile file  = post.getUser().getParseFile("profilePic");
        if (file != null) {
            int radius = 100;
            int margin = 100;
            Glide.with(this).load(file.getUrl())
                    .transform(new CircleCrop())
                    .into(ivProfilePic);
        }
        tvUsername.setText(post.getUser().getUsername());
        tvTimestamp.setText(post.getCreatedAt().toString());
        tvDescription.setText(post.getDescription());

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("PostsAdapter", "Clicked Liked");
                //ParseQuery query = post.getUserFavorited(ParseUser.getCurrentUser());
                ParseRelation relation = post.getRelation("usersFavorited");
                ParseQuery query = relation.getQuery();
                // search for user in query
                query.findInBackground(new FindCallback() {
                    @Override
                    public void done(List objects, ParseException e) {
                        Log.i("PostsAdapter", "Other done function getLikes");
                    }

                    @Override
                    public void done(Object o, Throwable throwable) {
                        ArrayList<ParseObject> obj = (ArrayList) o;
                        listOfUsersFavorited.clear();
                        listOfUsersFavorited.addAll(obj);
                        //Log.i("PostsAdapter", "Attr : " + ((ParseUser)obj.get(1)).getUsername());


                        for (ParseObject a : listOfUsersFavorited) {
                            Log.i("PostsAdapter", "User liked " + ((ParseUser)a).getUsername());
                        }

                        toggleLike(post, listOfUsersFavorited);

                    }
                });
            }
        });
        styleLikeButtons();
    }

    private void styleLikeButtons() {
        // color the like buttons depending on if user has liked post

        ParseRelation relation = post.getRelation("usersFavorited");
        ParseQuery query = relation.getQuery();

        // list of users who favorited this post
        final ArrayList<ParseObject> listOfUsersFavorited = new ArrayList<>();
        // search for user in query
        query.findInBackground(new FindCallback() {
            @Override
            public void done(List objects, ParseException e) {
                Log.i("PostsAdapter", "Other done function getLikes");
            }

            @Override
            public void done(Object o, Throwable throwable) {
                ArrayList<ParseObject> obj = (ArrayList) o;
                listOfUsersFavorited.clear();
                listOfUsersFavorited.addAll(obj);
                //Log.i("PostsAdapter", "Attr : " + ((ParseUser)obj.get(1)).getUsername());

                // color buttons
                // color buttons
                boolean userFoundInList = false;
                for (ParseObject parseObject : listOfUsersFavorited) {
                    if (  ((ParseUser)parseObject).getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ) {
                        userFoundInList = true;
                        break;
                    }
                }
                if (userFoundInList) {
                    // user wants to un like, remove user from list
                    btnLike.setBackgroundResource(R.drawable.instagram_heart_pressed);
                } else {
                    btnLike.setBackgroundResource(R.drawable.instagram_heart_unpressed);
                }


            }
        });
    }

    private void toggleLike(Post post, ArrayList<ParseObject> listUsers) {
        boolean userFoundInList = false;
        for (ParseObject parseObject : listUsers) {
            if (  ((ParseUser)parseObject).getObjectId().equals(ParseUser.getCurrentUser().getObjectId()) ) {
                userFoundInList = true;
                break;
            }

            //Log.i("PostsAdapter", "Looking");
        }
        if (userFoundInList) {
            // user wants to un like, remove user from list
            post.getUsersFavoritedRelation().remove(ParseUser.getCurrentUser());
            Log.i("PostsAdapter", "Trying to remove like");
            post.saveInBackground();
            btnLike.setBackgroundResource(R.drawable.instagram_heart_unpressed);
        } else {
            // user wants to like, put user on list
            post.getUsersFavoritedRelation().add(ParseUser.getCurrentUser());
            post.saveInBackground();
            btnLike.setBackgroundResource(R.drawable.instagram_heart_pressed);
        }

    }
}