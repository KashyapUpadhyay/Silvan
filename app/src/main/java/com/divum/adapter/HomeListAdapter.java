package com.divum.adapter;



import java.util.ArrayList;

import com.divum.silvan.R;
import com.divum.silvan.R.color;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



/** An array adapter that knows how to render views when given CustomData classes */
public class HomeListAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	ArrayList<String> array_list;
	Context context;
	public static int selectedPos=0;


	public HomeListAdapter(Context context,
			ArrayList<String> list_rooms) {
		// TODO Auto-generated constructor stub
		this.context=context;
		this.array_list=list_rooms;
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
		Holder holder;

		if (convertView == null) {
			convertView = mInflater.inflate(com.divum.silvan.R.layout.home_list_view, parent, false);
			holder = new Holder();
			holder.txt_list = (TextView) convertView.findViewById(com.divum.silvan.R.id.txt_list);
			holder.img_list_icon = (ImageView) convertView.findViewById(com.divum.silvan.R.id.img_list_icon);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		String section=array_list.get(position);

		holder.txt_list.setText(section);
		/**
		 * selection color change
		 */
		if(section.toLowerCase().contains("home")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.home_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.home_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}else if(section.toLowerCase().contains("living")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.living_room_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.living_room_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}else if(section.toLowerCase().contains("bed")||section.toLowerCase().contains("br")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.bedroom_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.bedroom_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}else if(section.toLowerCase().contains("common")||section.toLowerCase().contains("global")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.common_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.common_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}else if(section.toLowerCase().contains("drawing")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.drawing_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.drawing_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}
		else if(section.toLowerCase().contains("door")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.drawing_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.drawing_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}
		else if(section.toLowerCase().contains("settings")){
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.settings_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.settings_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}
		else if (section.toLowerCase().contains("dining"))
		{
			if (selectedPos==position) {
				holder.img_list_icon.setImageResource(R.drawable.mood_dinner);
				holder.txt_list.setTextColor(context.getResources().getColor(color.white));
			}
			else{
				holder.img_list_icon.setImageResource(R.drawable.mood_dinner_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}

		}else{
			if(selectedPos==position){
				holder.img_list_icon.setImageResource(R.drawable.bedroom_icon_selected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.white));
			}else{
				holder.img_list_icon.setImageResource(R.drawable.bedroom_icon_unselected);
				holder.txt_list.setTextColor(context.getResources().getColor(R.color.unselected));
			}
		}

		//System.out.println("head::"+array_story.get(position).getCityName());

		return convertView;
	}

	/** View holder for the views we need access to */
	private static class Holder {
		public TextView txt_list;
		public ImageView img_list_icon;
	}

	public void selectedItemPosition(int position) {
		selectedPos=position;

	}


}
