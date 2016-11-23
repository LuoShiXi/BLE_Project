package com.cytx.view;

import com.cytx.model.CharacterModel;
import com.mt.mtbletools.R;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CharacterView extends LinearLayout {
	private Context context;
	public CharacterView(Context context) {
		super(context);
		this.context = context;
		initView();
	}
	
	private boolean enable = false;

	private TextView childname_txt;
	private TextView prov;
	private TextView child_uuid_txt;
	private Button enable_btn;
	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fmt_servicechild, this);
		
		childname_txt = (TextView) findViewById(R.id.childname_txt);
		prov = (TextView) findViewById(R.id.prov);
		child_uuid_txt = (TextView) findViewById(R.id.child_uuid_txt);
		enable_btn = (Button) findViewById(R.id.enable_btn);
		
		enable_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				enable = !enable;
				if(callback != null){
					callback.onEnable(enable);
				}
				if(enable){
					enable_btn.setText(getResources().getString(R.string.disable));
				}else{
					enable_btn.setText(getResources().getString(R.string.enable));
				}
			}
		});
	}
	
	// 更新UI
	public void setDatas(CharacterModel datas){
		childname_txt.setText(datas.getName());
		enable_btn.setVisibility(View.GONE);
		
		BluetoothGattCharacteristic charact = datas.getCharact();
		if(charact == null){
			return;
		}
		child_uuid_txt.setText(charact.getUuid().toString());
		
		String pro = "(" + charact.getProperties() + ")";
		if (0 != (charact.getProperties() & BluetoothGattCharacteristic.PROPERTY_READ)) { // 可读
			pro += "可读,";
		}
		if ((0 != (charact.getProperties() & BluetoothGattCharacteristic.PROPERTY_WRITE))
				|| (0 != (charact.getProperties()
						& BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE))) { // 可写
			pro += "可写,";
		}
		if ((0 != (charact.getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY))
				|| (0 != (charact.getProperties()
						& BluetoothGattCharacteristic.PROPERTY_INDICATE))) { // 通知
			pro += "可通知";
			
			enable_btn.setVisibility(View.VISIBLE);
		}
		prov.setText(pro);
	}

	private OnEnableClick callback;
	// 设置按钮监听
	public void setEnableClick(OnEnableClick callback){
		this.callback = callback;
	}
	
	// 按钮监听
	public static interface OnEnableClick{
		public void onEnable(boolean enable);
	}
}
