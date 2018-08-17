package com.divum.adapter;

import java.util.ArrayList;

import com.divum.silvan.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FanLevelAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater;
	private ArrayList<String> array_list;
	private Context context;

	public FanLevelAdapter(Context context,ArrayList<String> listAcLvlKey) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.array_list=listAcLvlKey;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return array_list.size();
	}

	@Override
	public Object getItem(int arg0) {
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
			convertView = mInflater.inflate(R.layout.ac_level_view, parent, false);
			holder = new Holder();
			holder.txt_ac_level = (TextView) convertView.findViewById(R.id.txt_ac_level);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		
		String data=array_list.get(position);
		holder.txt_ac_level.setText(data.substring(data.length()-1));

		return convertView;
	}
	
	/** View holder for the views we need access to */
	private static class Holder {
		public TextView txt_ac_level;
		
	}

}
