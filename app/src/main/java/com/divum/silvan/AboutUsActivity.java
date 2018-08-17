package com.divum.silvan;

import com.divum.constants.GetAPI;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutUsActivity extends Activity{

	private String content ="Silvan Innovation Labs (Silvan) incorporated in 2008 is now in revenue and poised for a"+
	"market break-through in the Home Automation and Security markets. Silvan understands that today�s demanding"+
			"lifestyles need intelligent homes and facilities. Silvan application helps the user to control their home on"+
	"the go. From anywhere, the user can control the lights,fans,curtains, ac, tv, stb, door controls and have access to"+
			"the surveillance cameras. They can also control the home via mood configuration and active the house in a single click.";
	boolean aboutusvalue=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.aboutus_view);
		TextView txt_header = (TextView) findViewById(R.id.txt_header);
		txt_header.setText("About us");
		
		ImageView img_slider=(ImageView) findViewById(R.id.img_slider);
		img_slider.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				aboutusvalue=false;
				finish();
			}
		});
		
		
		TextView txtAbout=(TextView) findViewById(R.id.txtAbout);
		txtAbout.setText(content);
		
		TextView txtIP =(TextView)findViewById(R.id.txtIP);
		String ipaddr= GetAPI.BASE_URL.replace("http://", "");
		txtIP.setText(ipaddr);
		
		TextView txtIPType=(TextView)findViewById(R.id.txtIPType);
		SharedPreferences storagePref=getSharedPreferences("IP", MODE_PRIVATE);
		txtIPType.setText(storagePref.getString("ipType", ""));
	}
	
	@Override
	protected void onPause() {
		App_Variable.appMinimize=0;
		CustomLog.show("Log", "pause:" + App_Variable.appMinimize);
		SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
		boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
		if(checkvalue) {
		 if(aboutusvalue)
	       {
	         Intent intent = new Intent(this, UnlockActivity.class);
	         startActivity(intent);

          }
		}
		super.onPause();
	}

	@Override
	protected void onResume() {
		App_Variable.appMinimize=1;
		CustomLog.show("Log", "resume:"+App_Variable.appMinimize);

		super.onResume();
	}
	public boolean onKeyDown(int keyCode, KeyEvent event){
		aboutusvalue=false;
		finish();
		return super.onKeyDown(keyCode, event);
	}
}
