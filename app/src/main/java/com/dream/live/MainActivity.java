package com.dream.live;

import com.dongnaoedu.live.R;
import com.dream.live.jni.PushNative;
import com.dream.live.listener.LiveStateChangeListener;
import com.dream.live.pusher.LivePusher;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity implements LiveStateChangeListener {

	//替换你当前的地址
	static final String URL = "rtmp://123.207.37.204:1935/live/dream";
	private LivePusher live;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case PushNative.CONNECT_FAILED:
				Toast.makeText(MainActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
				//Log.d("dream", "连接失败..");
				break;
			case PushNative.INIT_FAILED:
				Toast.makeText(MainActivity.this, "初始化失败", Toast.LENGTH_SHORT).show();
				break;	
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.surface);
		//相机图像的预览
		live = new LivePusher(surfaceView.getHolder());
	}

	/**
	 * 开始直播
	 * @param view
	 */
	public void mStartLive(View view) {
		Button btn = (Button)view;
		if(btn.getText().equals("开始直播")){
			live.startPush(URL,this);
			btn.setText("停止直播");
		}else{
			live.stopPush();
			btn.setText("开始直播");
		}
	}

	/**
	 * 切换摄像头
	 * @param btn
	 */
	public void mSwitchCamera(View btn) {
		live.switchCamera();
	}

	//改方法执行仍然在子线程中，发送消息到UI主线程
	@Override
	public void onError(int code) {
		handler.sendEmptyMessage(code);
	}

}
