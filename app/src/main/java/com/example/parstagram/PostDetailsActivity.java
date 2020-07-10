package com.example.parstagram;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.ParseFile;

import org.parceler.Parcels;

public class PostDetailsActivity extends AppCompatActivity {

    Post post;

    ImageView ivImage;
    ImageView ivProfilePic;
    TextView tvUsername;
    TextView tvTimestamp;
    TextView tvDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        ivImage = findViewById(R.id.ivImage);
        tvUsername = findViewById(R.id.tvUsername);
        tvTimestamp = findViewById(R.id.tvTimestamp);
        tvDescription = findViewById(R.id.tvDescription);
        ivProfilePic = findViewById(R.id.ivProfilePic);

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
    }
}