package com.izdo.shoppingmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cjj.MaterialRefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.izdo.shoppingmall.Contants;
import com.izdo.shoppingmall.R;
import com.izdo.shoppingmall.WareDetailActivity;
import com.izdo.shoppingmall.adapter.BaseAdapter;
import com.izdo.shoppingmall.adapter.HWAdapter;
import com.izdo.shoppingmall.adapter.decoration.DividerItemDecoration;
import com.izdo.shoppingmall.bean.Page;
import com.izdo.shoppingmall.bean.Wares;
import com.izdo.shoppingmall.utils.Pager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;


public class HotFragment extends Fragment {

    private HWAdapter mAdapter;

    @ViewInject(R.id.recyclerview)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.refresh_view)
    private MaterialRefreshLayout mRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_hot, container, false);

        ViewUtils.inject(this, view);

        Pager pager = Pager.newBuilder().setUrl(Contants.API.WARES_HOT).setLoadMore(true)
                .setOnPageListener(new Pager.OnPageListener() {
                    @Override
                    public void load(List datas, int totalPage, int totalCount) {
                        mAdapter = new HWAdapter(getContext(), datas);

                        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {

                                Wares wares = mAdapter.getItem(position);

                                Intent intent = new Intent(getActivity(), WareDetailActivity.class);

                                intent.putExtra(Contants.WARE, wares);

                                startActivity(intent);

                            }
                        });

                        mRecyclerView.setAdapter(mAdapter);

                        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
                        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));
                    }

                    @Override
                    public void refresh(List datas, int totalPage, int totalCount) {
                        mAdapter.clear();
                        mAdapter.addData(datas);
                        mRecyclerView.scrollToPosition(0);
                    }

                    @Override
                    public void loadMore(List datas, int totalPage, int totalCount) {
                        mAdapter.addData(mAdapter.getDatas().size(), datas);

                        mRecyclerView.scrollToPosition(mAdapter.getDatas().size());
                    }
                }).setPageSize(20)
                .setRefreshLayout(mRefreshLayout).build(getContext(), new TypeToken<Page<Wares>>() {
                }.getType());

        pager.request();

        return view;

    }

}
