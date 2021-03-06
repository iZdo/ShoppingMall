package com.izdo.shoppingmall;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.izdo.shoppingmall.bean.Tab;
import com.izdo.shoppingmall.fragment.CartFragment;
import com.izdo.shoppingmall.fragment.CategoryFragment;
import com.izdo.shoppingmall.fragment.HomeFragment;
import com.izdo.shoppingmall.fragment.HotFragment;
import com.izdo.shoppingmall.fragment.MineFragment;
import com.izdo.shoppingmall.widget.FragmentTabHost;
import com.izdo.shoppingmall.widget.MyToolbar;

import java.util.ArrayList;
import java.util.List;

import static android.view.View.GONE;

public class MainActivity extends BaseActivity {

    private LayoutInflater mInflater;

    private FragmentTabHost mTabHost;

    private MyToolbar mMyToolbar;

    private CartFragment cartFragment;

    private List<Tab> mTabs = new ArrayList<>(5);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolBar();
        initTab();
    }

    private void initToolBar() {
        mMyToolbar = (MyToolbar) findViewById(R.id.toolbar);
    }

    private void initTab() {
        Tab tab_home = new Tab(HomeFragment.class, R.string.home, R.drawable.selector_icon_home);
        Tab tab_hot = new Tab(HotFragment.class, R.string.hot, R.drawable.selector_icon_hot);
        Tab tab_category = new Tab(CategoryFragment.class, R.string.catagory, R.drawable.selector_icon_category);
        Tab tab_cart = new Tab(CartFragment.class, R.string.cart, R.drawable.selector_icon_cart);
        Tab tab_mine = new Tab(MineFragment.class, R.string.mine, R.drawable.selector_icon_mine);

        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_category);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);

        mInflater = LayoutInflater.from(this);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtablcontent);

        for (Tab tab : mTabs) {

            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTitle()));

            tabSpec.setIndicator(buildIndicator(tab));

            mTabHost.addTab(tabSpec, tab.getFragment(), null);
        }

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

                if(tabId==getString(R.string.cart)){
                    mMyToolbar.setVisibility(View.VISIBLE);
                    refData();

                } else if(tabId==getString(R.string.mine)){
                    mMyToolbar.setVisibility(GONE);

                }else {
                    mMyToolbar.setVisibility(View.VISIBLE);
                    mMyToolbar.showSearchView();
                    mMyToolbar.hideTitleView();
                    mMyToolbar.getRightButton().setVisibility(GONE);

                }
            }
        });

        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        mTabHost.setCurrentTab(0);
    }

    private void refData() {

        if (cartFragment == null) {

            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));

            if (fragment != null) {

                cartFragment = (CartFragment) fragment;

                cartFragment.refData();
                cartFragment.changeToolbar();
            }
        } else {
            cartFragment.refData();
            cartFragment.changeToolbar();
        }
    }

    private View buildIndicator(Tab tab) {

        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = (ImageView) view.findViewById(R.id.icon_tab);
        TextView text = (TextView) view.findViewById(R.id.txt_indicator);

        img.setBackgroundResource(tab.getIcon());
        text.setText(tab.getTitle());

        return view;
    }
}
