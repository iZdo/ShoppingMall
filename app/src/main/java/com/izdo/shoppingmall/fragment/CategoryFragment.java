package com.izdo.shoppingmall.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.izdo.shoppingmall.Contants;
import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.adapter.BaseAdapter;
import com.izdo.shoppingmall.adapter.CategoryAdapter;
import com.izdo.shoppingmall.adapter.WaresAdapter;
import com.izdo.shoppingmall.adapter.decoration.DividerGridItemDecoration;
import com.izdo.shoppingmall.adapter.decoration.DividerItemDecoration;
import com.izdo.shoppingmall.bean.Banner;
import com.izdo.shoppingmall.bean.Category;
import com.izdo.shoppingmall.bean.Page;
import com.izdo.shoppingmall.bean.Wares;
import com.izdo.shoppingmall.http.BaseCallback;
import com.izdo.shoppingmall.http.OkHttpHelper;
import com.izdo.shoppingmall.http.SpotsCallback;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.List;

public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recyclerview_category)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.recyclerview_wares)
    private RecyclerView mRecyclerViewWares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.slider)
    private SliderLayout mSliderLayout;

    private CategoryAdapter mCategoryAdapter;
    private WaresAdapter mWaresAdapter;

    private int curPage = 1;
    private int pageSize = 10;
    private int totalPage = 1;
    private long category_id = 0;

    private static final int STATE_NORMAL = 0;
    private static final int STATE_REFRESH = 1;
    private static final int STATE_MORE = 2;

    private int state = STATE_NORMAL;

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);

        ViewUtils.inject(this, view);

        requestCategoryData();
        requestBannerDatas();

        initRefreshLayout();
        return view;
    }

    private void initRefreshLayout() {

        mRefreshLayout.setLoadMore(true);
        mRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {

                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {

                if (curPage <= totalPage) {
                    loadMoreData();
                } else {
                    Toast.makeText(getContext(), "没有更多数据", Toast.LENGTH_SHORT).show();
                    mRefreshLayout.finishRefreshLoadMore();
                }
            }
        });

    }

    private void refreshData() {

        curPage = 1;

        state = STATE_REFRESH;
        requestWares(category_id);
    }

    private void loadMoreData() {

        curPage = ++curPage;

        state = STATE_MORE;
        requestWares(category_id);
    }

    private void requestCategoryData() {

        mHttpHelper.get(Contants.API.CATEGORY_LIST, new SpotsCallback<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                showCategoryData(categories);

                if (categories != null && categories.size() > 0) {
                    category_id = categories.get(0).getId();
                    requestWares(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showCategoryData(final List<Category> categories) {

        mCategoryAdapter = new CategoryAdapter(getContext(), categories);

        mCategoryAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                Category category = mCategoryAdapter.getItem(position);

                category_id = category.getId();
                curPage = 1;
                state = STATE_NORMAL;

                requestWares(category_id);
            }
        });

        mRecyclerView.setAdapter(mCategoryAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
    }

    /**
     * 初始化recyclerView
     */
    private void requestBannerDatas() {

        String url = Contants.API.BANNER + "?type=1";

        mHttpHelper.get(url, new SpotsCallback<List<Banner>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Banner> banners) {

                showSliderViews(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }


    /**
     * 初始化slider
     */
    private void showSliderViews(List<Banner> banners) {

        if (banners != null) {

            for (Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(this.getActivity());
                sliderView.image(banner.getImgUrl());
                sliderView.description(banner.getName());
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);
                mSliderLayout.addSlider(sliderView);

            }
        }

        mSliderLayout.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mSliderLayout.setCustomAnimation(new DescriptionAnimation());
        mSliderLayout.setPresetTransformer(SliderLayout.Transformer.RotateDown);
        mSliderLayout.setDuration(3000);
    }

    private void requestWares(long categoryId) {

        String url = Contants.API.WARES_LIST + "?categoryId=" + categoryId + "&curPage=" + curPage + "&pageSize=" + pageSize;

        mHttpHelper.get(url, new BaseCallback<Page<Wares>>() {
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
            public void onSuccess(Response response, Page<Wares> waresPage) {
                curPage = waresPage.getCurrentPage();
                totalPage = waresPage.getTotalPage();

                showWaresData(waresPage.getList());

            }


            @Override
            public void onError(Response response, int code, Exception e) {

            }
        });
    }

    private void showWaresData(List<Wares> wares) {

        switch (state) {
            case STATE_NORMAL:

                if (mWaresAdapter == null) {
                    mWaresAdapter = new WaresAdapter(getContext(), wares);

                    mRecyclerViewWares.setAdapter(mWaresAdapter);

                    mRecyclerViewWares.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecyclerViewWares.setItemAnimator(new DefaultItemAnimator());
                    mRecyclerViewWares.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else{
                    mWaresAdapter.clear();
                    mWaresAdapter.addData(wares);
                }

                break;
            case STATE_REFRESH:
                mWaresAdapter.clear();
                mWaresAdapter.addData(wares);

                mRecyclerView.scrollToPosition(0);

                mRefreshLayout.finishRefresh();

                break;

            case STATE_MORE:

                mWaresAdapter.addData(mWaresAdapter.getDatas().size(), wares);

                mRecyclerView.scrollToPosition(mWaresAdapter.getDatas().size());

                mRefreshLayout.finishRefreshLoadMore();
                break;
        }
    }
}



