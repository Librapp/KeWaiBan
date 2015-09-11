package com.lims.kewaiban.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lims.kewaiban.R;

/**
 * @author Luke
 * @date 2015年7月28日上午10:12:15
 * @description
 */
public class SchoolsFragment extends Fragment {
	private ListView lv;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.schools_fragment, container,
				false);
		lv = (ListView) view.findViewById(R.id.lv_schools);
		return view;
	}
}
