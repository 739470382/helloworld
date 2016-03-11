package com.bravesoft.riumachi.util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Paint;
import android.widget.TextView;

import com.bravesoft.riumachi.constant.TextTypeFace;
import com.bravesoft.riumachi.layout.LayoutUtils;
import com.bravesoft.riumachi.layout.ScreenConfig;

/**
 * @author wangyuanshi
 */
public class StringCalculateUtils {

	/**
	 * this method help you calculate a string to string array in appointed size.
	 * @param textSize      textview's size
	 * @param marginLeft  margin left 
	 * @param marginRight margin right
	 * @return
	 */
	public static List<String> getStringArray(Context context,String originalStr ,int textSize,int marginLeft , int marginRight) {
		List<String> data = new ArrayList<String>();
		TextView textView = new TextView(context);
		LayoutUtils.setTextSize(textView, textSize);
		marginLeft = LayoutUtils.getRate4px(marginLeft);
		marginRight = LayoutUtils.getRate4px(marginRight);
		Paint paint = textView.getPaint();
		int oneWordWidth = (int) paint.measureText("私");
		int twoWordWidth = (int) paint.measureText("私私");
		int oneWordWidthBig = (twoWordWidth - oneWordWidth*2)+oneWordWidth+1;
		int oneByteWordWidth = (twoWordWidth - oneWordWidth*2) + (int) paint.measureText("A");
		oneWordWidth = (int) paint.measureText("a");
		twoWordWidth = (int) paint.measureText("aa");
		int oneWordWidthLowerCase = (twoWordWidth - oneWordWidth*2)+oneWordWidth+1;
		oneWordWidth = (int) paint.measureText("A");
		twoWordWidth = (int) paint.measureText("AA");
		int oneWordWidthUpCase =  (twoWordWidth - oneWordWidth*2)+oneWordWidth+1;
		oneWordWidth = (int) paint.measureText("9");
		twoWordWidth = (int) paint.measureText("99");
		int oneWordWidthMumber =  (twoWordWidth - oneWordWidth*2)+oneWordWidth+1;
		oneWordWidth = (int) paint.measureText("@");
		twoWordWidth = (int) paint.measureText("@@");
		int oneWordtWidthPoint =  (twoWordWidth - oneWordWidth*2)+oneWordWidth+1;
		int oneLineBigLenght = (ScreenConfig.SCRREN_W-(marginLeft+marginRight))/ oneWordWidthBig;
		int oneLineWidth = (ScreenConfig.SCRREN_W-(marginLeft+marginRight));
		originalStr = ToDBC(originalStr);
		System.out.println("ScreenConfig.SCRREN_W=="+ScreenConfig.SCRREN_W);
		System.out.println("ScreenConfig.SCRREN_H=="+ScreenConfig.SCRREN_H);
		System.out.println("oneLineWidth=="+oneLineWidth);
		System.out.println("oneWordWidthBig=="+oneWordWidthBig);
		System.out.println("oneWordWidthLowerCase=="+oneWordWidthLowerCase);
		System.out.println("oneWordWidthUpCase=="+oneWordWidthUpCase);
		System.out.println("oneWordWidthMumber=="+oneWordWidthMumber);
		System.out.println("point=="+(int) paint.measureText("@"));
		if (originalStr.length() <= oneLineBigLenght && !originalStr.contains("\r") && !originalStr.contains("\n")) {
			data.add(originalStr);
		}else{
			char[] charArr = originalStr.toCharArray();
			StringBuffer stringBuffer = new StringBuffer();
			int lenght = 0;
			
			for (int i = 0; i < charArr.length; i++) {
				if (charArr[i] =='\r'||charArr[i] =='\n'){
					lenght = 0;
					data.add(stringBuffer.toString());
					stringBuffer.delete(0,stringBuffer.length());
				}else{
					String tempStr = charArr[i]+"";
					if (tempStr.getBytes().length < 2) {
						System.out.println("call me 1");
						if (isNumeric(tempStr)) {
							if (oneLineWidth - lenght <  oneWordWidthMumber ) {	
								lenght = 0;
								data.add(stringBuffer.toString());
								stringBuffer.delete(0,stringBuffer.length());
							}
							stringBuffer.append(charArr[i]);
							lenght+=oneWordWidthMumber;
						}else if (isLowerCase(tempStr)) {
							if (oneLineWidth - lenght <  oneWordWidthLowerCase ) {	
								lenght = 0;
								data.add(stringBuffer.toString());
								stringBuffer.delete(0,stringBuffer.length());
							}
							stringBuffer.append(charArr[i]);
							lenght+=oneWordWidthLowerCase;
						}else if (isUpperCase(tempStr)){
							if (oneLineWidth - lenght <  oneWordWidthUpCase ) {	
								lenght = 0;
								data.add(stringBuffer.toString());
								stringBuffer.delete(0,stringBuffer.length());
							}
							stringBuffer.append(charArr[i]);
							lenght+=oneWordWidthUpCase;
						}else{
							if (oneLineWidth - lenght <  oneWordtWidthPoint ) {	
								lenght = 0;
								data.add(stringBuffer.toString());
								stringBuffer.delete(0,stringBuffer.length());
							}
							stringBuffer.append(charArr[i]);
							lenght+=oneWordtWidthPoint;
						}
						
					}else {
						System.out.println("call me 2");
						if (oneLineWidth - lenght <  oneWordWidthBig) {
							lenght = 0;
							data.add(stringBuffer.toString());
							stringBuffer.delete(0,stringBuffer.length());
						}
						stringBuffer.append(charArr[i]);
						lenght+=oneWordWidthBig;
					}
				}
//				if (oneLineWidth - lenght <  oneWordWidthBig) {
//					data.add(stringBuffer.toString());
//					stringBuffer.delete(0,stringBuffer.length());
//					lenght = 0;
//				}
			}
			if (stringBuffer!=null || stringBuffer.length()>0) {
				data.add(stringBuffer.toString());
			}
		}
		return data;
	}
	
