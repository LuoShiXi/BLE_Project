package com.carCloth.shop;

import java.util.Calendar;
import java.util.Date;

import com.cheyitianxia.bletools.MyScrollView;
import com.mt.mtbletools.R;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.GestureDetector.OnGestureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class CarClothShop extends Activity implements OnGestureListener,
		OnTouchListener, OnClickListener {
	//网店链接
	private WebView webView;

	// 图片切换
	private TextView date_TextView;
	private ViewFlipper viewFlipper;
	private boolean showNext = true;
	private boolean isRun = true;
	private int currentPage = 0;
	private final int SHOW_NEXT = 0011;
	private static final int FLING_MIN_DISTANCE = 50;
	private static final int FLING_MIN_VELOCITY = 0;
	private GestureDetector mGestureDetector;

	// private MainActivity mainactivity = new MainActivity();

	// 电商连接按钮
	private Button btn_taobao;
	private Button btn_ali;
	private Button btn_weidian;
	private Button btn_jingdong;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shopping);

		btn_taobao = (Button) findViewById(R.id.btn_taobao);
		btn_ali = (Button) findViewById(R.id.btn_ali);
		btn_weidian = (Button) findViewById(R.id.btn_weidian);
		btn_jingdong = (Button) findViewById(R.id.btn_jingdong);

		btn_taobao.setOnClickListener(this);
		btn_ali.setOnClickListener(this);
		btn_weidian.setOnClickListener(this);
		btn_jingdong.setOnClickListener(this);

		date_TextView = (TextView) findViewById(R.id.home_date_tv);
		date_TextView.setText(getDate());

		viewFlipper = (ViewFlipper) findViewById(R.id.mViewFlipper_vf);
		mGestureDetector = new GestureDetector(this);
		// viewFlipper.setOnTouchListener(this);
		viewFlipper.setLongClickable(false);
		viewFlipper.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				switch (viewFlipper.getDisplayedChild()) {
				case 0:
					toastInfo("00000000000");
					break;
				case 1:
					toastInfo("11111111111");
					break;
				case 2:
					toastInfo("22222222222");
					break;
				case 3:
					toastInfo("33333333333");
					break;
				default:
					break;
				}
			}
		});
		displayRatio_selelct(currentPage);

		MyScrollView myScrollView = (MyScrollView) findViewById(R.id.viewflipper_scrollview);
		myScrollView.setGestureDetector(mGestureDetector);

//		setStatus("已连接");
		thread.start();

	}

//	private final void setStatus(CharSequence subTitle) {
//		final ActionBar actionBar = getActionBar();
//		actionBar.setSubtitle(subTitle);
//	}

	// 此Handler处理BluetoothChatService传来的消息
	@SuppressLint("HandlerLeak")
	private final Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SHOW_NEXT:
				if (showNext) {
					showNextView();
				} else {
					showPreviousView();
				}
				break;
			}
		}
	};

	// 图片切换选择
	private void displayRatio_selelct(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(true);
	}

	private void displayRatio_normal(int id) {
		int[] ratioId = { R.id.home_ratio_img_04, R.id.home_ratio_img_03,
				R.id.home_ratio_img_02, R.id.home_ratio_img_01 };
		ImageView img = (ImageView) findViewById(ratioId[id]);
		img.setSelected(false);
	}

	// 获取当前日期
	private String getDate() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		int w = c.get(Calendar.DAY_OF_WEEK) - 1;
		if (w < 0) {
			w = 0;
		}
		String mDate = c.get(Calendar.YEAR) + "年" + (c.get(Calendar.MONTH) + 1)
				+ "月" + c.get(Calendar.DATE) + "日  " + weekDays[w];
		return mDate;
	}

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		this.viewFlipper.setClickable(true);
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		Log.e("view", "onFling");
		if (e1.getX() - e2.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "left");
			showNextView();
			showNext = true;
		} else if (e2.getX() - e1.getX() > FLING_MIN_DISTANCE
				&& Math.abs(velocityX) > FLING_MIN_VELOCITY) {
			Log.e("fling", "right");
			showPreviousView();
			showNext = false;
		}
		this.viewFlipper.setClickable(false);
		return false;
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub

	}

	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		return mGestureDetector.onTouchEvent(event);
	}

	Thread thread = new Thread() {

		@Override
		public void run() {
			// 图片展示run
			while (isRun) {
				try {
					Thread.sleep(1000 * 8);
					Message msg = new Message();
					msg.what = SHOW_NEXT;
					mHandler.sendMessage(msg);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	};

	private void showNextView() {

		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_left_out));
		viewFlipper.showNext();
		currentPage++;
		if (currentPage == viewFlipper.getChildCount()) {
			displayRatio_normal(currentPage - 1);
			currentPage = 0;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage - 1);
		}
		Log.e("currentPage", currentPage + "");

	}

	private void showPreviousView() {
		displayRatio_selelct(currentPage);
		viewFlipper.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_in));
		viewFlipper.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.push_right_out));
		viewFlipper.showPrevious();
		currentPage--;
		if (currentPage == -1) {
			displayRatio_normal(currentPage + 1);
			currentPage = viewFlipper.getChildCount() - 1;
			displayRatio_selelct(currentPage);
		} else {
			displayRatio_selelct(currentPage);
			displayRatio_normal(currentPage + 1);
		}
		Log.e("currentPage", currentPage + "");
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			isRun = false;
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	private void toastInfo(String string) {
		Toast.makeText(CarClothShop.this, string, Toast.LENGTH_SHORT).show();
	}

	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_taobao:
			Toast.makeText(CarClothShop.this, "淘宝链接", Toast.LENGTH_SHORT)
					.show();
			Intent intent_taobao = new Intent();
			intent_taobao.setClass(this, CarClothWebView.class);
			startActivity(intent_taobao);
			break;
		case R.id.btn_ali:
			Toast.makeText(CarClothShop.this, "阿里链接", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.btn_weidian:
			Toast.makeText(CarClothShop.this, "微店链接", Toast.LENGTH_SHORT)
					.show();
			break;
		case R.id.btn_jingdong:
			Toast.makeText(CarClothShop.this, "京东链接", Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}

}
