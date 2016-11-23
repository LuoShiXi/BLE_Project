package com.cytx.view;

import com.cytx.model.ServiceModel;
import com.mt.mtbletools.R;

import android.bluetooth.BluetoothGattService;
import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ServiceView extends LinearLayout {
	private Context context;
	public ServiceView(Context context) {
		super(context);
		this.context = context;
		initView();
	}

	private TextView grounpname_txt;
	private TextView grounp_uuid_txt;
	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fmt_servicegrounp, this);
		
		grounpname_txt = (TextView) findViewById(R.id.grounpname_txt);
		grounp_uuid_txt = (TextView) findViewById(R.id.grounp_uuid_txt);
	}
	
	// 更新UI
	public void setDatas(ServiceModel datas){
		grounpname_txt.setText(datas.getName());
		BluetoothGattService service = datas.getService();
		if(service == null){
			return;
		}
		grounp_uuid_txt.setText(service.getUuid().toString());
	}
}
