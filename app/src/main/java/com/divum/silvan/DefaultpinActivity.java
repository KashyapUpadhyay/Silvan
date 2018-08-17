package com.divum.silvan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class DefaultpinActivity extends Activity {
    private Button Default_btnok;
    private EditText Default_edittext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_defaultpin);
        Default_btnok=(Button)findViewById(R.id.Default_btnok);
        Default_edittext=(EditText)findViewById(R.id.Default_edittext);
        Default_btnok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Default_edittext.getText().toString().equals("1234")) {
                    Intent in = new Intent(DefaultpinActivity.this, PinMainActivity.class);
                    startActivity(in);
                   finish();
                }
                else
                {
                    Toast.makeText(DefaultpinActivity.this,"enter corrct passcode",Toast.LENGTH_SHORT).show();
                }
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
