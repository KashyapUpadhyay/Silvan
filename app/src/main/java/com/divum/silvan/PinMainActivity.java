package com.divum.silvan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//import android.support.v7.app.ActionBarActivity;
//import android.text.Editable;


public class PinMainActivity extends Activity {
    private EditText passcode1;
    private EditText passcode2;
    private Button btnok;
    private Button btncancel;
    private SplashActivity sa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pin_main);
        createpasscode();


    }

    public String var_passcode;

    public void createpasscode() {

        passcode1 = (EditText) findViewById(R.id.EditText1);
        passcode2 = (EditText) findViewById(R.id.EditText2);
        btnok = (Button) findViewById(R.id.btnok);
        btncancel = (Button) findViewById(R.id.btncancel);
        SplashActivity sa;
        btnok.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (passcode1.getText().toString().equals(passcode2.getText().toString())) {
                    Toast.makeText(PinMainActivity.this, "valid", Toast.LENGTH_SHORT).show();

                    var_passcode = passcode1.getText().toString();

                  /* Intent in = new Intent(PinMainActivity.this, SplashActivity.class);
                    in.putExtra("passcode", var_passcode);
                    startActivity(in);
                  */
                    SharedPreferences sharedPreferences = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean("value", true);
                  //  editor.putBoolean("isunlock",false);
                    editor.putString("passcode", var_passcode);
                    editor.commit();
                    Intent in=new Intent(PinMainActivity.this,SplashActivity.class);
                   // startActivity(in);
                   setResult(50,in);
                    finish();
                    //move to silvan
                } else {
                    Toast.makeText(PinMainActivity.this, "Passcode should match",
                            Toast.LENGTH_SHORT).show();

                }
            }

        });
        btncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PinMainActivity.this, "should set passcode",
                        Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_google, menu);
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


}