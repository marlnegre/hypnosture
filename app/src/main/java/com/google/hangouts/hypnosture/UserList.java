package com.google.hangouts.hypnosture;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by neil on 2/15/18.
 */

public class UserList extends ArrayAdapter<User> {

    private Activity context;
    private List<User> userList;

    public  UserList(Activity context, List<User> userList){
        super(context, R.layout.list_layout, userList);
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView profileUser = listViewItem.findViewById(R.id.textViewName);
        TextView profileEmail = listViewItem.findViewById(R.id.textViewEmail);

        User userprofile = userList.get(position);


        profileEmail.setText(userprofile.getEmail());
        profileUser.setText(userprofile.getUsername());

        return listViewItem;
    }
}
