package com.divum.silvan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;


public class PinsettingActivity extends Activity implements View.OnClickListener {
private CheckBox checkok;
    private Button setbutton;
    boolean pinvalue=true;
    private String tag="PinSettingActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pinsetting);
        TextView txt_header = (TextView) findViewById(R.id.txt_header);
        txt_header.setText("Pincode settings");
        ImageView img_slider=(ImageView) findViewById(R.id.img_slider);
        img_slider.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                pinvalue = false;
                finish();
            }
        });

        checkok = (CheckBox) findViewById(R.id.checkBox);
        setbutton=(Button)findViewById(R.id.SetButton);
        SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", MODE_PRIVATE);
        boolean b=sharedPreferences.getBoolean("checkboxvalue",false);
        checkok.setOnClickListener(this);
        setbutton.setOnClickListener(this);
        if (b==false){
            checkok.setChecked(false);
        }
        else
        {
            checkok.setChecked(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pinsetting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {

            case R.id.checkBox:

                if(checkok.isChecked())
                {
                        SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("checkboxvalue", true);
                        editor.commit();

                }
                else
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("checkboxvalue", false);
                    editor.commit();
                }
                break;
            case R.id.SetButton:
                pinvalue=false;
                Intent in=new Intent(PinsettingActivity.this,ModifyPinActivity.class);
                startActivity(in);

                break;
        }

    }
    protected void onResume() {
        App_Variable.appMinimize=1;
        CustomLog.show(tag, "resume:"+App_Variable.appMinimize);
        pinvalue=true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        App_Variable.appMinimize=0;
        CustomLog.show(tag, "pause:" + App_Variable.appMinimize);
        CustomLog.show(tag, "ranjith time pinsettings pause" +pinvalue);
        SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
        boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
        if(checkvalue) {

            if(pinvalue) {
            Intent intent = new Intent(this, UnlockActivity.class);
            startActivity(intent);
        }
        }
        super.onPause();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        pinvalue=false;
        finish();
        return super.onKeyDown(keyCode, event);
    }


}
