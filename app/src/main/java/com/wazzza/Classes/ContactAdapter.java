package com.wazzza.Classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;
import com.wazzza.R;

import java.util.ArrayList;

public class ContactAdapter extends ArrayAdapter<User> {
    public ContactAdapter(@NonNull Context context, ArrayList<User> arrayList) {
        super(context, 0, arrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        User user = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wazzus_contact_item, parent, false);
        }
        ImageView profilePicture = convertView.findViewById(R.id.wazzus_contact_item_profile_picture);
        TextView nickname = convertView.findViewById(R.id.wazzus_contact_item_nickname);
        TextView description = convertView.findViewById(R.id.wazzus_contact_item_description);
        Picasso.get().load(user.getProfilePicture()).into(profilePicture);
        nickname.setText(user.getNickname());
        description.setText(user.getDescription());
        return convertView;
    }
}
