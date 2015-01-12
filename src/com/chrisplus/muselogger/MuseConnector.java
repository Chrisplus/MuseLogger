package com.chrisplus.muselogger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class MuseConnector {

	private BluetoothDevice currentDevice;
	private BluetoothSocket socket;

	public MuseConnector() {
		socket = null;
		Set<BluetoothDevice> devices = BluetoothUtils.findPaired();

		for (BluetoothDevice device : devices) {
			if (device.getName().equalsIgnoreCase("muse")) {
				currentDevice = device;
				break;
			}
		}
	}

	public boolean connect() {
		if (currentDevice == null) {
			return false;
		}

		BluetoothSocket tmp = null;

		try {
			tmp = currentDevice.createRfcommSocketToServiceRecord(UUID
					.fromString("00001101-0000-1000-8000-00805F9B34FB"));
			tmp.connect();

			if (tmp.isConnected()) {
				socket = tmp;
				return true;
			} else {
				return false;
			}

		} catch (IOException e) {
			return false;
		}
	}

	public BluetoothSocket getReader() {
		return socket;
	}

	public void startStreaming() {
		if (socket != null && socket.isConnected()) {

			try {
				OutputStream writer = socket.getOutputStream();

				writer.write(getByte("h\n"));
				writer.flush();

				writer.write(getByte("v 2\n"));
				writer.flush();

				writer.write(getByte("% 14\n"));
				writer.flush();
				writer.write(getByte("?\n"));
				writer.flush();
				writer.write(getByte("s\n"));
				writer.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public void stopStreaming() {
		if (socket != null && socket.isConnected()) {
			try {
				OutputStream writer = socket.getOutputStream();

				writer.write(getByte("h\n"));
				writer.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void keepStreaming() {
		if (socket != null && socket.isConnected()) {
			try {
				OutputStream writer = socket.getOutputStream();
				writer.write(getByte("k\n"));
				writer.flush();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void close() {
		stopStreaming();

		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static byte[] getByte(String message) {
		return message.getBytes(StandardCharsets.US_ASCII);
	}

}
