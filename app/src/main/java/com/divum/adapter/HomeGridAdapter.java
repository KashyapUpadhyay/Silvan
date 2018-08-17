package com.divum.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import com.divum.silvan.R;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeGridAdapter extends BaseAdapter{

	private LayoutInflater mInflater;
	private ArrayList<String> array_list;
	private Context context;
	private String[] value={"HOOTER","CAMERA","VDB","PROFILE","PANIC"};

	private HashMap<String, Integer> hashValue;

	public HomeGridAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context=context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		value=App_Variable.GetHomeValue();

		hashValue=new HashMap<String, Integer>();
		hashValue.put("HOOTER", R.drawable.icon_hooter);
		hashValue.put("CAMERA", R.drawable.icon_camera);
		hashValue.put("VDB", R.drawable.icon_away);
		hashValue.put("PROFILE", R.drawable.icon_maid);
		hashValue.put("PANIC", R.drawable.icon_panic);

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
			convertView = mInflater.inflate(R.layout.home_box_view, parent, false);
			holder = new Holder();
			holder.txtEachSetting = (TextView) convertView.findViewById(R.id.home_text);
			holder.imgEachSetting = (ImageView) convertView.findViewById(R.id.home_box_img);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		//holder.imgEachSetting.setImageResource(resImage[position]);
		/*if(value[position].equals("PROFILE")){
			
			int drawableIcon=getProfileIcon();
			if(drawableIcon!=0){
				holder.imgEachSetting.setImageResource(drawableIcon);
			}
			holder.txtEachSetting.setText(App_Variable.PROFILE.toUpperCase());
		}else{*/
			holder.imgEachSetting.setImageResource(hashValue.get(value[position]));
			holder.txtEachSetting.setText(value[position].toUpperCase());
		//}

		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public TextView txtEachSetting;
		public ImageView imgEachSetting;
	}

	private int getProfileIcon() {

		String _profile=App_Variable.PROFILE.toLowerCase().trim();
		if(_profile.equals("maid"))
			return R.drawable.maid_icon;
		else if(_profile.equals("away"))
			return R.drawable.away_icon;
		else if(_profile.equals("disarm")){
			return R.drawable.disarm_icon;
		}else if(_profile.equals("terrace")){
			return R.drawable.terrace_icon;
		}

		return 0;

	}

}
