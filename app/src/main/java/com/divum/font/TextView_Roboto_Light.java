package com.divum.font;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextView_Roboto_Light extends TextView{

	public TextView_Roboto_Light(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		createFont();
	}

	public TextView_Roboto_Light(Context context, AttributeSet attrs) {
		super(context, attrs);
		createFont();
	}

	public TextView_Roboto_Light(Context context) {
		super(context);
		createFont();
	}

	public void createFont() {
		Typeface font = Typeface.createFromAsset(getContext().getAssets(), "fonts/roboto_light.ttf");
		setTypeface(font);
	}

}
