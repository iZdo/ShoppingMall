package com.izdo.shoppingmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.izdo.shoppingmall.Contants;
import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.WareListActivity;
import com.izdo.shoppingmall.adapter.CardViewtemDecortion;
import com.izdo.shoppingmall.adapter.HomeCategoryAdapter;
import com.izdo.shoppingmall.bean.Banner;
import com.izdo.shoppingmall.bean.Campaign;
import com.izdo.shoppingmall.bean.HomeCampaign;
import com.izdo.shoppingmall.http.BaseCallback;
import com.izdo.shoppingmall.http.OkHttpHelper;
import com.izdo.shoppingmall.http.SpotsCallback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class HomeFragment extends Fragment {

    private SliderLayout mSliderLayout;

    private PagerIndicator indicator;

    private RecyclerView mRecyclerView;

    private HomeCategoryAdapter mAdapter;

    private Gson mGson = new Gson();

    private List<Banner> mBanner;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mSliderLayout = (SliderLayout) view.findViewById(R.id.slider);

        indicator = (PagerIndicator) view.findViewById(R.id.custom_indicator);

        requestImages();

        initRecyclerView(view);

        return view;
    }

    /**
     * 请求图片
     */
    private void requestImages() {
        String url = "http://112.124.22.238:8081/course_api/banner/query?type=1";

        mHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                mBanner = banners;

                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });

    }

    /**
     * 初始化recyclerView
     */
    private void initRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        mHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

            @Override
            public void onBeforeRequest(Request request) {

            }

            @Override
            public void onFailure(Request request, Exception e) {

            }

            @Override
            public void onResponse(Response response) {

            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onTokenError(Response response, int code) {

            }
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns) {

        mAdapter = new HomeCategoryAdapter(homeCampaigns, getActivity());

        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {

                Intent intent = new Intent(getActivity(), WareListActivity.class);
                intent.putExtra(Contants.COMPAINGAIN_ID, campaign.getId());
                startActivity(intent);

            }
        });

        mRecyclerView.setAdapter(mAdapter);

        mRecyclerView.addItemDecoration(new CardViewtemDecortion());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
    }

    /**
     * 初始化slider
     */
    private void initSlider() {

        if (mBanner != null) {

            for (Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());
                textSliderView.description(banner.getName());

                mSliderLayout.addSlider(textSliderView);

            }
        }

        mSliderLayout.setCustomIndicator(indicator);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateUp);
        mSliderLayout.setDuration(3000);
    }
}
