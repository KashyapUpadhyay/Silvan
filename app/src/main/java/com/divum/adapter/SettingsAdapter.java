package com.divum.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.divum.silvan.R;
import com.divum.utils.App_Variable;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SettingsAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private ArrayList<String> array_list;
	private Context context;
	String[] value={"Sensor Status","Refresh","Log","Profile","Mood","Ip","About us","Pincode settings"};
//	private int[] resImage={R.drawable.settings_sensor,R.drawable.settings_refresh,R.drawable.settings_log,
//			R.drawable.settings_profile,R.drawable.settings_mood,R.drawable.settings_ip};
//

	private HashMap<String, Integer> hashSettings;

	public SettingsAdapter(Context context) {
		// TODO Auto-generated constructor stub

		this.context=context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		value=App_Variable.GetSettingsValue();
		
		hashSettings=new HashMap<String, Integer>();
		hashSettings.put("Sensor Status", R.drawable.settings_sensor);
		hashSettings.put("Refresh", R.drawable.settings_refresh);
		hashSettings.put("Log", R.drawable.settings_log);
		hashSettings.put("Profile", R.drawable.settings_profile);
		hashSettings.put("Mood", R.drawable.settings_mood);
		hashSettings.put("Ip", R.drawable.settings_ip);
		hashSettings.put("About us", R.drawable.selected_settings_aboutus1);
		hashSettings.put("Pincode settings",R.drawable.settings_icon_selected);
		//hashSettings.put("GBD",R.drawable.sensor_gasleak);
	
		
		

		/*if(App_Variable.STATUS_SENSORS.equals("0")&&App_Variable.STATUS_DOOR_UNLOCK.equals("0")&&App_Variable.STATUS_LIFESTYLE.equals("0")){
			value=new String[]{"Ip"};
			resImage=new int[]{R.drawable.settings_ip};
		}else if(App_Variable.STATUS_SENSORS.equals("0")&&App_Variable.STATUS_DOOR_UNLOCK.equals("0")){
			value=new String[]{"Refresh","Mood","Ip"};
			resImage=new int[]{R.drawable.settings_refresh,R.drawable.settings_mood,R.drawable.settings_ip};
		}
		else if(App_Variable.STATUS_SENSORS.equals("0")){
			value=new String[]{"Refresh","Log","Mood","Ip"};
			resImage=new int[]{R.drawable.settings_refresh,R.drawable.settings_log,R.drawable.settings_mood,R.drawable.settings_ip};
		}else if(App_Variable.STATUS_LIFESTYLE.equals("0")){
			value=new String[]{"Refresh","Log","Mood","Ip"};
			resImage=new int[]{R.drawable.settings_refresh,R.drawable.settings_log,R.drawable.settings_mood,R.drawable.settings_ip};
		}*/
	}

	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return value.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.settings_view, parent, false);
			holder = new Holder();
			holder.txtEachSetting = (TextView) convertView.findViewById(R.id.txtEachSetting);
			holder.imgEachSetting = (ImageView) convertView.findViewById(R.id.imgEachSetting);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//holder.imgEachSetting.setImageResource(resImage[position]);
		holder.imgEachSetting.setImageResource(hashSettings.get(value[position]));
		holder.txtEachSetting.setText(value[position].toUpperCase());

		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public TextView txtEachSetting;
		public ImageView imgEachSetting;
	}

}
