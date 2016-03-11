package com.bravesoft.riumachi.listener;

import android.text.Editable;
import android.text.TextWatcher;

class EditChangedListener implements TextWatcher {  
    private CharSequence temp;//Text before listening
    private int editStart;//Cursor start position
    private int editEnd;//End cursor position 
    
	@Override
	public void afterTextChanged(Editable s) {
		
	}
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}
	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}  

  

    } 
