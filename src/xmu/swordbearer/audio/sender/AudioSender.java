package xmu.swordbearer.audio.sender;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import xmu.swordbearer.audio.data.AudioData;
import android.util.Log;

import com.hikvision.netsdk.HCNetSDK;
import com.test.demo.DemoActivity;

public class AudioSender implements Runnable {
	String LOG = "AudioSender ";

	private boolean isSendering = false;
	private List<AudioData> dataList;

//	DatagramSocket socket;
//	DatagramPacket dataPacket;
//	private InetAddress ip;
//	private int port;

	public AudioSender() {
		dataList = Collections.synchronizedList(new LinkedList<AudioData>());
//		try {
//			try {
//				ip = InetAddress.getByName(NetConfig.SERVER_HOST);
//				Log.e(LOG, "服务端地址是 " + ip.toString());
//				port = NetConfig.SERVER_PORT;
//				socket = new DatagramSocket();
//			} catch (UnknownHostException e) {
//				e.printStackTrace();
//			}
//		} catch (SocketException e) {
//			e.printStackTrace();
//		}
	}

	public void addData(byte[] data, int size) {
		AudioData encodedData = new AudioData();
		encodedData.setSize(size);
		byte[] tempData = new byte[size];
		System.arraycopy(data, 0, tempData, 0, size);
		encodedData.setRealData(tempData);
		dataList.add(encodedData);
	}

	/*
	 * send data to server
	 */
	private void sendData(byte[] data, int size) {
//		try {
//			dataPacket = new DatagramPacket(data, size, ip, port);
//			dataPacket.setData(data);
//			Log.e(LOG, "发送一段数据 " + data.length);
//			socket.send(dataPacket);
			
			boolean send=HCNetSDK.getInstance()
					.NET_DVR_VoiceComSendData(DemoActivity.m_iLogID, data, size);
			Log.i("HCNetSDK.getInstance().NET_DVR_VoiceComSendData",send+"");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	/*
	 * start sending data
	 */
	public void startSending() {
		new Thread(this).start();
	}

	/*
	 * stop sending data
	 */
	public void stopSending() {
		this.isSendering = false;
	}

	// run
	public void run() {
		this.isSendering = true;
		System.out.println(LOG + "start....");
		while (isSendering) {
			if (dataList.size() > 0) {
				AudioData encodedData = dataList.remove(0);
				sendData(encodedData.getRealData(), encodedData.getSize());
			}
		}
		System.out.println(LOG + "stop!!!!");
	}
}