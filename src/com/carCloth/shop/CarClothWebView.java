package com.carCloth.shop;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.mt.mtbletools.R;

public class CarClothWebView extends Activity {
	
	//网店链接显示页面
	private WebView webView; 

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.webviewshop);
		
		webView = (WebView) findViewById(R.id.webviewShop);
		this.webView.getSettings().setSupportZoom(false);  
//      this.webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);  
        this.webView.getSettings().setJavaScriptEnabled(true);  
        this.webView.getSettings().setDomStorageEnabled(true); 
        
        webView.setWebViewClient(new WebViewClient(){
 	       @Override
 	       public boolean shouldOverrideUrlLoading(WebView view, String url) {
 	            // TODO Auto-generated method stub
 	            //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
 	            view.loadUrl(url);
 	            return true;
 	        }
 	       }); 
        	this.webView.loadUrl("https://shop583768536.taobao.com/");
		
	}
}