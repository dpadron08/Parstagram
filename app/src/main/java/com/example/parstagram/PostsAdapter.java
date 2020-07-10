package com.example.parstagram;

import android.content.Context;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import org.parceler.Parcels;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {

    Context context;
    private List<Post> posts;

    public PostsAdapter(Context context, List<Post> posts) {
        this.context = context;
        this.posts = posts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.bind(post);
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // Clean all elements of the recycler
    public void clear() {
        posts.clear();
        notifyDataSetChanged();
    }

    // Add a list of items -- change to type used
    public void addAll(List<Post> list) {
        posts.addAll(list);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUsername;
        private ImageView ivImage;
        private TextView tvDescription;

        ImageView ivProfilePic;
        TextView tvTimestamp;
        Button btnLike;
        Button btnComment;
        Button btnSave;

        ConstraintLayout container;

        // list of users that favorited this post
        final ArrayList<ParseObject> usersFavoritedList = new ArrayList<>();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            btnLike = itemView.findViewById(R.id.btnLike);
            btnComment = itemView.findViewById(R.id.btnComment);
            btnSave = itemView.findViewById(R.id.btnSave);

            container = itemView.findViewById(R.id.container);
        }

        public void bind(final Post post) {
            // Bind the post data to the view elements
            tvDescription.setText(post.getDescription());
            Log.i("ABC", "URL: " + post.getImage().getUrl());
            tvUsername.setText(post.getUser().getUsername());
            ParseFile image = post.getImage();
            if (image != null) {
                Glide.with(context).load(image.getUrl()).into(ivImage);
            }

            String date = ((Date) post.getCreatedAt()).toString();
            //String relativeTimestamp = getRelativeTimeAgo( date );
            tvTimestamp.setText(date);
            ParseFile file  = post.getUser().getParseFile("profilePic");
            if (file != null) {
                int radius = 100;
                int margin = 100;
                Glide.with(context).load(file.getUrl())
                        .transform(new CircleCrop())
                        .into(ivProfilePic);
            }

            container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.i("AAA", "Clicked");
                    // make sure the position is valid, i.e. actually exists in the view
                    // create intent for the new activity
                    Intent intent = new Intent(context, PostDetailsActivity.class);
                    intent.putExtra("post", Parcels.wrap(post));
                    context.startActivity(intent);

                }
            });



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
                            usersFavoritedList.addAll(obj);
                            //Log.i("PostsAdapter", "Attr : " + ((ParseUser)obj.get(1)).getUsername());

                            /*
                            for (ParseObject a : usersFavoritedList) {
                                Log.i("PostsAdapter", "User liked " + ((ParseUser)a).getUsername());
                            }
                             */

                        }
                    });
                }
            });


        }
        /*
        public String getRelativeTimeAgo(String rawJsonDate) {
            String twitterFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";
            //String twitterFormat = "yyyy-MM-ddTHH::mm:ss.SSS";

            SimpleDateFormat sf = new SimpleDateFormat(twitterFormat, Locale.ENGLISH);
            sf.setLenient(true);

            String relativeDate = "";
            try {
                long dateMillis = sf.parse(rawJsonDate).getTime();
                relativeDate = DateUtils.getRelativeTimeSpanString(dateMillis,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS).toString();
            } catch (ParseException | java.text.ParseException e) {
                e.printStackTrace();
            }

            return relativeDate;
        }

         */
    }
}
