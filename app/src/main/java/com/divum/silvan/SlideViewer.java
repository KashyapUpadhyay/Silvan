package com.divum.silvan;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.divum.adapter.HomeListAdapter;
import com.divum.adapter.MoodListAdapter;
import com.divum.imagedownloder.LoadImageView;
import com.divum.silvan.callback.ShowViewCallback;
import com.divum.utils.App_Variable;
import com.divum.utils.CustomLog;

@SuppressLint("ValidFragment")
public class SlideViewer extends Fragment{

	private ArrayList<String> list_rooms;
	private ListView list_home;
	private ImageView slide_logo;
	//private ShowViewCallback callback;

	public SlideViewer() {

	}

	String section="";
	public static SlideViewer newInstance(String _section) {
		SlideViewer myFragment = new SlideViewer(_section);

		Bundle args = new Bundle();
		args.putString("address", _section);
		myFragment.setArguments(args);

		return myFragment;
	}
	public SlideViewer(String _section){
		/**
		 * list of multiple rooms  // app started
		 */
		section = _section;

		//callback = (ShowViewCallback) _activity;

		list_rooms=new ArrayList<String>();
		list_rooms.add("Home");


		if(App_Variable.STATUS_LIFESTYLE.equals("1")){
			if(App_Variable.hashRoomType!=null){
				if(App_Variable.hashRoomType.entrySet()!=null){
					Set entrySet = App_Variable.hashRoomType.entrySet();
					System.out.println("json entity set"+entrySet);
					Iterator iterator = entrySet.iterator();
					while (iterator.hasNext()) {
						Entry entry = (Entry) iterator.next();
						CustomLog.debug("key new:"+entry.getKey()+","+entry.getValue());
						if(section.equals("mood")){
							String type=(""+entry.getKey()).toLowerCase();
							if(!type.startsWith("common"))
								list_rooms.add(""+entry.getKey());
							System.out.println("json List rooms"+list_rooms);
						}else{
							list_rooms.add(""+entry.getKey());
						}
					}
				}
			}
		}

		/*for(Enumeration en = hashRoomType.keys(); en.hasMoreElements();){
			String key=(String)en.nextElement();
			CustomLog.debug("key::"+key);
			list_rooms.add(key);
		}*/

		if(section.equals("home")){
			if(App_Variable.STATUS_DOOR_UNLOCK.equals("1")){
				list_rooms.add("Door Unlock");
			}else{
				//no need to add
			}
		}
		list_rooms.add("Settings");
		///logot


	}


	public View onCreateView(LayoutInflater _inflater, ViewGroup container, Bundle savedInstanceState) {

		LayoutInflater inflater =_inflater;
		View _vi = inflater.inflate(R.layout.sliding_view, null);
		slide_logo=(ImageView)_vi.findViewById(R.id.slide_logo);
		String url="http://s3.amazonaws.com/silvanlabs/apps/has/"+App_Variable.CONFIG_NUMBER+"/images/android_hdpi/customer_logo.png";
		LoadImageView.LoadImage(url, slide_logo);
		list_home=(ListView)_vi.findViewById(R.id.list_home);

		if(section.equals("home")){
			final HomeListAdapter adapter=new HomeListAdapter(getActivity(), list_rooms);
			list_home.setAdapter(adapter);

			list_home.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					CustomLog.debug("selected::"+position);
					adapter.selectedItemPosition(position);
					adapter.notifyDataSetChanged();

					((HomeActivity) getActivity()).ShowViewSection(position,list_rooms.get(position));
					//((BaseActivity) getActivity()).ShowView(position,list_rooms.get(position),getActivity());

				}
			});
		}else if(section.equals("mood")){
			final MoodListAdapter adapter=new MoodListAdapter(getActivity(), list_rooms);
			list_home.setAdapter(adapter);

			list_home.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					CustomLog.debug("selected::"+position);
					adapter.selectedItemPosition(position);
					adapter.notifyDataSetChanged();

					((MoodActivity) getActivity()).ShowViewSection(position,list_rooms.get(position));
					//((BaseMoodActivity) getActivity()).ShowView(position,list_rooms.get(position),getActivity());

				}
			});
		}
		return _vi;

	}

}
