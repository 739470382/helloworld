package com.bravesoft.riumachi.activity;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.bravesoft.riumachi.R;
import com.bravesoft.riumachi.adapter.MyKarteDetailAdapter;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;
import com.bravesoft.riumachi.util.MyUtils;

public class EditTestActivity extends BaseActivity{
	
	private ListView mListView;
	private MyKarteDetailAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_edit_view);
		
		mListView = getRateView(R.id.list_edit, true);
		mAdapter = new MyKarteDetailAdapter(this, getStringArray(null,31 ,80 ,40));
		mListView.setAdapter(mAdapter);
		super.onCreate(savedInstanceState);
	}
	
	
	/**
	 * this method help you calculate a string to string array in appointed size.
	 * @param textSize    has been rate view textview's size
	 * @param marginLeft  margin left 
	 * @param marginRight margin right
	 * @return
	 */
	private List<String> getStringArray(String originalStr ,int textSize,int marginLeft , int marginRight) {
		List<String> data = new ArrayList<String>();
		TextView textView = new TextView(getApplicationContext());
		LayoutUtils.setTextSize(textView, textSize);
		marginLeft = LayoutUtils.getRate4px(marginLeft);
		marginRight = LayoutUtils.getRate4px(marginRight);
		int oneWordWidthBig = LayoutUtils.getRate4px(textView.getPaint().measureText("私"));
		int oneWordWidthSmall = LayoutUtils.getRate4px(textView.getPaint().measureText("a"));
		int oneLineBigLenght = (ScreenConfig.SCRREN_W-(marginLeft+marginRight))/ oneWordWidthBig;
		int oneLineSmallLenght = (ScreenConfig.SCRREN_W-(marginLeft+marginRight))/ oneWordWidthSmall;
		int oneLineWidth = (ScreenConfig.SCRREN_W-(marginLeft+marginRight));
		originalStr = ToDBC(getResources().getString(R.string.lbl_privacy_content3));
		System.out.println("oneWordWidthBig="+oneWordWidthBig+"=oneWordWidthSmall="+oneWordWidthSmall);
		System.out.println("oneLineWidth="+oneLineWidth);
			//System.out.println("oneLineSmallLenght="+oneLineSmallLenght+"=oneLineSmallLenght="+oneLineSmallLenght);
	//		originalStr = "私私私私私私私私私私私私私私私私私私私私";
		if (originalStr.length() <= oneLineBigLenght) {
			data.add(originalStr);
		}else{
			char[] charArr = originalStr.toCharArray();
			StringBuffer stringBuffer = new StringBuffer();
			int lenght = 0;
			
			for (int i = 0; i < charArr.length; i++) {
				//System.out.println("call me=="+charArr[i]);
				if (charArr[i] =='\r'||charArr[i] =='\n'){
					lenght = 0;
					data.add(stringBuffer.toString());
					stringBuffer.delete(0,stringBuffer.length());
				}else{
					String tempStr = charArr[i]+"";
					if (tempStr.getBytes().length < 2) {

						if (oneLineWidth - lenght <  oneWordWidthSmall ) {
							//System.out.println("oneLineWidth - lenght="+(oneLineWidth - lenght)+"=oneWordWidthSmall="+oneWordWidthSmall);
							lenght = 0;
							data.add(stringBuffer.toString());
							stringBuffer.delete(0,stringBuffer.length());
						}
						stringBuffer.append(charArr[i]);
						lenght+=oneWordWidthSmall;
					}else{
						if (oneLineWidth - lenght <  oneWordWidthBig) {
							//System.out.println("oneLineWidth - lenght="+(oneLineWidth - lenght)+"=oneWordWidthBig="+oneWordWidthBig);
							lenght = 0;
							data.add(stringBuffer.toString());
							stringBuffer.delete(0,stringBuffer.length());
						}
						stringBuffer.append(charArr[i]);
						lenght+=oneWordWidthBig;
					}
				}
				
				//System.out.println("oneLineWidth - lenght="+(oneLineWidth - lenght)+"=oneWordWidthBig="+oneWordWidthBig);
				if (oneLineWidth - lenght <  oneWordWidthBig) {
					data.add(stringBuffer.toString());
					stringBuffer.delete(0,stringBuffer.length());
					lenght = 0;
				}
			}
		}
		return data;
	}

	/**
	 * this method help you calculate a string to string array in appointed size.
	 * @param textSize    has been rate view textview's size
	 * @param marginLeft  margin left 
	 * @param marginRight margin right
	 * @return
	 */
	private List<String> getData(int textSize,int marginLeft , int marginRight) {
		TextView textView = new TextView(getApplicationContext());
		LayoutUtils.setTextSize(textView, textSize);
		marginLeft = LayoutUtils.getRate4px(marginLeft);
		marginRight = LayoutUtils.getRate4px(marginRight);
		int oneWordWidth = LayoutUtils.getRate4px(textView.getPaint().measureText("私"));
		int oneLineMaxLenght = (ScreenConfig.SCRREN_W-(marginLeft+marginRight))/ oneWordWidth;
		String originalStr = getResources().getString(R.string.lbl_privacy_content3);
//		String originalStr = "私私私私私私私私私私私私私私私私私私私私";
		List<String> data = new ArrayList<String>();
		if (!MyUtils.isNull(originalStr)) { // originalStr can't  be null
			if (originalStr.contains("\r")||originalStr.contains("\n")) {
				String arr[] = null;
				if (originalStr.contains("\r")) {
					arr = originalStr.split("\r");
				}else if (originalStr.contains("\n")) {
					arr = originalStr.split("\n");
				}
				if (arr != null && arr.length > 0) {
					//viewHolder.txtContent.setText(arr[0]);
				}
			}
			int size = originalStr.length()/oneLineMaxLenght;
			if (originalStr.length()%oneLineMaxLenght!=0) {
				size++;
			}
			System.out.println("oneLineMaxLenght=="+oneLineMaxLenght+"==originalStr.length=="+originalStr.length());
			if (originalStr.length() <= oneLineMaxLenght) {
				System.out.println("call me");
				data.add(originalStr);
			}else{
				for (int i = 0; i < size; i++) {
					if (i < size -1) {
						data.add(originalStr.substring(i*oneLineMaxLenght, (i+1)*oneLineMaxLenght));
					}if (i == size - 1) {
						data.add(originalStr.substring(i*oneLineMaxLenght));
					}
					
				}
			}
		}
		
		return data;
	}
	
	public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }
	
}
