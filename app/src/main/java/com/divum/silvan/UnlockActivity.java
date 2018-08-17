package com.divum.silvan;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;


public class UnlockActivity extends Activity {
    private Button btnunlock;
    private EditText edittext;
    private PinMainActivity Ma;
    private PopupWindow pw;
    SecretKeySpec sks = null;

    // private PopupWindow pwindo;
    //private LinearLayout layout;
   // Signout googleActivity=new Signout();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("its comming");
        setContentView(R.layout.activity_unlock);
        edittext = (EditText) findViewById(R.id.edittext);
        btnunlock = (Button) findViewById(R.id.btnunlock);
        final int[] count = {0};
        SharedPreferences SUGGESTION_PREF = getSharedPreferences("SUGGESTION_PREF", Context.MODE_PRIVATE);
        String passcode=SUGGESTION_PREF.getString("passcode", null);
        System.out.println("pass code:"+passcode);
        final byte Encode[]=AesEncreption(passcode);
        System.out.println(Encode);
        btnunlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Bundle extras = getIntent().getExtras();
                //byte[] ss= extras.getByteArray("username");
                //byte[] ss=getIntent().getByteArrayExtra("username");
               // System.out.println(ss);
                String Devar_passcode = Aesdecript(Encode);
                System.out.println(Devar_passcode);
                if (edittext.getText().toString().equals(Devar_passcode)){
                    Toast.makeText(UnlockActivity.this, "passcode correct", Toast.LENGTH_SHORT).show();
                    String page=getIntent().getStringExtra("pagetype");
                    Intent intent=new Intent();
                    //intent.putExtra("unlock",true);
                    intent.putExtra("page1",page);
                    setResult(10,intent);
                    finish();

                } else {
                    count[0] = count[0] + 1;
                    System.out.println(count[0]);
                    Toast.makeText(UnlockActivity.this, "please enter correct pin", Toast.LENGTH_SHORT).show();
                    // initiatePopupWindow();
                    if (count[0] == 5) {
                        Toast.makeText(UnlockActivity.this,"you should resign with google to set passcode",Toast.LENGTH_SHORT).show();
                        /*Intent in=new Intent(UnlockActivity.this,GoogleActivity.class);
                        in.putExtra("Request","logout");
                        startActivity(in);
                        *///Ga.signOutFromGplus();

                   }
                }

            }
        });


    }

   /* private void initiatePopupWindow() {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) UnlockActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View layout = inflater.inflate(R.layout.popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            // create a 300px width and 470px height PopupWindow
            pw = new PopupWindow(layout, 600, 470, true);
            // display the popup in the center
            pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

            //mResultText = (TextView) layout.findViewById(R.id.server_status_text);
            Button cancelButton = (Button) layout.findViewById(R.id.popupbtn);
            // makeBlack(cancelButton);
            cancelButton.setOnClickListener(cancel_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pw.dismiss();
        }
    };

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

    public String Aesdecript(byte Encode[]) {
        final String TAG = "SymmetricAlgorithmAES";
        /*SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
            //System.out.println("sks2"+sks);
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }*/
        byte[] decodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.DECRYPT_MODE, sks);
            System.out.println(Encode);
            decodedBytes = c.doFinal(Encode);
            System.out.println("decodedBytes"+decodedBytes);
            String decode=new String(decodedBytes);
            return decode;
        } catch (Exception e) {
            Log.e(TAG, "AES decryption error");
            return TAG;
        }
        //String decode=new String(decodedBytes);


    }
    public byte[] AesEncreption(String var_passcode){
        final String TAG = "SymmetricAlgorithmAES";
        //SecretKeySpec sks = null;
        try {
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed("any data used as random seed".getBytes());
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(128, sr);
            sks = new SecretKeySpec((kg.generateKey()).getEncoded(), "AES");
           System.out.println("sks1"+sks);
        } catch (Exception e) {
            Log.e(TAG, "AES secret key spec error");
        }
        byte[] encodedBytes = null;
        try {
            Cipher c = Cipher.getInstance("AES");
            c.init(Cipher.ENCRYPT_MODE, sks);
            encodedBytes = c.doFinal(var_passcode.getBytes());
        } catch (Exception e) {
            Log.e(TAG, "AES encryption error");
        }
        return encodedBytes;

    }
    @Override
    public void onBackPressed(){
    }
}




