package com.cytx.view;

import com.cheyitianxia.bletools.TalkFragment;
import com.mt.mtbletools.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TalkMsgView extends LinearLayout {
	private Context context;

	public TalkMsgView(Context context) {
		super(context);
		this.context = context;

		initView();
	}

	private View msgto_fmt_layout;
	private View msgfrom_fmt_layout;
	private TextView msgto_txt;
	private TextView msgfrom_txt;
	private void initView() {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		inflater.inflate(R.layout.fmt_talkmsg, this);
		
		msgto_fmt_layout = findViewById(R.id.msgto_fmt_layout);
		msgfrom_fmt_layout = findViewById(R.id.msgfrom_fmt_layout);
		
		msgto_txt = (TextView) findViewById(R.id.msgto_txt);
		msgfrom_txt = (TextView) findViewById(R.id.msgfrom_txt);
	}
	
	// 设置数据更新UI
	public void setDatas(TalkFragment.Msg msg){
		if(msg.getDir() == TalkFragment.Msg.DIR.RECIVE){  // 收数据
			msgto_fmt_layout.setVisibility(View.GONE);
			msgfrom_fmt_layout.setVisibility(View.VISIBLE);
			msgfrom_txt.setText(msg.getDismsg());
		}else{
			msgto_fmt_layout.setVisibility(View.VISIBLE);
			msgfrom_fmt_layout.setVisibility(View.GONE);
			msgto_txt.setText(msg.getDismsg());
		}
	}

}
