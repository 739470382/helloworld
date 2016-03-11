package com.bravesoft.riumachi.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.activity.MainActivity;
import com.bravesoft.riumachi.adapter.VasHaqAdapter;
import com.bravesoft.riumachi.bean.HaqBean;
import com.bravesoft.riumachi.bean.HaqVasBean;
import com.bravesoft.riumachi.bean.VasBean;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.database.DBHelper;
import com.bravesoft.riumachi.database.VasDBOperator;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.util.DateFormatUtil;

public class VasHaqFragment extends BaseFragment {

	private View currentView;
	private TextView vashaq_time, vashaq_data;
	private RelativeLayout vashaq_lin;
	private ImageView vashaq_img;
	private RelativeLayout item_height;
	private ListView vas_haq_list;
	private VasHaqAdapter vasHaqAdapter;
	private TextView txtNoMesTip;
	private View mLine;
	private SQLiteDatabase db;

	List<HaqVasBean> mlHaqVasBean = null;

	// List<HaqBean> mlHaqBean = null;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		db = ((MainActivity) getActivity()).getDb();
		currentView = inflater.inflate(R.layout.fragment_vas_haq, container,
				false);

		vas_haq_list = (ListView) currentView.findViewById(R.id.vas_haq_list);
		txtNoMesTip = (TextView) currentView.findViewById(R.id.txt_no_message);
		mLine = currentView.findViewById(R.id.line);
		LayoutUtils.setTextSize(txtNoMesTip, 25,
				TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
		LayoutUtils.rateScale(getActivity(), txtNoMesTip, true);

		getdataFromDB();

		if (mlHaqVasBean.size() == 0) {

			vas_haq_list.setVisibility(View.INVISIBLE);
			mLine.setVisibility(View.INVISIBLE);
			txtNoMesTip.setVisibility(View.VISIBLE);
		} else {
			vas_haq_list.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
			txtNoMesTip.setVisibility(View.INVISIBLE);
			List<HaqVasBean> newLst = new ArrayList<HaqVasBean>();
			newLst = getNewList(mlHaqVasBean);
			vasHaqAdapter = new VasHaqAdapter(getActivity(), newLst);
			vas_haq_list.setAdapter(vasHaqAdapter);
		}

		return currentView;
	}

	@Override
	public void onResume() {
		getdataFromDB();
         
		//if the data's length eauals 0
		if (mlHaqVasBean.size() == 0) {

			vas_haq_list.setVisibility(View.INVISIBLE);
			mLine.setVisibility(View.INVISIBLE);
			txtNoMesTip.setVisibility(View.VISIBLE);
		} 
		else {
			vas_haq_list.setVisibility(View.VISIBLE);
			mLine.setVisibility(View.VISIBLE);
			txtNoMesTip.setVisibility(View.INVISIBLE);
			List<HaqVasBean> newLst = new ArrayList<HaqVasBean>();
			newLst = getNewList(mlHaqVasBean);
			vasHaqAdapter = new VasHaqAdapter(getActivity(), newLst);
			vas_haq_list.setAdapter(vasHaqAdapter);
		}

		super.onResume();
	}

	/**
	 * get data form db
	 */
	private void getdataFromDB() {
		mlHaqVasBean = new ArrayList<HaqVasBean>();
		mlHaqVasBean = VasDBOperator.queryVasListOrderByDate(db);

	}

	/** 
	 * Sequence the  haqvas data，get a new list
	 * @param mlHaqVasBean
	 * @return
	 */
	private List<HaqVasBean> getNewList(List<HaqVasBean> mlHaqVasBean) {
		List<HaqVasBean> newLst = new ArrayList<HaqVasBean>();
		int length = mlHaqVasBean.size();
		int flag = 0;
		for (int i = 0; i < length; i++) {
			flag = i + 1;
			if (flag < length
					&& Integer.parseInt(DateFormatUtil.getStringData(
							Long.parseLong(mlHaqVasBean.get(i).getDateNo()))
							.replace("-", "")) == Integer
							.parseInt(DateFormatUtil.getStringData(
									Long.parseLong(mlHaqVasBean.get(flag)
											.getDateNo())).replace("-", ""))) {
				if (mlHaqVasBean.get(i).getType().equals("1")) {
					newLst.add(mlHaqVasBean.get(i));
					newLst.add(mlHaqVasBean.get(flag));
					i++;
				} else if (mlHaqVasBean.get(i).getType().equals("2")) {
					newLst.add(mlHaqVasBean.get(flag));
					newLst.add(mlHaqVasBean.get(i));
					i++;
				}
			} else {
				newLst.add(mlHaqVasBean.get(i));
			}

		}

		return newLst;
	}

	@Override
	protected View findViewById(int id) {
		return currentView;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
