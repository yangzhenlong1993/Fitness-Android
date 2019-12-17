package com.zhenlong.Fitness.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.loopeer.cardstack.CardStackView;
import com.loopeer.cardstack.StackAdapter;
import com.zhenlong.Fitness.Bean.Event;
import com.zhenlong.Fitness.R;

public class EventCardStackAdapter extends StackAdapter<Event> {
    public EventCardStackAdapter(Context context) {
        super(context);
    }
    public static Integer[] TEST_DATAS = new Integer[]{
            R.color.color_1,
            R.color.color_2,
            R.color.color_3,
            R.color.color_4,
            R.color.color_5,
            R.color.color_6,
            R.color.color_7,
            R.color.color_8,
            R.color.color_9,
            R.color.color_10,
            R.color.color_11,
            R.color.color_12,
            R.color.color_13,
            R.color.color_14,
            R.color.color_15,
            R.color.color_16,
            R.color.color_17,
            R.color.color_18,
            R.color.color_19,
            R.color.color_20,
            R.color.color_21,
            R.color.color_22,
            R.color.color_23,
            R.color.color_24,
            R.color.color_25,
            R.color.color_26
    };

    @Override
    public void bindView(Event event, int position, CardStackView.ViewHolder holder) {
        ColorItemViewHolder h = (ColorItemViewHolder) holder;

        if(event.getDoneornot()==1){
            h.onBind(Color.GREEN, event);
        } else  {
            h.onBind(Color.RED, event);
        }
    }

    @Override
    protected CardStackView.ViewHolder onCreateView(ViewGroup parent, int viewType) {
        View view;
        view = getLayoutInflater().inflate(R.layout.list_card_item, parent, false);
        return new ColorItemViewHolder(view);
    }

    static class ColorItemViewHolder extends CardStackView.ViewHolder {
        View mLayout;
        View mContainerContent;
        TextView mTextTitle, expendedText;

        public ColorItemViewHolder(View view) {
            super(view);
            mLayout = view.findViewById(R.id.frame_list_card_item);
            mContainerContent = view.findViewById(R.id.container_list_content);
            mTextTitle =  view.findViewById(R.id.text_list_card_title);
            expendedText = view.findViewById(R.id.container_tv);
        }

        @Override
        public void onItemExpand(boolean b) {

            mContainerContent.setVisibility(b ? View.VISIBLE : View.GONE);
        }

        public void onBind(Integer color, Event event) {
            mLayout.getBackground().setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_ATOP));
            mTextTitle.setText(event.getTitle());
            expendedText.setText("aaaaaa");
        }

    }
}
