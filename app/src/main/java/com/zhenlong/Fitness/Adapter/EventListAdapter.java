package com.zhenlong.Fitness.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Util.DateTransfer;

import java.util.List;

public class EventListAdapter extends BaseAdapter {
    private List<Event> events;
    private LayoutInflater inflater;

    public EventListAdapter(Context ctx, List<Event> events) {
        inflater = LayoutInflater.from(ctx);
        this.events = events;

    }

    @Override
    public int getCount() {
        return events.size();
    }

    @Override
    public Object getItem(int position) {
        return events.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.event_item, null);
            viewHolder = new ViewHolder();
            viewHolder.eventTitle = view.findViewById(R.id.event_item_title);
            viewHolder.eventStartTime = view.findViewById(R.id.event_item_start_time);
            viewHolder.eventLocation = view.findViewById(R.id.event_item_location);
            viewHolder.eventImg = view.findViewById(R.id.event_item_img);
            viewHolder.eventStatus = view.findViewById(R.id.event_item_status);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        if (events.get(position).getDoneornot() == 1) {
            viewHolder.eventStatus.setText("Event Done");
            viewHolder.eventStatus.setTextColor(Color.RED);
        } else {
            viewHolder.eventStatus.setText("Coming Soon");
            viewHolder.eventStatus.setTextColor(Color.GREEN);
        }
        viewHolder.eventTitle.setText(events.get(position).getTitle());
        viewHolder.eventStartTime.setText(DateTransfer.dateToString(events.get(position).getStarttime()));
        viewHolder.eventLocation.setText(events.get(position).getLocationname());
        switch (events.get(position).getCategory()){
            case "run":
                viewHolder.eventImg.setImageResource(R.drawable.type_running);
                break;
            case "climb":
                viewHolder.eventImg.setImageResource(R.drawable.type_climbing);
                break;
            case "swim":
                viewHolder.eventImg.setImageResource(R.drawable.type_swim);
                break;
            case "cycling":
                viewHolder.eventImg.setImageResource(R.drawable.type_bike);
                break;
        }
        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        if (events.get(position).getDoneornot() == 1) {
            return false;
        }
        return super.isEnabled(position);
    }

    private final static class ViewHolder {
        TextView eventTitle;
        TextView eventStartTime;
        TextView eventLocation;
        ImageView eventImg;
        TextView eventStatus;
    }
}
