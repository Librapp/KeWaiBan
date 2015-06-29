package com.lims.kewaiban;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;

import android.app.Activity;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SearchView.OnQueryTextListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Luke
 *
 */
public class SearchResultActivity extends Activity implements
		OnItemClickListener, OnClickListener, OnCheckedChangeListener {
	private SearchView searchview;
	private RadioGroup category;
	private LinearLayout typeLayout, tabLayout, filterLayout, empty;
	private GridView typeGrid, tabGrid;
	private ListView listView;
	private TextView location;
	private RelativeLayout loading;

	private JSONArray typetxt, tabtxt;
	private String categorytxt = "all";
	private final String ordertxt = "all";
	private final String methodtxt = "desc";
	private String keyword;
	private int offset = 1;
	private final int count = 10;
	private int position = 0;
	private int typeCount = 0;
	private int tabCount = 0;
	private List<YouErYuanData> yydList;
	private List<TabData> yeyTypeList, zjTypeList, yeyTabList, zjTabList;
	private HashMap<String, String> type, tab;
	private TabAdapter tabAdapter, typeAdapter;
	private SearchTask searchTask;
	private Button btn;
	private boolean refresh = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		type = new HashMap<String, String>();
		tab = new HashMap<String, String>();
		setContentView(R.layout.search_activity);
		findViewById(R.id.addagent).setOnClickListener(this);
		findViewById(R.id.filter).setOnClickListener(this);
		filterLayout = (LinearLayout) findViewById(R.id.filter_layout);
		category = (RadioGroup) findViewById(R.id.category_radiogroup);
		category.setOnCheckedChangeListener(this);
		typeLayout = (LinearLayout) findViewById(R.id.type_layout);
		tabLayout = (LinearLayout) findViewById(R.id.tab_layout);
		typeGrid = (GridView) findViewById(R.id.type_grid);
		tabGrid = (GridView) findViewById(R.id.tab_grid);

		typeGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TabData data = (TabData) arg0.getItemAtPosition(arg2);
				if (data.selected) {
					data.selected = false;
					type.remove(data.name);
					typeCount--;
					if (typeCount == 0)
						type.clear();
					yydList = new ArrayList<YouErYuanData>();
					search(1, 10);
				} else {
					if (typeCount == 3) {
						Toast.makeText(SearchResultActivity.this, "最多只能选三项",
								Toast.LENGTH_SHORT).show();
					} else {
						data.selected = true;
						type.put(data.name, data.code);
						typeCount++;
						yydList = new ArrayList<YouErYuanData>();
						search(1, 10);
					}
				}
				typeAdapter.notifyDataSetChanged();
			}
		});
		tabGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TabData data = (TabData) arg0.getItemAtPosition(arg2);
				if (data.selected) {
					data.selected = false;
					tab.remove(data.name);
					tabCount--;
					if (tabCount == 0)
						tab.clear();
					yydList = new ArrayList<YouErYuanData>();
					search(1, 10);
				} else {
					if (tabCount == 3) {
						Toast.makeText(SearchResultActivity.this, "最多只能选三项",
								Toast.LENGTH_SHORT).show();
					} else {
						data.selected = true;
						tab.put(data.name, data.code);
						tabCount++;
						yydList = new ArrayList<YouErYuanData>();
						search(1, 10);
					}
				}
				tabAdapter.notifyDataSetChanged();
			}
		});
		btn = new Button(this);
		loading = (RelativeLayout) findViewById(R.id.loading);
		listView = (ListView) findViewById(R.id.search_list);
		listView.setOnItemClickListener(this);
		empty = (LinearLayout) findViewById(R.id.search_empty);
		findViewById(R.id.search_add).setOnClickListener(this);
		searchview = (SearchView) findViewById(R.id.searchview);
		searchview.setSubmitButtonEnabled(true);
		searchview.setOnQueryTextListener(new OnQueryTextListener() {

			@Override
			public boolean onQueryTextSubmit(String query) {
				keyword = query;
				yydList = new ArrayList<YouErYuanData>();
				search(offset, count);
				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				// TODO Auto-generated method stub
				return false;
			}
		});
		getActionBar().setDisplayHomeAsUpEnabled(true);
		findViewById(R.id.relocate).setOnClickListener(this);
		location = (TextView) findViewById(R.id.location);
		SharedPreferences sp = getSharedPreferences("default",
				Context.MODE_PRIVATE);
		MyApplication.locationData.name = sp.getString("locationName", "北京");
		MyApplication.locationData.code = sp.getString("locationCode", "131");
		MyApplication.locationData.level = sp.getInt("locationLevel", 1);
		location.setText(MyApplication.locationData.name);
		doSearchQuery(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) { // activity重新置顶
		super.onNewIntent(intent);
		doSearchQuery(intent);
	}

	// 对searchable activity的调用仍是标准的intent，我们可以从intent中获取信息，即要搜索的内容
	private void doSearchQuery(Intent intent) {
		if (intent == null)
			return;

		if (Intent.ACTION_SEARCH.equals(intent.getAction())) { // 如果是通过ACTION_SEARCH来调用，即如果通过搜索调用
			keyword = intent.getStringExtra(SearchManager.QUERY); // 获取搜索内容
			int category = intent.getIntExtra("type", 0);
			if (category == 0) {
				categorytxt = "all";
				((RadioButton) findViewById(R.id.category_all))
						.setChecked(true);
			} else if (category == 1) {
				categorytxt = "yey";
				((RadioButton) findViewById(R.id.category_youeryuan))
						.setChecked(true);
			} else if (category == 2) {
				categorytxt = "zjpx";
				((RadioButton) findViewById(R.id.category_zaojiao))
						.setChecked(true);
			}
			searchview.setQuery(keyword, false);
			yydList = new ArrayList<YouErYuanData>();
			search(offset, count);
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();
		searchView.setIconifiedByDefault(true);
		searchView.setSubmitButtonEnabled(true);
		searchView.setQueryRefinementEnabled(true);
		SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
		SearchableInfo info = searchManager
				.getSearchableInfo(getComponentName());
		searchView.setSearchableInfo(info);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void search(int offset, int count) {
		if (searchTask == null
				|| searchTask.getStatus() != AsyncTask.Status.RUNNING) {
			searchTask = new SearchTask();
			loading.setVisibility(View.VISIBLE);
			searchTask.execute(offset, count);
			MobclickAgent.onEvent(this, "Search");
		}
	}

	private class SearchTask extends AsyncTask<Object, String, String> {

		@Override
		protected String doInBackground(Object... params) {
			if (type.isEmpty()) {

			} else {
				typetxt = FormatUtil.hashMapToJsonArray(type);
			}

			if (tab.isEmpty()) {

			} else {
				tabtxt = FormatUtil.hashMapToJsonArray(tab);
			}
			String result = JSONParser.getYouErYuanList(NetUtil.executePost(
					SearchResultActivity.this, JSONRequest.search(keyword,
							typetxt, tabtxt, categorytxt, ordertxt, methodtxt,
							MyApplication.locationData.level,
							MyApplication.locationData.code,
							(Integer) params[0], (Integer) params[1]),
					JSONRequest.SEARCH), yydList);
			return result;
		}

		@Override
		protected void onPostExecute(String result) {
			loading.setVisibility(View.GONE);
			if (result.equals("")) {
				if (yydList.size() > 0) {
					listView.removeFooterView(btn);
					if (yydList.size() > position) {
						btn.setText(R.string.loadmore);
						btn.setBackgroundResource(R.drawable.btn_bg);
						btn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								position = yydList.size();
								offset++;
								search(offset, count);
							}
						});
						listView.addFooterView(btn);
					}
					AgentAdapter yeya = new AgentAdapter(
							SearchResultActivity.this, yydList);
					listView.setAdapter(yeya);
					listView.setSelection(position - 1);
					listView.setVisibility(View.VISIBLE);
					empty.setVisibility(View.GONE);
				} else {
					listView.setVisibility(View.GONE);
					empty.setVisibility(View.VISIBLE);
				}
			} else {
				listView.setVisibility(View.GONE);
				empty.setVisibility(View.VISIBLE);
			}
			super.onPostExecute(result);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		Intent i = new Intent(this, DetailActivity.class);
		i.putExtra("title", R.string.agentdetail);
		i.putExtra("id", yydList.get(arg2).id);
		startActivity(i);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.addagent:
			startActivity(new Intent(this, SecondaryActivity.class).putExtra(
					"title", R.string.addagent));
			break;
		case R.id.filter:
			if (filterLayout.isShown())
				filterLayout.setVisibility(View.GONE);
			else
				filterLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.relocate:
			refresh = true;
			startActivity(new Intent(this, UtilActivity.class).putExtra(
					"title", R.string.relocate));
			break;
		case R.id.search_add:
			startActivity(new Intent(this, SecondaryActivity.class).putExtra(
					"title", R.string.addagent));
			break;
		default:
			break;
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.category_all:
			tabLayout.setVisibility(View.GONE);
			typeLayout.setVisibility(View.GONE);
			categorytxt = "all";
			typeCount = 0;
			tabCount = 0;
			typetxt = null;
			tabtxt = null;
			break;
		case R.id.category_youeryuan:
			initYeyFilter();
			typeGrid.setAdapter(typeAdapter);
			tabGrid.setAdapter(tabAdapter);
			tabLayout.setVisibility(View.VISIBLE);
			typeLayout.setVisibility(View.VISIBLE);
			categorytxt = "yey";
			break;
		case R.id.category_zaojiao:
			initZjFilter();
			typeGrid.setAdapter(typeAdapter);
			tabGrid.setAdapter(tabAdapter);
			tabLayout.setVisibility(View.VISIBLE);
			typeLayout.setVisibility(View.VISIBLE);
			categorytxt = "zjpx";
			break;
		default:
			break;
		}
		yydList = new ArrayList<YouErYuanData>();
		search(offset, count);
	}

	private void initYeyFilter() {
		type = new HashMap<String, String>();
		tab = new HashMap<String, String>();
		typeCount = 0;
		tabCount = 0;
		typetxt = null;
		tabtxt = null;
		yeyTypeList = new ArrayList<TabData>();
		yeyTypeList.add(new TabData("公办示范", "gbshifan"));
		yeyTypeList.add(new TabData("公办一级", "gbyiji"));
		yeyTypeList.add(new TabData("公办二级", "gberji"));
		yeyTypeList.add(new TabData("公办不详", "gbbuxiang"));
		yeyTypeList.add(new TabData("民办示范", "mbshifan"));
		yeyTypeList.add(new TabData("民办一级", "mbyiji"));
		yeyTypeList.add(new TabData("民办二级", "mberji"));
		yeyTypeList.add(new TabData("民办不详", "mbbuxiang"));

		yeyTabList = new ArrayList<TabData>();
		yeyTabList.add(new TabData("外语", "外语"));
		yeyTabList.add(new TabData("小班", "小班"));
		yeyTabList.add(new TabData("艺术", "艺术"));
		yeyTabList.add(new TabData("蒙氏", "蒙氏"));
		yeyTabList.add(new TabData("国际", "国际"));
		yeyTabList.add(new TabData("全托", "全托"));
		yeyTabList.add(new TabData("混龄", "混龄"));
		typeAdapter = new TabAdapter(this, yeyTypeList);
		tabAdapter = new TabAdapter(this, yeyTabList);
	}

	private void initZjFilter() {
		type = new HashMap<String, String>();
		tab = new HashMap<String, String>();
		typeCount = 0;
		tabCount = 0;
		typetxt = null;
		tabtxt = null;
		zjTypeList = new ArrayList<TabData>();
		zjTypeList.add(new TabData("亲子", "qinzi"));
		zjTypeList.add(new TabData("才艺", "caiyi"));
		zjTypeList.add(new TabData("外语", "waiyu"));
		zjTypeList.add(new TabData("益智", "yizhi"));
		zjTypeList.add(new TabData("运动", "yundong"));
		zjTypeList.add(new TabData("综合", "zonghe"));
		zjTypeList.add(new TabData("其他", "qita"));

		zjTabList = new ArrayList<TabData>();
		zjTabList.add(new TabData("一对一", "一对一"));
		zjTabList.add(new TabData("寒暑班", "寒暑班"));
		zjTabList.add(new TabData("小班授课", "小班授课"));
		zjTabList.add(new TabData("周末班", "周末班"));
		zjTabList.add(new TabData("平日班", "平日班"));
		zjTabList.add(new TabData("外教", "外教"));
		zjTabList.add(new TabData("连锁", "连锁"));
		typeAdapter = new TabAdapter(this, zjTypeList);
		tabAdapter = new TabAdapter(this, zjTabList);
	}

	@Override
	public void onResume() {
		if (refresh) {
			refresh = false;
			location.setText(MyApplication.locationData.name);
			yydList = new ArrayList<YouErYuanData>();
			search(offset, count);
		}
		super.onResume();
	}

}