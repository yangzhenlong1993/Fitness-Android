package com.zhenlong.Fitness.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zhenlong.Fitness.Bean.Participants;
import com.zhenlong.Fitness.R;

import java.util.List;

public class ParticipantsListAdapter extends BaseAdapter {
    private List<Participants> participants;
    private LayoutInflater inflater;

    public ParticipantsListAdapter(Context ctx, List<Participants> participants) {
        inflater = LayoutInflater.from(ctx);
        this.participants = participants;
    }

    @Override
    public int getCount() {
        return participants.size();
    }

    @Override
    public Object getItem(int position) {
        return participants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ViewHolder viewHolder;
        if (view == null) {
            view = inflater.inflate(R.layout.participant_item, null);
            viewHolder = new ViewHolder();
            viewHolder.participantName = view.findViewById(R.id.participants_username);
            viewHolder.participantCount = view.findViewById(R.id.participants_count);
            viewHolder.participantLevel = view.findViewById(R.id.participants_level);
            viewHolder.participantRole = view.findViewById(R.id.participants_role);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.participantName.setText(participants.get(position).getUser().getUsername());
        viewHolder.participantCount.setText(participants.get(position).getCount().toString());
        viewHolder.participantLevel.setText(participants.get(position).getUser().getLevel().toString());
        viewHolder.participantRole.setText(participants.get(position).getRole());
        return view;
    }

    private static final class ViewHolder{
        TextView participantName;
        TextView participantCount;
        TextView participantLevel;
        TextView participantRole;
    }
}
