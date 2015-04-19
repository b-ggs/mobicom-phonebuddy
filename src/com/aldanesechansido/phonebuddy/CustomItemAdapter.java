package com.aldanesechansido.phonebuddy;



import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;


public class CustomItemAdapter extends ArrayAdapter<Setting> {
	
	Setting[] settings;
	
	public CustomItemAdapter(Context context, int resource, Setting[] objects) {
		super(context, resource, objects);
		settings = objects;
		
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		if(convertView == null)
		{
			LayoutInflater infl = (LayoutInflater) getContext().getSystemService(Service.LAYOUT_INFLATER_SERVICE);
			convertView = infl.inflate(R.layout.list_item, parent, false);
		}
		
		
		TextView tv1 = (TextView) convertView.findViewById((R.id.tv_header));
		TextView tv2 = (TextView) convertView.findViewById((R.id.tv_description));
		Switch s = (Switch) convertView.findViewById((R.id.switch_btn));
		
		Setting temp = settings[position];
		
		tv1.setText(temp.getName());
		tv2.setText(temp.getDesc());
		if(temp.hasSwitch())
		{
			s.setVisibility(View.VISIBLE);
			s.setChecked(temp.isActive());
		}
		else
			s.setVisibility(View.INVISIBLE);
		
		return convertView;
	}
}
