package com.chrisplus.muselogger;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

public class BluetoothUtils {

	public static Set<BluetoothDevice> findPaired() {

		BluetoothAdapter bluetoothAdapter = BluetoothAdapter
				.getDefaultAdapter();
		Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();

		if (devices == null || devices.isEmpty()) {
			return null;

		} else {
			return devices;
		}

	}

	public static boolean checkMusePaired() {
		Set<BluetoothDevice> devices = findPaired();

		if (devices == null || devices.isEmpty()) {
			return false;
		}

		for (BluetoothDevice device : devices) {
			if (device.getName().equalsIgnoreCase("muse")) {
				return true;
			}
		}

		return false;
	}

}
