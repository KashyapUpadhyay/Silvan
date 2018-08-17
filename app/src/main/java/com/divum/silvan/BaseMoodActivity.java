package com.divum.silvan;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.divum.utils.CustomLog;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

public class BaseMoodActivity extends SlidingFragmentActivity {

	private int mTitleRes;
	protected SlideViewer mFrag;
	SlidingMenu sm ;



	public BaseMoodActivity(int titleRes) {
		mTitleRes = titleRes;
		CustomLog.debug("base activity::::"+mTitleRes);

	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		try{
			super.onCreate(savedInstanceState);

			setTitle(mTitleRes);

			// set the Behind View
			setBehindContentView(R.layout.slide_menu);
			if (savedInstanceState == null) {
				CustomLog.debug("BaseActivity null");
				FragmentTransaction frag = this.getSupportFragmentManager().beginTransaction();
				mFrag = new SlideViewer("mood");
				frag.replace(R.id.menu_slide, mFrag);
				frag.commit();
			} else {
				CustomLog.debug("BaseActivity not null");
				mFrag = (SlideViewer)this.getSupportFragmentManager().findFragmentById(R.id.menu_slide);
			}


			// customize the SlidingMenu
			sm = getSlidingMenu();
			sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			sm.setMode(SlidingMenu.LEFT);
			sm.setShadowWidthRes(R.dimen.shadow_width);
			sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);	
			sm.setFadeDegree(0.35f);//0.35f
			sm.setBehindScrollScale(0.25f);
			sm.toggle();
		}catch (Exception e) {
			System.out.println("base mood activity activity saveinstace:"+e);
		}

	}


	public void ShowView(int position, String text, FragmentActivity activity) {

		if(activity instanceof HomeActivity){
			((HomeActivity) activity).ShowViewSection(position,text);
		}else if(activity instanceof MoodActivity){
			((MoodActivity)activity).ShowViewSection(position, text);
		}
		else{
			Intent intent=new Intent(activity,HomeActivity.class);	
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra("position", position);
			intent.putExtra("room", text);
			startActivity(intent);
			finish();
		}

	}

	/*public void clearData() {
		SharedPreferences pref_profile=getSharedPreferences("Register", MODE_PRIVATE);
		SharedPreferences.Editor editor =pref_profile.edit();
		editor.putString("status", "");
		editor.putString("dealer_id", "");
		editor.putString("user_id", "");
		editor.commit();
		AddValuesfromSigning.user_id = "";
		AddValuesfromSigning.dealer_id = "";
		final Intent i = new Intent(BaseActivity.this, MainActivity.class);
		startActivity(i);
		finish();
	}*/




}
