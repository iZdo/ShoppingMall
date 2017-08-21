package com.izdo.shoppingmall;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.izdo.shoppingmall.adapter.HWAdapter;
import com.izdo.shoppingmall.adapter.decoration.DividerItemDecoration;
import com.izdo.shoppingmall.bean.Page;
import com.izdo.shoppingmall.bean.Wares;
import com.izdo.shoppingmall.utils.Pager;
import com.izdo.shoppingmall.widget.MyToolbar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * Created by iZdo on 2017/8/21.
 */

public class WareListActivity extends AppCompatActivity implements Pager.OnPageListener<Wares>, TabLayout.OnTabSelectedListener, View.OnClickListener {

    public static final int TAG_DEFAULT = 0;
    public static final int TAG_SALE = 1;
    public static final int TAG_PRICE = 2;

    public static final int ACTION_LIST = 1;
    public static final int ACTION_GRID = 2;


    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.txt_summary)
    private TextView mTxtSummary;


    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerview_wares;

    @ViewInject(R.id.refresh_layout)
    private MaterialRefreshLayout mRefreshLayout;

    @ViewInject(R.id.toolbar)
    private MyToolbar mToolbar;

    private int orderBy = 0;
    private long campaignId = 0;

    private HWAdapter mWaresAdapter;

    private Pager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_warelist);
        ViewUtils.inject(this);

        initToolBar();

        campaignId = getIntent().getLongExtra(Contants.COMPAINGAIN_ID, 0);

        initTab();

        getData();
    }

    private void initToolBar() {

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WareListActivity.this.finish();
            }
        });


        //mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        //mToolbar.getRightButton().setTag(ACTION_LIST);

        // mToolbar.setRightButtonOnClickListener(this);

    }

    private void getData() {

        pager = Pager.newBuilder().setUrl(Contants.API.WARES_CAMPAIN_LIST)
                .putParams("campaignId", campaignId)
                .putParams("orderBy", orderBy)
                .setRefreshLayout(mRefreshLayout)
                .setLoadMore(true)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());

        pager.request();
    }

    private void initTab() {
        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setText("默认");
        tab.setTag(TAG_DEFAULT);

        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("价格");
        tab.setTag(TAG_PRICE);

        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setText("销量");
        tab.setTag(TAG_SALE);

        mTabLayout.addTab(tab);

        mTabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void load(List datas, int totalPage, int totalCount) {

        mTxtSummary.setText("共有" + totalCount + "件商品");

        if (mWaresAdapter == null) {
            mWaresAdapter = new HWAdapter(this, datas);
            mRecyclerview_wares.setAdapter(mWaresAdapter);
            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerview_wares.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
            mRecyclerview_wares.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    @Override
    public void refresh(List datas, int totalPage, int totalCount) {

        mWaresAdapter.refreshData(datas);
        mRecyclerview_wares.scrollToPosition(0);
    }

    @Override
    public void loadMore(List datas, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        orderBy = (int) tab.getTag();
        pager.putParam("orderBy", orderBy);
        pager.request();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onClick(View view) {

        //        int action = (int) view.getTag();
        //
        //        if (ACTION_LIST == action) {
        //
        //            mToolbar.setRightButtonIcon(R.drawable.icon_list_32);
        //            mToolbar.getRightButton().setTag(ACTION_GRID);
        //            mWaresAdapter.resetLayout(R.layout.template_grid_wares);
        //            mRecyclerview_wares.setLayoutManager(new GridLayoutManager(this, 2));
        //
        //        } else if (ACTION_GRID == action) {
        //
        //            mToolbar.setRightButtonIcon(R.drawable.icon_grid_32);
        //            mToolbar.getRightButton().setTag(ACTION_LIST);
        //            mWaresAdapter.resetLayout(R.layout.template_hot_wares);
        //            mRecyclerview_wares.setLayoutManager(new LinearLayoutManager(this));
        //
        //        }
    }
}