	/**
	 * this method help you calculate a string to string array in appointed size. 
	 * the result is better than getStringArray(),but the efficiency maybe slower than getStringArray().
	 * @param textSize    has been rate view textview's size
	 * @param marginLeft  margin left 
	 * @param marginRight margin right
	 * @return
	 */
	public static List<String> getSplitStringArray(Context context,String originalStr ,int textSize,int marginLeft , int marginRight) {
		List<String> data = new ArrayList<String>();
		TextView textView = new TextView(context);
		LayoutUtils.setTextSize(textView, textSize ,TextTypeFace.TYPEFACE_GENSHINGOTHIC_REGULAR);
		marginLeft = LayoutUtils.getRate4px(marginLeft);
		marginRight = LayoutUtils.getRate4px(marginRight);
		Paint paint = textView.getPaint();
		int oneLineWidth = (ScreenConfig.SCRREN_W-(marginLeft+marginRight));
		originalStr = ToDBC(originalStr);
		char[] charArr = originalStr.toCharArray();
		StringBuffer stringBuffer = new StringBuffer();
		int lenght = 0;
			
		for (int i = 0; i < charArr.length; i++) {
			if (charArr[i] =='\r'||charArr[i] =='\n'){
				lenght = 0;
				data.add(stringBuffer.toString());
				stringBuffer.delete(0,stringBuffer.length());
			}else{
				String tempStr = charArr[i]+"";
				int oneWordWidth = (int) paint.measureText(tempStr);
				int twoWordWidth = (int) paint.measureText(tempStr+tempStr);
				int oneWordWidthBig = (twoWordWidth - oneWordWidth*2)+oneWordWidth;
				if (oneLineWidth - lenght <  oneWordWidthBig ) {	
					lenght = 0;
					data.add(stringBuffer.toString());
					stringBuffer.delete(0,stringBuffer.length());
				}
				stringBuffer.append(charArr[i]);
				lenght+=oneWordWidthBig;
			}
		}
		if (stringBuffer!=null || stringBuffer.length()>0) {
			data.add(stringBuffer.toString());
		}
		return data;
	}

	public static boolean isNumeric(String str){ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	public static boolean isLowerCase(String str){ 
	    Pattern pattern = Pattern.compile("[a-z]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	public static boolean isUpperCase(String str){ 
	    Pattern pattern = Pattern.compile("[A-Z]*"); 
	    return pattern.matcher(str).matches();    
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
