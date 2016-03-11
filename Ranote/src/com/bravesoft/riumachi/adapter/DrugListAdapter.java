package com.bravesoft.riumachi.adapter;

import java.util.ArrayList;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.bean.DrugBean;
import com.bravesoft.riumachi.bean.DrugGroup;
import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DrugListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<DrugGroup> group = new ArrayList<DrugGroup>();
	private ArrayList<ArrayList<DrugBean>> child = new ArrayList<ArrayList<DrugBean>>();
	private static int ISCHECK = 1;

	public DrugListAdapter(Context context, ArrayList<DrugGroup> group,
			ArrayList<ArrayList<DrugBean>> child) {
		this.context = context;
		this.group = group;
		this.child = child;
	}

	@Override
	public int getGroupCount() {
		return group.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return child.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return group.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return child.get(groupPosition).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolderGroup holderGroup;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_druglist_group, null);
			holderGroup = new ViewHolderGroup();
			holderGroup.group_name = (TextView) convertView
					.findViewById(R.id.textview_group);
			holderGroup.imageView=(ImageView) convertView.findViewById(R.id.pic_drug);
			holderGroup.item = (RelativeLayout) convertView.findViewById(R.id.relative_item);
			LayoutUtils.setTextSize(holderGroup.group_name, 24 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_BOLD);
			LayoutUtils.rateScale(context, holderGroup.imageView, true);
			LayoutUtils.rateScale(context,holderGroup.item, true);
			convertView.setTag(holderGroup);
		} else {
			holderGroup = (ViewHolderGroup) convertView.getTag();
		}
		holderGroup.group_name.setText(group.get(groupPosition).getName());
		holderGroup.imageView.setImageResource(group.get(groupPosition).getPic_drug());
		return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolderChild holderChild;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.item_druglist_child, null);
			holderChild = new ViewHolderChild();
			holderChild.child_name = (TextView) convertView
					.findViewById(R.id.textview_child);
			holderChild.iamge = (ImageView) convertView
					.findViewById(R.id.imageview_drug_ischeck);
			holderChild.child_line=convertView.findViewById(R.id.druglist_child_line);
			holderChild.layout=(RelativeLayout) convertView.findViewById(R.id.relative_druglist_change);
			LayoutUtils.setTextSize(holderChild.child_name, 34 ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
			LayoutUtils.rateScale(context, holderChild.iamge, true);
			LayoutUtils.rateScale(context, holderChild.child_line, true);
			convertView.setTag(holderChild);
		} else {
			holderChild = (ViewHolderChild) convertView.getTag();
		}
		
		if (child.get(groupPosition).get(childPosition).getIsShow()==2) {
				holderChild.child_line.setVisibility(View.VISIBLE);
		}else {
			holderChild.child_line.setVisibility(View.GONE);
		}
		
		
		if (child.get(groupPosition).get(childPosition).getIsCheck() == 1) {
			holderChild.iamge.setImageResource(R.drawable.check_off);
		} else {
			holderChild.iamge.setImageResource(R.drawable.check_on);
		}
		
		if (child.get(groupPosition).get(childPosition).getIsFoucs()==1) {
			holderChild.child_name.setTextColor(0xff9b9b9b);
			holderChild.iamge.setVisibility(View.GONE);
		}else if (child.get(groupPosition).get(childPosition).getIsFoucs()==2){
			holderChild.iamge.setVisibility(View.VISIBLE);
			holderChild.child_name.setTextColor(Color.BLACK);
			holderChild.iamge.setImageResource(R.drawable.icon_delete_small);
		}else if (child.get(groupPosition).get(childPosition).getIsFoucs()==3){
			holderChild.child_name.setTextColor(Color.BLACK);
			holderChild.iamge.setVisibility(View.VISIBLE);
		}
		holderChild.child_name.setText(child.get(groupPosition)
				.get(childPosition).getDrug_name());
		return convertView;
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

	class ViewHolderGroup {
		TextView group_name;
		RelativeLayout item;
		ImageView imageView;
	}

	class ViewHolderChild {
		TextView child_name;
		int img_ischeck;
		ImageView iamge;
		RelativeLayout layout;
		RelativeLayout item;
		View child_line;
	}

}
