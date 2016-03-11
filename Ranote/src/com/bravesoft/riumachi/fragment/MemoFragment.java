package com.bravesoft.riumachi.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.provider.Contacts.People;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.EditSmyptomActivity;
import com.bravesoft.riumachi.activity.MainActivity;
import com.bravesoft.riumachi.activity.ScheduleDetailActivity;
import com.bravesoft.riumachi.adapter.MemoSymptomAdapter;
import com.bravesoft.riumachi.bean.SymptomMemoBean;
import com.bravesoft.riumachi.constant.SymptomTagConfig;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class MemoFragment extends BaseFragment {

	private View currentView;
	private TextView promtTextview;
	private SQLiteDatabase db;
	List<SymptomMemoBean> datalist;
	private MemoSymptomAdapter adapter;
	private ListView listview;
	
	private View mLine;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		db = ((MainActivity)getActivity()).getDb();
		// db = mDbHelper.getWritableDatabase();
		datalist = new ArrayList<SymptomMemoBean>();
		datalist = DBOperator.querySymptomMemoListOrderByDate(db);

		currentView = inflater
				.inflate(R.layout.fragment_memo, container, false);

		listview = (ListView) currentView.findViewById(R.id.symptom_ListView);
		LayoutUtils.rateScale(getActivity(), listview, true);
		promtTextview = (TextView) currentView.findViewById(R.id.prompt_user);
		LayoutUtils.setTextSize(promtTextview, 25 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.rateScale(getActivity(), promtTextview, true);
		mLine = currentView.findViewById(R.id.line);
		adapter = new MemoSymptomAdapter(getActivity(), datalist);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// Remove item
				// deletelistitem(position);

				// Presentation page
				// datalist = adapter.datalist;
				Intent intent = new Intent(getActivity(),
						EditSmyptomActivity.class);
				intent.putExtra(SymptomTagConfig.SMYPTOM_ID, datalist.get(position).getId() + "");

				getActivity().startActivity(intent);
				getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
			}

		});

		if (datalist != null && datalist.size() != 0) {

			listview.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);

			promtTextview.setVisibility(View.GONE);
		} else {
			promtTextview.setVisibility(View.VISIBLE);
			listview.setVisibility(View.INVISIBLE);
			mLine.setVisibility(View.INVISIBLE);
		}

		return currentView;
	}

	@Override
	protected View findViewById(int id) {
		return currentView;
	}

	@Override
	public void onResume() {

		datalist = DBOperator.querySymptomMemoListOrderByDate(db);
		if (datalist != null && datalist.size() != 0) {

			promtTextview.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
			if (adapter.datalist != null) {
				adapter.datalist.clear();
			} else {
				adapter.datalist = new ArrayList<SymptomMemoBean>();
			}
			adapter.datalist.addAll(datalist);
			adapter.notifyDataSetChanged();
		} else {
			promtTextview.setVisibility(View.VISIBLE);
			listview.setVisibility(View.INVISIBLE);
			mLine.setVisibility(View.INVISIBLE);
		}

		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	// Delete a record
	public void deletelistitem(int position) {

		DBOperator.deleteSymptomMemoById(db, datalist.get(position).getId()
				+ "");
		if (datalist.get(position).getLocal_path() != null) {
			File tempFile = new File(datalist.get(position).getLocal_path());
			tempFile.delete();
		}
		adapter.datalist.remove(position);
		if (adapter.datalist.size() == 0) {
			promtTextview.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
			mLine.setVisibility(View.INVISIBLE);
		}

		adapter.notifyDataSetChanged();
	}

}
