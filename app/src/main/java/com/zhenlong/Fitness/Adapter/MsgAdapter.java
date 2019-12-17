package com.zhenlong.Fitness.Adapter;

import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenlong.Fitness.Bean.Msg;
import com.zhenlong.Fitness.R;

import java.util.List;

public class MsgAdapter extends RecyclerView.Adapter<MsgAdapter.ViewHolder> {
    private List<Msg> msgList;

    public MsgAdapter(List<Msg> msgList) {
        this.msgList = msgList;
    }

    @NonNull
    @Override
    public MsgAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MsgAdapter.ViewHolder holder, int position) {
        Msg msg = msgList.get(position);
        if (msg.getType() == Msg.TYPE_RECEIVED) {
            if(msg.getImg()==null){
                holder.leftTextLayout.setVisibility(View.VISIBLE);
                holder.rightTextLayout.setVisibility(View.GONE);
                holder.leftImgLayout.setVisibility(View.GONE);
                holder.rightImgLayout.setVisibility(View.GONE);
                holder.leftMsg.setText(msg.getShortMessage());
            } else {
                holder.leftImgLayout.setVisibility(View.VISIBLE);
                holder.rightTextLayout.setVisibility(View.GONE);
                holder.leftTextLayout.setVisibility(View.GONE);
                holder.rightImgLayout.setVisibility(View.GONE);
                holder.leftImg.setImageBitmap(BitmapFactory.decodeByteArray(msg.getImg(),0,msg.getImg().length));
            }
        } else if (msg.getType() ==Msg.TYPE_SENT) {
            if(msg.getImg()==null){
                holder.rightTextLayout.setVisibility(View.VISIBLE);
                holder.leftTextLayout.setVisibility(View.GONE);
                holder.leftImgLayout.setVisibility(View.GONE);
                holder.rightImgLayout.setVisibility(View.GONE);
                holder.rightMsg.setText(msg.getShortMessage());
            } else{
                holder.rightImgLayout.setVisibility(View.VISIBLE);
                holder.rightTextLayout.setVisibility(View.GONE);
                holder.leftTextLayout.setVisibility(View.GONE);
                holder.leftImgLayout.setVisibility(View.GONE);
                holder.rightImg.setImageBitmap(BitmapFactory.decodeByteArray(msg.getImg(),0,msg.getImg().length));
            }
        }
    }

    @Override
    public int getItemCount() {
        return msgList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout leftTextLayout,rightTextLayout, leftImgLayout,rightImgLayout;

        TextView leftMsg;
        TextView rightMsg;
       ImageView leftImg,rightImg;

        public ViewHolder(View view) {
            super(view);
            leftTextLayout =  view.findViewById(R.id.left_text_layout);
            rightTextLayout =view.findViewById(R.id.right_text_layout);
            leftImgLayout = view.findViewById(R.id.left_img_layout);
            rightImgLayout= view.findViewById(R.id.right_img_layout);
            leftMsg =  view.findViewById(R.id.left_msg);
            rightMsg =  view.findViewById(R.id.right_msg);
            leftImg = view.findViewById(R.id.left_img);
            rightImg  = view.findViewById(R.id.right_img);
        }
    }
}
