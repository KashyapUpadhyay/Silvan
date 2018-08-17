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
import android.widget.EditText;
import android.widget.Toast;

import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;


public class ModifyPinActivity extends Activity {
    private EditText oldpasscode;
    private EditText newpasscode;
    private Button btnok;
    private Button btncancel;
    private String tag="Modifypasscode Activity";
public boolean modifyvalue=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_pin);
        Modifypasscode();

    }
    public void Modifypasscode(){
       oldpasscode = (EditText) findViewById(R.id.EditText1);
        newpasscode = (EditText) findViewById(R.id.EditText2);
        btnok = (Button) findViewById(R.id.btnok);
        btncancel = (Button) findViewById(R.id.btncancel);
        btnok.setOnClickListener(new View.OnClickListener() {
            SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
            String passcode = SUGGESTION_PREF.getString("passcode", null);

            @Override
            public void onClick(View v) {
                if (oldpasscode.getText().toString().equals(passcode)) {
                    Toast.makeText(ModifyPinActivity.this, "valid", Toast.LENGTH_SHORT).show();
                    String passcode = newpasscode.getText().toString();
                    SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", true);
                    editor.putString("passcode", passcode);
                    editor.commit();
                    modifyvalue = false;
                    finish();
                } else {
                    Toast.makeText(ModifyPinActivity.this, "oldpasscode is wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modifyvalue = false;
                finish();

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_modify_pin, menu);
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
    protected void onPause() {
        App_Variable.appMinimize=0;
        //CustomLog.show(tag, "pause:" + App_Variable.appMinimize);
        SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
        boolean checkvalue=SUGGESTION_PREF.getBoolean("checkboxvalue", false);
        if(checkvalue) {

            if(modifyvalue) {
                Intent intent = new Intent(this, UnlockActivity.class);
                startActivity(intent);
            }}
        super.onPause();
    }
    protected void onResume() {
        App_Variable.appMinimize=1;
        CustomLog.show(tag, "resume:"+App_Variable.appMinimize);

        super.onResume();
    }
    public boolean onKeyDown(int keyCode, KeyEvent event){
        modifyvalue=false;
        finish();
        return super.onKeyDown(keyCode, event);
    }


}
