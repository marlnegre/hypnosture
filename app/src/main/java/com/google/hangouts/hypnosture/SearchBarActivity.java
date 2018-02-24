package com.google.hangouts.hypnosture;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class SearchBarActivity extends AppCompatActivity {

    private EditText mSearchField;
    Button mSearchButton;

    private RecyclerView mResultList;

    private DatabaseReference mUserProfileDatabase;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_bar);

        mUserProfileDatabase = FirebaseDatabase.getInstance().getReference("Profiles");

        mSearchField = findViewById(R.id.search_field);
        mSearchButton = findViewById(R.id.imageButton);
        mResultList = findViewById(R.id.result_list);
        mResultList.setHasFixedSize(true);
        mResultList.setLayoutManager(new LinearLayoutManager(this));


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = mSearchField.getText().toString();
                UserSearch(searchText);
            }
        });

    }

    private void UserSearch(String searchText) {

        Toast.makeText(this, "Started searching...", Toast.LENGTH_SHORT).show();

       Query SearchQuery = mUserProfileDatabase.orderByChild("fullname").startAt(searchText).endAt(searchText + "\uf8ff");

       FirebaseRecyclerAdapter<UserProfile, UsersViewHolder> RecyclerAdapter = new FirebaseRecyclerAdapter<UserProfile, UsersViewHolder>(
                UserProfile.class,
                R.layout.list_layout,
                UsersViewHolder.class,
                SearchQuery
        ){

            @Override
            protected void populateViewHolder(UsersViewHolder viewHolder, UserProfile model, int position){

                viewHolder.setDetails(getApplicationContext(), model.getFullname(), model.getProfilePicURL());

            }
        };

        mResultList.setAdapter(RecyclerAdapter);

    }


    public static class UsersViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public UsersViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
        }

        public void setDetails(Context ctx, String userName, String userImage){

            TextView user_name = mView.findViewById(R.id.textViewName);
            ImageView user_image = mView.findViewById(R.id.profileImage);

            user_name.setText(userName);
            Glide.with(ctx).load(userImage).into(user_image);

        }
    }

}
