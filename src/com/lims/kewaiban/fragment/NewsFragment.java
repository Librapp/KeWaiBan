package com.lims.kewaiban.fragment;

import java.util.LinkedList;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.handmark.pulltorefresh.extras.listfragment.PullToRefreshListFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.lims.kewaiban.bean.News;

/**
 * @author Luke
 * @date 2015年7月28日上午10:11:27
 * @description
 */
public class NewsFragment extends PullToRefreshListFragment implements
		OnRefreshListener2<ListView> {
	private LinkedList<News> mListItems;
	private ArrayAdapter<News> mAdapter;

	private PullToRefreshListView mPullRefreshListView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPullRefreshListView = getPullToRefreshListView();
		mPullRefreshListView.setOnRefreshListener(this);
		ListView actualListView = mPullRefreshListView.getRefreshableView();

		mListItems = new LinkedList<News>();
		actualListView.setAdapter(mAdapter);
		setListShown(true);
	}

	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		// TODO Auto-generated method stub

	}

}
