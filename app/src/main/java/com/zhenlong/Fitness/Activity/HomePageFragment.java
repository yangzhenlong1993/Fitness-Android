package com.zhenlong.Fitness.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.zhenlong.Fitness.Adapter.HomePageAdapter;
import com.zhenlong.Fitness.R;
import com.zhenlong.Fitness.Weight.AutoPollRecyclerView;

import java.util.ArrayList;

public class HomePageFragment extends Fragment implements View.OnClickListener {

    private AutoPollRecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HomePageAdapter homePageAdapter;
    private LinearLayout createLinear, joinedLinear, nearbyLinear, popularLinear;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_homepage, container, false);
        mRecyclerView = view.findViewById(R.id.home_page_recycleView);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initWeight();

        Bitmap bitmap1 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.football); // 间接调用 BitmapFactory.decodeStream
        Bitmap bitmap2 = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.climbing);
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        bitmaps.add(bitmap1);
        bitmaps.add(bitmap2);
        homePageAdapter=new HomePageAdapter(bitmaps);

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager =  new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(homePageAdapter);
        mRecyclerView.start();

    }

    private void initWeight() {
        createLinear = getActivity().findViewById(R.id.home_page_create_linear);
        joinedLinear = getActivity().findViewById(R.id.home_page_joined_linear);
        nearbyLinear = getActivity().findViewById(R.id.home_page_nearby_linear);
        popularLinear = getActivity().findViewById(R.id.home_page_popular_linear);
        createLinear.setOnClickListener(this);
        joinedLinear.setOnClickListener(this);
        nearbyLinear.setOnClickListener(this);
        popularLinear.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        if(null != mRecyclerView){
            mRecyclerView.stop();
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        Intent intent= null;
        switch (v.getId()){

            case R.id.home_page_create_linear:
                intent = new Intent(getActivity(), CreateEventActivity.class);
                startActivity(intent);
                break;
            case R.id.home_page_joined_linear:
                intent = new Intent(getActivity(), MyEventActivity.class);
                startActivity(intent);
                break;
            case R.id.home_page_nearby_linear:
                intent = new Intent( getActivity(), NearByEvents.class);
                startActivity(intent);
                break;
            case R.id.home_page_popular_linear:
                intent = new Intent(getActivity(),PopularEvents.class);
                startActivity(intent);
                break;
        }
    }
}
