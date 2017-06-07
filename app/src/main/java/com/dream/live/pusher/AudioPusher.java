package com.dream.live.pusher;

import com.dream.live.jni.PushNative;
import com.dream.live.params.AudioParam;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder.AudioSource;

public class AudioPusher extends Pusher{

	private AudioParam audioParam;
	private AudioRecord audioRecord;
	private boolean isPushing = false;
	private int minBufferSize;
	private PushNative pushNative;

	public AudioPusher(AudioParam audioParam, PushNative pushNative) {
		this.audioParam = audioParam;
		this.pushNative = pushNative;
		
		int channelConfig = audioParam.getChannel() == 1 ? 
				AudioFormat.CHANNEL_IN_MONO : AudioFormat.CHANNEL_IN_STEREO; 
		//最小缓冲区大小
		minBufferSize = AudioRecord.getMinBufferSize(audioParam.getSampleRateInHz(), channelConfig, AudioFormat.ENCODING_PCM_16BIT);
		audioRecord = new AudioRecord(AudioSource.MIC, 
				audioParam.getSampleRateInHz(), 
				channelConfig, 
				AudioFormat.ENCODING_PCM_16BIT, minBufferSize);
	}
	

	@Override
	public void startPush() {
		isPushing = true;
		pushNative.setAudioOptions(audioParam.getSampleRateInHz(), audioParam.getChannel());
		//启动一个录音子线程
		new Thread(new AudioRecordTask()).start();
	}

	@Override
	public void stopPush() {
		isPushing = false;
		audioRecord.stop();
	}
	
	@Override
	public void release() {
		if(audioRecord != null){
			audioRecord.release();
			audioRecord = null;
		}
	}

	class AudioRecordTask implements Runnable{

		@Override
		public void run() {
			//开始录音
			audioRecord.startRecording();
			
			while(isPushing){
				//通过AudioRecord不断读取音频数据
				byte[] buffer = new byte[minBufferSize];
				int len = audioRecord.read(buffer, 0, buffer.length);
				if(len > 0){
					//传给Native代码，进行音频编码
					pushNative.fireAudio(buffer, len);
				}
			}
		}
		
	}

}
