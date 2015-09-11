package com.lims.kewaiban;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.SearchView;

import com.lims.kewaiban.fragment.AgentFragment;
import com.lims.kewaiban.fragment.LessonsFragment;
import com.lims.kewaiban.fragment.NewsFragment;
import com.lims.kewaiban.fragment.SchoolsFragment;
import com.lims.kewaiban.fragment.SelfFragment;

public class MainActivity extends FragmentActivity implements
		OnCheckedChangeListener, OnClickListener {
	private FragmentTabHost mTabHost;
	private RadioGroup radioGroup;
	private SearchView searchView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.realtabcontent, new NewsFragment()).commit();
		}
		// EventBus.getDefault().register(this);
		InitView();
	}

	private void InitView() {
		searchView = (SearchView) findViewById(R.id.sv_search);
		searchView.setOnClickListener(this);
		mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.getTabWidget().setVisibility(View.GONE); // 隐藏系统的TabWidget

		mTabHost.addTab(mTabHost.newTabSpec("news").setIndicator("资讯"),
				NewsFragment.class, null);

		mTabHost.addTab(mTabHost.newTabSpec("lessons").setIndicator("课程"),
				LessonsFragment.class, null);

		mTabHost.addTab(mTabHost.newTabSpec("agent").setIndicator("机构"),
				AgentFragment.class, null);

		mTabHost.addTab(mTabHost.newTabSpec("schools").setIndicator("学校"),
				SchoolsFragment.class, null);

		mTabHost.addTab(mTabHost.newTabSpec("self").setIndicator("个人中心"),
				SelfFragment.class, null);

		mTabHost.setCurrentTabByTag("news");
		((RadioButton) findViewById(R.id.rb_news)).setChecked(true);

		radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		FragmentManager fm = getSupportFragmentManager();
		NewsFragment news = (NewsFragment) fm.findFragmentByTag("news");
		LessonsFragment lessons = (LessonsFragment) fm
				.findFragmentByTag("lessons");
		AgentFragment agent = (AgentFragment) fm.findFragmentByTag("agent");
		SchoolsFragment schools = (SchoolsFragment) fm
				.findFragmentByTag("schools");
		SelfFragment self = (SelfFragment) fm.findFragmentByTag("self");

		FragmentTransaction ft = fm.beginTransaction();

		/** Detaches the androidfragment if exists */
		if (news != null)
			ft.detach(news);
		if (lessons != null)
			ft.detach(lessons);
		if (agent != null)
			ft.detach(agent);
		if (schools != null)
			ft.detach(schools);
		if (self != null)
			ft.detach(self);
		// EventBus.getDefault().post(null);
		switch (checkedId) {
		case R.id.rb_news:
			if (news == null) {
				ft.add(R.id.realtabcontent, new NewsFragment(), "news");
			} else {
				ft.attach(news);
			}
			mTabHost.setCurrentTabByTag("news");
			break;
		case R.id.rb_lessons:
			if (lessons == null) {
				ft.add(R.id.realtabcontent, new LessonsFragment(), "lessons");
			} else {
				ft.attach(lessons);
			}
			mTabHost.setCurrentTabByTag("lessons");
			break;
		case R.id.rb_agent:
			if (agent == null) {
				ft.add(R.id.realtabcontent, new AgentFragment(), "agent");
			} else {
				ft.attach(agent);
			}
			mTabHost.setCurrentTabByTag("agent");
			break;
		case R.id.rb_schools:
			if (schools == null) {
				ft.add(R.id.realtabcontent, new SchoolsFragment(), "schools");
			} else {
				ft.attach(schools);
			}
			mTabHost.setCurrentTabByTag("schools");
			break;
		case R.id.rb_self:
			if (self == null) {
				ft.add(R.id.realtabcontent, new SelfFragment(), "self");
			} else {
				ft.attach(self);
			}
			mTabHost.setCurrentTabByTag("self");
			break;
		}
		ft.commit();
	}

	@Override
	protected void onDestroy() {
		// EventBus.getDefault().unregister(this);// 反注册EventBus
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.sv_search:
			Intent iSearch = new Intent(MainActivity.this, SearchActivity.class);
			iSearch.putExtra("keyword", searchView.getQuery());
			startActivity(iSearch);
			break;
		default:
			break;
		}
	}
}
