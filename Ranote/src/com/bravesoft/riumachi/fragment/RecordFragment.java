package com.bravesoft.riumachi.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.constant.RecordFragmentTag;
import com.bravesoft.riumachi.layout.LayoutUtils;

@SuppressLint("ValidFragment")
public class RecordFragment extends BaseFragment implements RecordFragmentTag,
		OnClickListener {

	private View currentView;
	private static final String CURRENT_FRAGMENT_TAG = "current_fragment_tag";
	private String mCurrentFragmentTag;
	private List<RadioButton> mGuideTabs; // Button list
	private RadioButton mImgMyKarteTab; // mykarte Button
	private RadioButton mImgMemoTab; // memo  Button
	private RadioButton mImgVasHaqTab; // vas_haq  Button
	private RadioButton mCurrentTab; // current  Button
	private SparseArray<Fragment> mRecordFragmentMap; // Record fragment
	private Fragment mRecentlyFragment; // current  Fragment
	private MyKarteFragment myKarteFragment;  
	private MemoFragment mMemoFragment; 
	private VasHaqFragment mVasHaqFragment;  
	private RecordFragmentListener mRecordFragmentListener;

	public RecordFragment() {

	}

	public RecordFragment(RecordFragmentListener recordFragmentListener) {
		this.mRecordFragmentListener = recordFragmentListener;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		mGuideTabs = new ArrayList<RadioButton>();
		mRecordFragmentMap = new SparseArray<Fragment>();
		super.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		currentView = inflater.inflate(R.layout.fragment_record, null);
		initView();
		setSelectTab(VAS_HAQ_FRAGMENT);
		setSelectTab(MEMO_FRAGMENT);
		setSelectTab(MYKARTE_FRAGMENT);
		if (mRecordFragmentListener != null) {
			mRecordFragmentListener.OnChildPageChange(0,
					getString(R.string.tab_my_card_title));
		}
		return currentView;
	}

	private void initView() {
		/*Bound control */
		mImgMyKarteTab = (RadioButton) currentView
				.findViewById(R.id.radio_btn_mykarte_tab);
		mImgMemoTab = (RadioButton) currentView
				.findViewById(R.id.radio_btn_memo_tab);
		mImgVasHaqTab = (RadioButton) currentView
				.findViewById(R.id.radio_btn_vas_haq_tab);
		LayoutUtils.rateScale(getActivity(),
				currentView.findViewById(R.id.horizotal_line_one), true);

		/* Settings tab */
		mImgMyKarteTab.setTag(MYKARTE_FRAGMENT);
		mImgMemoTab.setTag(MEMO_FRAGMENT);
		mImgVasHaqTab.setTag(VAS_HAQ_FRAGMENT);

		/* Join Tab Group */
		mGuideTabs.add(mImgMyKarteTab);
		mGuideTabs.add(mImgMemoTab);
		mGuideTabs.add(mImgVasHaqTab);

		/* The main interface is the incoming fragment the map */
		myKarteFragment = new MyKarteFragment();
		mMemoFragment = new MemoFragment();
		mVasHaqFragment = new VasHaqFragment();
		mRecordFragmentMap = new SparseArray<Fragment>();
		mRecordFragmentMap.put(R.id.radio_btn_mykarte_tab, myKarteFragment);
		mRecordFragmentMap.put(R.id.radio_btn_memo_tab, mMemoFragment);
		mRecordFragmentMap.put(R.id.radio_btn_vas_haq_tab, mVasHaqFragment);
		
		/* init  Fragment */
//		FragmentTransaction transaction = getActivity().getSupportFragmentManager()
//				.beginTransaction();
//		transaction.add(R.id.framelayout_record,  mVasHaqFragment ,mImgVasHaqTab.getTag().toString());
//		transaction.add(R.id.framelayout_record, mMemoFragment, mImgMemoTab.getTag().toString());
//		transaction.add(R.id.framelayout_record, myKarteFragment, mImgMyKarteTab.getTag().toString());
//		transaction.commit();
//		mRecentlyFragment = myKarteFragment;

		 
		LayoutUtils.rateScale(getActivity(), mImgMyKarteTab, true);
		LayoutUtils.rateScale(getActivity(), mImgMemoTab, true);
		LayoutUtils.rateScale(getActivity(), mImgVasHaqTab, true);

		 
		mImgMyKarteTab.setOnClickListener(this);
		mImgMemoTab.setOnClickListener(this);
		mImgVasHaqTab.setOnClickListener(this);

	}

	/* Select the tab */
	private void setSelectTab(String guideTab) {
		if (mCurrentTab != null && mCurrentTab.getTag().toString() == guideTab) {
			return;
		}
		for (RadioButton tab : mGuideTabs) {
			if (tab.getTag().toString().equals(guideTab)) {
				tab.setChecked(true);
				replaceFragment(mRecordFragmentMap.get(tab.getId()), tab
						.getTag().toString());
				mCurrentTab = tab;
			} else {
				tab.setChecked(false);
			}
		}
	}

	/*
	 * Setting fragment
	 */
	public void replaceFragment(final Fragment fragment, String tag) {
		System.out.println("call me==="+tag);
		int count = getActivity().getSupportFragmentManager()
				.getBackStackEntryCount();
		for (int i = 0; i < count; i++) {
			getActivity().getSupportFragmentManager().popBackStack();
		}
		FragmentTransaction transaction = getActivity()
				.getSupportFragmentManager().beginTransaction();

	 



		if (fragment.isAdded()) {// It has been added to determine whether to deal with multiple clicks Status
			if (fragment != mRecentlyFragment && mRecentlyFragment != null) {
					transaction.hide(mRecentlyFragment);
					mRecentlyFragment = fragment;
					transaction.show(fragment);
			} else {
				transaction.hide(mMemoFragment);
			}
		} else {
			if (mRecentlyFragment != null) {
				transaction.hide(mRecentlyFragment);
			}
			mRecentlyFragment = fragment;
			transaction.add(R.id.framelayout_record, fragment, tag);
		}
		transaction.commitAllowingStateLoss();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.radio_btn_mykarte_tab:
			setSelectTab(MYKARTE_FRAGMENT);
			if (mRecordFragmentListener != null) {
				mRecordFragmentListener.OnChildPageChange(0,
						getString(R.string.tab_my_card_title));
			}
			break;
		case R.id.radio_btn_memo_tab:
			setSelectTab(MEMO_FRAGMENT);
			if (mRecordFragmentListener != null) {
				mRecordFragmentListener.OnChildPageChange(1,
						getString(R.string.tab_memo_title));
			}
			break;
		case R.id.radio_btn_vas_haq_tab:
			setSelectTab(VAS_HAQ_FRAGMENT);
			if (mRecordFragmentListener != null) {
				mRecordFragmentListener.OnChildPageChange(2,
						getString(R.string.tab_vas_haq_title));
			}
			break;

		default:
			break;
		}
	}

	@Override
	protected View findViewById(int id) {
		return currentView;
	}

	public interface RecordFragmentListener {
		public void OnChildPageChange(int position, String title);
	}

	/**
	 * Refresh Fragment interface
	 */
	public void reFrashViewMainTitle(){
		if (mRecentlyFragment != null) {
			if (mRecentlyFragment.getTag().equals(MYKARTE_FRAGMENT)) {
				mRecordFragmentListener.OnChildPageChange(0,
						getString(R.string.tab_my_card_title));
			}else if (mRecentlyFragment.getTag().equals(MEMO_FRAGMENT)) {
				mRecordFragmentListener.OnChildPageChange(1,
						getString(R.string.tab_memo_title));
				((MemoFragment)mRecentlyFragment).onResume();
			}else if (mRecentlyFragment.getTag().equals(VAS_HAQ_FRAGMENT)) {
				mRecordFragmentListener.OnChildPageChange(2,
						getString(R.string.tab_vas_haq_title));
				((VasHaqFragment)mRecentlyFragment).onResume();
			}
		}
	}
	
	/**
	 * show Memo Fragment
	 */
	public void showMyKarteFragment(){
		setSelectTab(MYKARTE_FRAGMENT);
		reFrashViewMainTitle();
	}
	
	/**
	 * show  Memo Fragment
	 */
	public void showMemoFragment(){
		setSelectTab(MEMO_FRAGMENT);
		reFrashViewMainTitle();
	}
	
	/**
	 * show  VasHaq Fragment
	 */
	public void showVasHaqFragment(){
		setSelectTab(VAS_HAQ_FRAGMENT);
		reFrashViewMainTitle();
	}
	
	@Override
	public void onResume() {
		
		super.onResume();
	}
}
