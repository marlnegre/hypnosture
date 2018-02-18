package com.google.hangouts.hypnosture;

import android.app.Activity;
import android.content.Context;
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
 * Created by neil on 2/16/18.
 */

public class ProfileList extends ArrayAdapter<UserProfile> {

    private Activity context;
    private List<UserProfile> profileList;

    public  ProfileList(Activity context, List<UserProfile> profileList){
        super(context, R.layout.list_layout_profile, profileList);
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout_profile, null, true);

        ImageView profileImage = listViewItem.findViewById(R.id.adminImageView);
        TextView profileUser = listViewItem.findViewById(R.id.textViewName);
        TextView profileBirthday = listViewItem.findViewById(R.id.textViewBirthday);
        TextView profileSex =  listViewItem.findViewById(R.id.textViewSex);

        UserProfile userprofile = profileList.get(position);

        Glide.with(getContext()).load(userprofile.getProfilePicURL()).into(profileImage);
        profileUser.setText(userprofile.getFullname());
        profileBirthday.setText(userprofile.getBirthday());
        profileSex.setText(userprofile.getSex());

        return listViewItem;
    }

}
