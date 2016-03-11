package com.bravesoft.riumachi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.CreateMyCardActivity;
import com.bravesoft.riumachi.activity.MainActivity;
import com.bravesoft.riumachi.activity.MyKarteDetailActivity;
import com.bravesoft.riumachi.adapter.MyKarteAdapter;
import com.bravesoft.riumachi.bean.MyKarteBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.DBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;

public class MyKarteFragment extends BaseFragment implements
		OnItemClickListener {

	private View currentView;
	private ListView mMyKarteListView;
	private TextView mTxtNoRecord;
	private MyKarteAdapter mMyKarteAdapter;
	private SQLiteDatabase db;
	List<MyKarteBean> mKarteBeans;
	private static Typeface mTypefacHansansRegular;
	private static Typeface mTypefacHansansBold;
	private static Typeface mTypefacRobotoRegular;
	private static Typeface mTypefacRobotoBold;

	private View mLine;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		db = ((MainActivity) getActivity()).getDb();
		currentView = inflater.inflate(R.layout.fragment_mykarte, container,
				false);
		mMyKarteListView = (ListView) currentView
				.findViewById(R.id.list_my_karte);
		mTxtNoRecord = (TextView) currentView
				.findViewById(R.id.txt_my_karte_no_record);

		mLine = currentView.findViewById(R.id.line);
		LayoutUtils.setTextSize(mTxtNoRecord, 25  ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.rateScale(getActivity(), mTxtNoRecord, true);
		LayoutUtils.rateScale(getActivity(), mMyKarteListView, true);
		mMyKarteListView.setOnItemClickListener(this);
		mMyKarteAdapter = new MyKarteAdapter(getActivity(), null);
		mMyKarteListView.setAdapter(mMyKarteAdapter);
		setContent();
		return currentView;
	}

	private void setContent() {
		if (mKarteBeans != null && mKarteBeans.size() > 0) {
			mTxtNoRecord.setVisibility(View.GONE);
		} else {
			mTxtNoRecord.setVisibility(View.VISIBLE);
		}
		mMyKarteAdapter.setData(mKarteBeans);
	}

	private List<MyKarteBean> getData() {
		return DBOperator.queryMyKarteListOrderByDate(db);
	}

	@Override
	protected View findViewById(int id) {
		return currentView;
	}

	@Override
	public void onResume() {
		mKarteBeans = getData();
		setContent();
		super.onResume();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		MyKarteDetailActivity.startMyCardDetailActivity(getActivity(),
				mMyKarteAdapter.getItem(position).getId() + "");
		getActivity().overridePendingTransition(R.anim.zoomin, R.anim.zoomout);
	}
}
