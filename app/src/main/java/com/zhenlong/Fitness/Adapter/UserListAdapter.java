package com.zhenlong.Fitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenlong.Fitness.Bean.User;
import com.zhenlong.Fitness.R;

import java.util.List;

public class UserListAdapter extends BaseAdapter {
    private List<User> users;
    private LayoutInflater inflater;

    public UserListAdapter(Context ctx, List<User> users) {
        inflater = LayoutInflater.from(ctx);
        this.users = users;

    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.user_item, null);
            viewHolder = new ViewHolder();
            viewHolder.username = view.findViewById(R.id.user_item_name);
            viewHolder.gender = view.findViewById(R.id.user_item_gender);
            viewHolder.star = view.findViewById(R.id.user_item_star);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.username.setText(users.get(position).getUsername());
        if (users.get(position).getGender().intValue() == 0) {
            viewHolder.gender.setImageResource(R.drawable.male);
        } else {
            viewHolder.gender.setImageResource(R.drawable.female);
        }
        switch (users.get(position).getLevel().intValue()) {
            case 1:
                viewHolder.star.setImageResource(R.drawable.one_level);
                break;
            case 2:
                viewHolder.star.setImageResource(R.drawable.level_two);
                break;
            case 3:
                viewHolder.star.setImageResource(R.drawable.level_three);
                break;
            case 4:
                viewHolder.star.setImageResource(R.drawable.level_four);
                break;
            case 5:
                viewHolder.star.setImageResource(R.drawable.level_five);
                break;
        }
        return view;
    }

    private final static class ViewHolder {
        TextView username;
        ImageView gender;
        ImageView star;
    }
}
