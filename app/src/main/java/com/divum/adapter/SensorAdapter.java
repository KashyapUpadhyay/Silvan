package com.divum.adapter;

import java.util.ArrayList;

import com.divum.silvan.R;
import com.divum.utils.App_Variable;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SensorAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private String[] array_list,key_list;
	private Context context;
	private String[] value={"Main Door","Terrace Door","ServerRoom Door","GasLeak","Motion Sensor","MBR-1","MBR-2","BedRoom-3","BedRoom-4","Smoke","GBD"};
	private int[] sensorImage={R.drawable.sensor_door,R.drawable.sensor_door,R.drawable.sensor_door,R.drawable.sensor_gasleak,R.drawable.sensor_motion,
			R.drawable.sensor_panicroom,R.drawable.sensor_panicroom,R.drawable.sensor_panicroom,R.drawable.sensor_panicroom,R.drawable.sensor_smoke,R.drawable.sensor_gasleak};

	public SensorAdapter(Context context, String[] sensorResponse, String[] ary_response) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.array_list=sensorResponse;
		this.key_list=ary_response;
		System.out.println("key list"+key_list);
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setResponse(String[] sensorResponse, String[] ary_response) {
		this.array_list=sensorResponse;
		this.key_list=ary_response;
		notifyDataSetChanged();
	}


	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return App_Variable.hashSensor.size();
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
			convertView = mInflater.inflate(com.divum.silvan.R.layout.settings_view, parent, false);
			holder = new Holder();
			holder.txtEachSetting = (TextView) convertView.findViewById(com.divum.silvan.R.id.txtEachSetting);
			holder.imgEachSetting = (ImageView) convertView.findViewById(com.divum.silvan.R.id.imgEachSetting);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}


		//String key=App_Variable.hashSensor.get(position);

		if(key_list!=null) {
			if(key_list[position]!=null) {
				String name = App_Variable.hashSensor.get(key_list[position]);

				if (name != null)
					holder.txtEachSetting.setText(name);

				if (key_list[position].contains("doorintrusion")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_door);
				} else if (key_list[position].contains("panic")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_panicroom);
				} else if (key_list[position].contains("gasleak")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_gasleak);
				} else if (key_list[position].contains("motion")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_motion);
				} else if (key_list[position].contains("smoke")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_smoke);
				} else if (key_list[position].contains("glassbreak")) {
					holder.imgEachSetting.setImageResource(R.drawable.sensor_glassdoor);
				} else if (key_list[position].contains("doorbreak")) {
					holder.imgEachSetting.setImageResource(R.drawable.glass_break);
				}else if(key_list[position].contains("heat")){
					holder.imgEachSetting.setImageResource(R.drawable.sensor_smoke);
				}
			}
		}

		if(array_list!=null) {
			for (int j = 0, jl = array_list.length; j < jl; j++) {
				if (array_list[j].contains(key_list[position])) {
					if (array_list[j].endsWith("1"))
						holder.txtEachSetting.setTextColor(Color.parseColor("#FF0000"));
					else if (array_list[j].endsWith("2"))
						holder.txtEachSetting.setTextColor(Color.parseColor("#0000FF"));
					else
						holder.txtEachSetting.setTextColor(Color.parseColor("#FFFFFF"));

				}

			}
		}





		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public TextView txtEachSetting;
		public ImageView imgEachSetting;

	}

	
}
