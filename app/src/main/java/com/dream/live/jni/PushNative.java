package com.dream.live.jni;

import com.dream.live.listener.LiveStateChangeListener;

/**
 * 调用C代码进行编码与推流
 * @version 1.0
 */
public class PushNative {
	
	public static final int CONNECT_FAILED = 101;
	public static final int INIT_FAILED = 102;
	
	LiveStateChangeListener liveStateChangeListener;
	
	/**
	 * 接收Native层抛出的错误
	 * @param code
	 */
	public void throwNativeError(int code){
		if(liveStateChangeListener != null){
			liveStateChangeListener.onError(code);
		}
	}
	
	
	public native void startPush(String url);
	
	public native void stopPush();
	
	public native void release();
	
	/**
	 * 设置视频参数
	 * @param width
	 * @param height
	 * @param bitrate
	 * @param fps
	 */
	public native void setVideoOptions(int width, int height, int bitrate, int fps);
	
	/**
	 * 设置音频参数
	 * @param sampleRateInHz
	 * @param channel
	 */
	public native void setAudioOptions(int sampleRateInHz, int channel);
	

	/**
	 * 发送视频数据
	 * @param data
	 */
	public native void fireVideo(byte[] data);
	
	/**
	 * 发送音频数据
	 * @param data
	 * @param len
	 */
	public native void fireAudio(byte[] data, int len);
	
	
	public void setLiveStateChangeListener(LiveStateChangeListener liveStateChangeListener) {
		this.liveStateChangeListener = liveStateChangeListener;
	}
	
	public void removeLiveStateChangeListener(){
		this.liveStateChangeListener = null;
	}
	
	static{
		System.loadLibrary("dn_live");
	}
	
}
