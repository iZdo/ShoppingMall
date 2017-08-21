package com.izdo.shoppingmall.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.google.gson.Gson;
import com.izdo.shoppingmall.Contants;
import com.izdo.shoppingmall.R;
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
            public void onSuccess(Response response, List<Banner> banners) {

                mBanner = banners;

                initSlider();
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });

    }

       /**
     * 初始化recyclerView
     */
    private void initRecyclerView(View view) {

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);

        //        List<HomeCategory> datas = new ArrayList<>(15);
        //
        //        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
        //        datas.add(category);
        //
        //        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
        //        datas.add(category);
        //        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
        //        datas.add(category);
        //
        //        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
        //        datas.add(category);
        //
        //        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
        //        datas.add(category);
        //
        //        mAdapter = new HomeCategoryAdapter(datas);
        //
        //        mRecyclerView.setAdapter(mAdapter);
        //
        //        mRecyclerView.addItemDecoration(new DividerItemDecoration());
        //
        //        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));


        mHttpHelper.get(Contants.API.CAMPAIGN_HOME, new BaseCallback<List<HomeCampaign>>() {

            @Override
            public void onRequestBefore(Request request) {

            }

            @Override
            public void onFailure(Request request, IOException e) {

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
        });
    }

    private void initData(List<HomeCampaign> homeCampaigns) {

        mAdapter = new HomeCategoryAdapter(homeCampaigns, getActivity());

        mAdapter.setOnCampaignClickListener(new HomeCategoryAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(), "title=" + campaign.getTitle(), Toast.LENGTH_SHORT).show();
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
