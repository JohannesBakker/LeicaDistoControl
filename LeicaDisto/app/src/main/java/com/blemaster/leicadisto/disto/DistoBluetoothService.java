package com.blemaster.leicadisto.disto;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.BluetoothProfile.ServiceListener;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.format.Time;
import android.util.Log;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.google.android.gms.analytics.Tracker;
import de.ffuf.leica.sketchlibrary.LeicaApplication;
import de.ffuf.leica.sketchlibrary.LeicaBaseActivity;
import de.ffuf.leica.sketchlibrary.Overview;
import de.ffuf.leica.sketchlibrary.classes.JQuery;
import de.ffuf.leica.sketchlibrary.database.DistoDevice;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.UUID;

public class DistoBluetoothService
{
	private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private static DistoBluetoothService instance;
	private BluetoothBondReceiver bluetoothBondReceiver;
	public BluetoothGatt btGatt;
	private Context context;
	private BluetoothDevice currentDevice;
	private DistoDevice dbDevice;
	private boolean forceStopped;

	BluetoothGattCallback gattCallback = new BluetoothGattCallback()
	{
		DistoMeasurement currentMeasure = new DistoMeasurement();
	
		private boolean enableNotification(BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
		{
			boolean bool = true;

			if ((DistoBluetoothService.this.btGatt == null) || (paramAnonymousBluetoothGattCharacteristic == null)) {
				return false;
			}

			while (!DistoBluetoothService.this.btGatt.setCharacteristicNotification(paramAnonymousBluetoothGattCharacteristic, true)) {
				return false;
			}

			Object localObject;
			Message handlerMsg;

			if (paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT))
			{
				DistoBluetoothService.this.setState(3);
				handlerMsg = DistoBluetoothService.this.m_Handler.obtainMessage(4);
				Bundle localBundle = new Bundle();

				localBundle.putBoolean("device_isble", true);

//				JQuery.e("DEVICE NAME ON CONNECT: " + DistoBluetoothService.this.btGatt.getDevice().getName());
				localBundle.putString("device_name", DistoBluetoothService.this.btGatt.getDevice().getName());
				localBundle.putString("device_address", DistoBluetoothService.this.btGatt.getDevice().getAddress());
				handlerMsg.setData(localBundle);

				DistoBluetoothService.this.m_Handler.sendMessage(handlerMsg);
				localObject = DistoBluetoothService.this;
				if (DistoBluetoothService.this.currentDevice.getBondState() != 12) {
					break label241;
				}
			}
			for (;;)
			{
			DistoBluetoothService.access$2002((DistoBluetoothService)localObject, bool);
			paramAnonymousBluetoothGattCharacteristic = paramAnonymousBluetoothGattCharacteristic.getDescriptor(DISTOtransfer.DISTO_DESCRIPTOR);
			if (paramAnonymousBluetoothGattCharacteristic == null) {
				break;
			}
			Log.i("DISTO BluetoothService", "enable notification");
			paramAnonymousBluetoothGattCharacteristic.setValue(BluetoothGattDescriptor.ENABLE_INDICATION_VALUE);
			return DistoBluetoothService.this.btGatt.writeDescriptor(paramAnonymousBluetoothGattCharacteristic);
			label241:
			bool = false;
			}
		}
	
	public void onCharacteristicChanged(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic)
	{
		JQuery.w("Characteristic changed: " + paramAnonymousBluetoothGattCharacteristic.getUuid() + " this: " + this);
		onCharacteristicRead(paramAnonymousBluetoothGatt, paramAnonymousBluetoothGattCharacteristic, 0);
	}
	
	public void onCharacteristicRead(final BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattCharacteristic paramAnonymousBluetoothGattCharacteristic, int paramAnonymousInt)
	{
		if (paramAnonymousInt != 0) {}
		do
		{
		do
		{
			do
			{
			return;
			float f;
			if (paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE))
			{
				f = ByteBuffer.wrap(paramAnonymousBluetoothGattCharacteristic.getValue()).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				JQuery.i("", "DISTANCE: " + f);
				this.currentMeasure = new DistoMeasurement();
				this.currentMeasure.m_Time = new Time();
				this.currentMeasure.m_Time.setToNow();
				this.currentMeasure.m_Distance = String.valueOf(f);
				this.currentMeasure.m_DistanceOriginal = f;
				return;
			}
			if ((paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT)) && (this.currentMeasure != null))
			{
				this.currentMeasure.m_DistanceUnitBLE = paramAnonymousBluetoothGattCharacteristic.getIntValue(18, 0).intValue();
				JQuery.i("", "DISTANCE UNIT: " + this.currentMeasure.m_DistanceUnitBLE);
				this.currentMeasure.m_Distance = this.currentMeasure.getDistance();
				DistoBluetoothService.this.m_Handler.obtainMessage(9, -1, -1, this.currentMeasure).sendToTarget();
				DistoBluetoothService.this.m_Handler.postDelayed(new Runnable()
				{
				public void run()
				{
					DistoBluetoothService.10.this.currentMeasure = null;
				}
				}, 500L);
				return;
			}
			if (paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION))
			{
				f = ByteBuffer.wrap(paramAnonymousBluetoothGattCharacteristic.getValue()).order(ByteOrder.LITTLE_ENDIAN).getFloat();
				if (this.currentMeasure == null) {
				this.currentMeasure = new DistoMeasurement();
				}
				this.currentMeasure.m_Angle = String.valueOf(f);
				JQuery.i("", "ANGLE: " + this.currentMeasure.m_Angle);
				return;
			}
			if (!paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION_DISPLAY_UNIT)) {
				break;
			}
			this.currentMeasure.m_AngleUnitBLE = paramAnonymousBluetoothGattCharacteristic.getIntValue(18, 0).intValue();
			JQuery.i("", "ANGLE UNIT: " + this.currentMeasure.m_AngleUnitBLE);
			this.currentMeasure.m_Angle = this.currentMeasure.getAngle();
			} while (this.currentMeasure.m_DistanceUnitBLE != -1);
			DistoBluetoothService.this.m_Handler.obtainMessage(9, -1, -1, this.currentMeasure).sendToTarget();
			this.currentMeasure = null;
			return;
		} while (!paramAnonymousBluetoothGattCharacteristic.getUuid().equals(DISTOtransfer.DISTO_CHARACTERISTIC_MODEL_NAME));
		paramAnonymousBluetoothGattCharacteristic = new String(paramAnonymousBluetoothGattCharacteristic.getValue()).replace("\000", "").trim();
		paramAnonymousBluetoothGatt = paramAnonymousBluetoothGattCharacteristic;
		if (paramAnonymousBluetoothGattCharacteristic.equals("D210")) {
			paramAnonymousBluetoothGatt = "D2";
		}
		JQuery.e("CURRENT TYPE: " + paramAnonymousBluetoothGatt);
		} while ((DistoBluetoothService.this.dbDevice == null) || (DistoBluetoothService.this.context == null));
		DistoBluetoothService.this.m_Handler.post(new Runnable()
		{
		public void run()
		{
			if ((DistoBluetoothService.this.context instanceof LeicaBaseActivity))
			{
			Object localObject2 = paramAnonymousBluetoothGatt;
			Object localObject1 = localObject2;
			if (!((String)localObject2).equals("Stabila"))
			{
				localObject1 = localObject2;
				if (!((String)localObject2).equals("WDM")) {
				localObject1 = "DISTO_" + (String)localObject2;
				}
			}
			localObject2 = ((LeicaApplication)((LeicaBaseActivity)DistoBluetoothService.this.context).getApplication()).getTracker();
			((Tracker)localObject2).send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected").build());
			((Tracker)localObject2).send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected_" + (String)localObject1).build());
			}
			DistoBluetoothService.this.dbDevice.deviceType = paramAnonymousBluetoothGatt;
			DistoBluetoothService.this.dbDevice.updateRecord(DistoBluetoothService.this.context);
		}
		});
	}
	
	public void onConnectionStateChange(final BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt1, int paramAnonymousInt2)
	{
		JQuery.i("", "Connection State Change: " + paramAnonymousInt2 + " --- " + paramAnonymousInt1 + " reEnable: " + DistoBluetoothService.this.m_btReEnabled);
		if (paramAnonymousInt2 > 110)
		{
		DistoBluetoothService.this.setState(1);
		paramAnonymousBluetoothGatt = DistoBluetoothService.this.m_Handler.obtainMessage(5);
		localObject = new Bundle();
		((Bundle)localObject).putString("toast", DistoBluetoothService.this.context.getString(2131296312));
		paramAnonymousBluetoothGatt.setData((Bundle)localObject);
		DistoBluetoothService.this.m_Handler.sendMessage(paramAnonymousBluetoothGatt);
		}
		do
		{
		return;
		if (paramAnonymousInt2 == 2)
		{
			JQuery.e("!!!!!!!!!! PAIR??!?! !!!!!!!!! " + DistoBluetoothService.this.pairCurrentDevice);
			if (DistoBluetoothService.this.pairCurrentDevice) {
			JQuery.i("CREATE BOND: " + DistoBluetoothService.this.bondDevice(DistoBluetoothService.this.currentDevice));
			}
			DistoBluetoothService.access$1902(DistoBluetoothService.this, false);
			PreferenceManager.getDefaultSharedPreferences(DistoBluetoothService.this.context).edit().putString("device", paramAnonymousBluetoothGatt.getDevice().getAddress()).apply();
			PreferenceManager.getDefaultSharedPreferences(DistoBluetoothService.this.context).edit().putBoolean("deviceIsBle", true).apply();
			DistoBluetoothService.access$2202(DistoBluetoothService.this, null);
			DistoBluetoothService.this.setState(2);
			DistoBluetoothService.this.m_Handler.postDelayed(new Runnable()
			{
			public void run()
			{
				DistoBluetoothService.this.btGatt.discoverServices();
			}
			}, 1000L);
			return;
		}
		} while (paramAnonymousInt2 != 0);
		DistoBluetoothService.this.setState(0);
		Object localObject = DistoBluetoothService.this.m_Handler.obtainMessage(5);
		Bundle localBundle = new Bundle();
		localBundle.putString("toast", DistoBluetoothService.this.context.getString(2131296312));
		((Message)localObject).setData(localBundle);
		DistoBluetoothService.this.m_Handler.sendMessage((Message)localObject);
		JQuery.e("IS CURRENT DEVICE PAIRABLE? " + DistoBluetoothService.this.pairCurrentDevice);
		JQuery.i("trying to reconnect");
		DistoBluetoothService.this.m_Handler.postDelayed(new Runnable()
		{
		public void run()
		{
			if (!DistoBluetoothService.this.forceStopped) {
			DistoBluetoothService.this.findDeviceAndConnect(paramAnonymousBluetoothGatt.getDevice().getAddress());
			}
		}
		}, 9000L);
	}
	
	public void onDescriptorWrite(BluetoothGatt paramAnonymousBluetoothGatt, BluetoothGattDescriptor paramAnonymousBluetoothGattDescriptor, int paramAnonymousInt)
	{
		JQuery.i("ONDESCRIPTORWRITE: " + paramAnonymousInt + " EMPTY: " + DistoBluetoothService.this.notificationStack.empty());
		if (!DistoBluetoothService.this.notificationStack.empty())
		{
		paramAnonymousBluetoothGatt = (BluetoothGattCharacteristic)DistoBluetoothService.this.notificationStack.pop();
		JQuery.w("ENABLE NOTIFICATION!!!!: " + enableNotification(paramAnonymousBluetoothGatt));
		}
	}
	
	public void onServicesDiscovered(final BluetoothGatt paramAnonymousBluetoothGatt, int paramAnonymousInt)
	{
		for (;;)
		{
		final BluetoothGattService localBluetoothGattService;
		try
		{
			JQuery.w("ONSERVICESDISCOVERED 0 ");
			DistoBluetoothService.access$2202(DistoBluetoothService.this, new Stack());
			localObject = paramAnonymousBluetoothGatt.getServices();
			JQuery.w("ONSERVICESDISCOVERED 1 SIZE " + ((List)localObject).size());
			bool1 = false;
			Iterator localIterator = ((List)localObject).iterator();
			if (!localIterator.hasNext()) {
			break;
			}
			localBluetoothGattService = (BluetoothGattService)localIterator.next();
			JQuery.d("GOT SERVICE " + localBluetoothGattService.getUuid());
			if (!localBluetoothGattService.getUuid().equals(DISTOtransfer.DISTO_SERVICE)) {
			continue;
			}
			boolean bool2 = true;
			JQuery.w("ONSERVICESDISCOVERED 3");
			bool1 = enableNotification(localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE));
			JQuery.w("ENABLE NOTIFICATION!!!!! " + bool1);
			if (!bool1)
			{
			paramAnonymousBluetoothGatt.disconnect();
			paramAnonymousBluetoothGatt.close();
			return;
			}
			if (localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_MODEL_NAME) != null) {
			DistoBluetoothService.this.m_Handler.postDelayed(new Runnable()
			{
				public void run()
				{
				paramAnonymousBluetoothGatt.readCharacteristic(localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_MODEL_NAME));
				}
			}, 300L);
			}
			localObject = "OTHER";
			if (DistoBluetoothService.this.currentDevice.getName().toLowerCase().startsWith("STABILA".toLowerCase()))
			{
			localObject = "Stabila";
			JQuery.e("CURRENT TYPE: " + (String)localObject);
			if ((DistoBluetoothService.this.dbDevice != null) && (DistoBluetoothService.this.context != null) && (DistoBluetoothService.this.dbDevice.deviceType == null) && (localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_MODEL_NAME) == null)) {
				DistoBluetoothService.this.m_Handler.post(new Runnable()
				{
				public void run()
				{
					DistoBluetoothService.this.dbDevice.deviceType = localObject;
					DistoBluetoothService.this.dbDevice.updateRecord(DistoBluetoothService.this.context);
					if ((bool1) && ((DistoBluetoothService.this.context instanceof Overview)))
					{
					Object localObject2 = localObject;
					Object localObject1 = localObject2;
					if (!((String)localObject2).equals("Stabila"))
					{
						localObject1 = localObject2;
						if (!((String)localObject2).equals("WDM")) {
						localObject1 = "DISTO_" + (String)localObject2;
						}
					}
					localObject2 = ((LeicaApplication)((Overview)DistoBluetoothService.this.context).getApplication()).getTracker();
					((Tracker)localObject2).send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected").build());
					((Tracker)localObject2).send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected_" + (String)localObject1).build());
					}
				}
				});
			}
			DistoBluetoothService.this.notificationStack.push(localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT));
			bool1 = bool2;
			if (localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION) == null) {
				continue;
			}
			DistoBluetoothService.this.notificationStack.push(localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION));
			DistoBluetoothService.this.notificationStack.push(localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION_DISPLAY_UNIT));
			bool1 = bool2;
			continue;
			}
			if (!DistoBluetoothService.this.currentDevice.getName().toLowerCase().startsWith("WDM".toLowerCase())) {
			break label465;
			}
		}
		catch (Exception paramAnonymousBluetoothGatt)
		{
			JQuery.e(paramAnonymousBluetoothGatt);
			return;
		}
		final Object localObject = "WDM";
		continue;
		label465:
		if (DistoBluetoothService.this.currentDevice.getName().toLowerCase().trim().equals("DISTO".toLowerCase())) {
			localObject = "D510";
		} else if (localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION) != null) {
			localObject = "D810";
		} else if (localBluetoothGattService.getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_INCLINATION) == null) {
			localObject = "D110";
		}
		}
		JQuery.w("SERVICES FOUND: " + bool1);
		if (!bool1) {
		paramAnonymousBluetoothGatt.discoverServices();
		}
		final boolean bool1 = paramAnonymousBluetoothGatt.getServices().isEmpty();
		if (!bool1) {}
	}
	};
	private BluetoothAdapter.LeScanCallback leScanCallback;
	private BluetoothAdapter m_BluetoothAdapter;
	private ConnectThread m_ConnectThread;
	private ConnectedThread m_ConnectedThread;
	public DistoInterpreter m_DistoMachine = new DistoInterpreter();
	private Handler m_Handler;
	private boolean m_btReEnabled = false;
	private int m_iState;
	private Stack<BluetoothGattCharacteristic> notificationStack = new Stack();
	private boolean pairCurrentDevice = false;
	
	private DistoBluetoothService()
	{
	Log.d("DISTO BluetoothService", "DistoBluetoothService");
	this.m_BluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	this.m_iState = 0;
	}
	
	private boolean bondDevice(BluetoothDevice paramBluetoothDevice)
	{
	return true;
	}
	
	private void connect(BluetoothDevice paramBluetoothDevice)
	{
	try
	{
		this.currentDevice = paramBluetoothDevice;
		Log.d("DISTO BluetoothService", "connect to: " + paramBluetoothDevice);
		if ((this.m_iState == 2) && (this.m_ConnectThread != null))
		{
		this.m_ConnectThread.cancel();
		this.m_ConnectThread = null;
		}
		if (this.m_ConnectedThread != null)
		{
		this.m_ConnectedThread.cancel();
		this.m_ConnectedThread = null;
		}
		this.m_ConnectThread = new ConnectThread(paramBluetoothDevice);
		this.m_ConnectThread.start();
		setState(2);
		return;
	}
	finally {}
	}
	
	private void connectBle(final BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
	{
	StringBuilder localStringBuilder = new StringBuilder().append("BTGATT: ");
	if (this.btGatt != null) {}
	for (boolean bool = true;; bool = false)
	{
		JQuery.w(bool + " STATE: " + getState() + " FS: " + this.forceStopped);
		if (!this.forceStopped) {
		break;
		}
		return;
	}
	this.pairCurrentDevice = false;
	if ((this.btGatt != null) && (getState() != 1) && (this.currentDevice != null))
	{
		JQuery.w("DISCONNECTING BTGATT " + this.currentDevice.getAddress());
		this.btGatt.disconnect();
		this.btGatt.close();
		this.btGatt = null;
	}
	this.m_Handler.removeCallbacksAndMessages(null);
	this.currentDevice = paramBluetoothDevice;
	JQuery.e("DEVICE: " + paramBluetoothDevice);
	this.m_BluetoothAdapter = ((BluetoothManager)this.context.getSystemService("bluetooth")).getAdapter();
	this.m_Handler.post(new Runnable()
	{
		public void run()
		{
		DistoBluetoothService.this.btGatt = paramBluetoothDevice.connectGatt(DistoBluetoothService.this.context, false, DistoBluetoothService.this.gattCallback);
		}
	});
	JQuery.e("STOPONFAIL?! " + paramBoolean);
	if (paramBoolean)
	{
		this.m_Handler.postDelayed(new Runnable()
		{
		public void run()
		{
			if (DistoBluetoothService.this.getState() != 3)
			{
			DistoBluetoothService.this.setState(1);
			Message localMessage = DistoBluetoothService.this.m_Handler.obtainMessage(10);
			Bundle localBundle = new Bundle();
			localBundle.putString("toast", DistoBluetoothService.this.context.getString(2131296314));
			localMessage.setData(localBundle);
			DistoBluetoothService.this.m_Handler.sendMessage(localMessage);
			}
		}
		}, 6000L);
		return;
	}
	this.m_Handler.postDelayed(new Runnable()
	{
		public void run()
		{
		if (DistoBluetoothService.this.getState() != 3) {
			DistoBluetoothService.this.findDeviceAndConnect(paramBluetoothDevice.getAddress());
		}
		}
	}, 6000L);
	}
	
	private void connected(BluetoothSocket paramBluetoothSocket, BluetoothDevice paramBluetoothDevice)
	{
	try
	{
		Log.d("DISTO BluetoothService", "connected");
		if (this.m_ConnectThread != null)
		{
		this.m_ConnectThread.cancel();
		this.m_ConnectThread = null;
		}
		if (this.m_ConnectedThread != null)
		{
		this.m_ConnectedThread.cancel();
		this.m_ConnectedThread = null;
		}
		this.m_ConnectedThread = new ConnectedThread(paramBluetoothSocket);
		this.m_ConnectedThread.start();
		if ((this.context instanceof Overview))
		{
		paramBluetoothSocket = ((LeicaApplication)((Overview)this.context).getApplication()).getTracker();
		paramBluetoothSocket.send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected").build());
		paramBluetoothSocket.send(new HitBuilders.EventBuilder().setCategory("Bluetooth").setAction("Connected_Unknown").build());
		}
		paramBluetoothSocket = this.m_Handler.obtainMessage(4);
		Bundle localBundle = new Bundle();
		localBundle.putBoolean("device_isble", false);
		localBundle.putString("device_name", paramBluetoothDevice.getName());
		localBundle.putString("device_address", paramBluetoothDevice.getAddress());
		paramBluetoothSocket.setData(localBundle);
		this.m_Handler.sendMessage(paramBluetoothSocket);
		PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString("device", paramBluetoothDevice.getAddress()).commit();
		setState(3);
		return;
	}
	finally {}
	}
	
	private void connectionFailed()
	{
	setState(1);
	Message localMessage = this.m_Handler.obtainMessage(10);
	Bundle localBundle = new Bundle();
	localBundle.putString("toast", this.context.getString(2131296314));
	localMessage.setData(localBundle);
	this.m_Handler.sendMessage(localMessage);
	}
	
	private void connectionLost()
	{
	setState(1);
	if (!this.forceStopped)
	{
		Message localMessage = this.m_Handler.obtainMessage(5);
		Bundle localBundle = new Bundle();
		localBundle.putString("toast", this.context.getString(2131296312));
		localMessage.setData(localBundle);
		this.m_Handler.sendMessage(localMessage);
		this.forceStopped = false;
		stop();
		return;
	}
	this.forceStopped = false;
	}
	
	private void findDeviceAndConnect(final String paramString)
	{
	if ((paramString == null) || (paramString.isEmpty()) || (this.context == null) || (this.forceStopped)) {
		return;
	}
	JQuery.i("TRYING TO FIND DEVICE! " + paramString + " HASZ: " + this.m_BluetoothAdapter.getBondedDevices().size());
	this.leScanCallback = new BluetoothAdapter.LeScanCallback()
	{
		public void onLeScan(BluetoothDevice paramAnonymousBluetoothDevice, int paramAnonymousInt, byte[] paramAnonymousArrayOfByte)
		{
		JQuery.e("FOUND IN FIND DEVICE: " + paramAnonymousBluetoothDevice.getName() + "\nADV: " + new String(paramAnonymousArrayOfByte));
		if ((paramAnonymousBluetoothDevice.getAddress().equals(paramString)) && (DistoBluetoothService.this.getState() != 3))
		{
			DistoBluetoothService.this.m_BluetoothAdapter.stopLeScan(DistoBluetoothService.this.leScanCallback);
			DistoBluetoothService.this.connectBle(paramAnonymousBluetoothDevice, true);
		}
		}
	};
	this.m_BluetoothAdapter.startLeScan(this.leScanCallback);
	Iterator localIterator = this.m_BluetoothAdapter.getBondedDevices().iterator();
	while (localIterator.hasNext())
	{
		BluetoothDevice localBluetoothDevice = (BluetoothDevice)localIterator.next();
		if ((localBluetoothDevice.getAddress().equals(paramString)) && (getState() != 3))
		{
		this.m_BluetoothAdapter.stopLeScan(this.leScanCallback);
		this.m_Handler.removeCallbacksAndMessages(null);
		connectBle(localBluetoothDevice, true);
		this.m_BluetoothAdapter.stopLeScan(this.leScanCallback);
		return;
		}
	}
	new Handler().postDelayed(new Runnable()
	{
		public void run()
		{
		DistoBluetoothService.this.m_BluetoothAdapter.stopLeScan(DistoBluetoothService.this.leScanCallback);
		if ((DistoBluetoothService.this.getState() != 3) && (!DistoBluetoothService.this.forceStopped) && (DistoBluetoothService.this.getState() != 2)) {
			DistoBluetoothService.this.m_Handler.postDelayed(new Runnable()
			{
			public void run() {}
			}, 2000L);
		}
		}
	}, 15000L);
	}
	
	public static DistoBluetoothService getInstance()
	{
	if (instance == null) {
		instance = new DistoBluetoothService();
	}
	return instance;
	}
	
	private void searchBluetoothProfile(int paramInt, BluetoothProfile paramBluetoothProfile)
	{
	Object localObject1 = PreferenceManager.getDefaultSharedPreferences(this.context).getString("device", null);
	int j = 0;
	Object localObject2 = paramBluetoothProfile.getConnectedDevices().iterator();
	int i;
	BluetoothDevice localBluetoothDevice;
	do
	{
		i = j;
		if (!((Iterator)localObject2).hasNext()) {
		break;
		}
		localBluetoothDevice = (BluetoothDevice)((Iterator)localObject2).next();
		JQuery.d("FOUND DEVICE : " + localBluetoothDevice.getName());
	} while ((!DISTOtransfer.isValidDevice(localBluetoothDevice)) || (localBluetoothDevice.getBondState() != 12));
	if ((localObject1 != null) && (localBluetoothDevice.getAddress().equals(localObject1)))
	{
		i = 1;
		if ((localBluetoothDevice.getType() == 2) || (localBluetoothDevice.getType() == 3))
		{
		connectBle(localBluetoothDevice, true);
		label149:
		if (i == 0)
		{
			localObject1 = paramBluetoothProfile.getConnectedDevices().iterator();
			while (((Iterator)localObject1).hasNext())
			{
			localObject2 = (BluetoothDevice)((Iterator)localObject1).next();
			if ((DISTOtransfer.isValidDevice((BluetoothDevice)localObject2)) && (((BluetoothDevice)localObject2).getBondState() == 12))
			{
				if ((((BluetoothDevice)localObject2).getType() != 2) && (((BluetoothDevice)localObject2).getType() != 3)) {
				break label294;
				}
				connectBle((BluetoothDevice)localObject2, true);
			}
			}
		}
		}
	}
	for (;;)
	{
		this.m_BluetoothAdapter.closeProfileProxy(paramInt, paramBluetoothProfile);
		return;
		connect(localBluetoothDevice);
		break label149;
		if (localObject1 != null) {
		break;
		}
		i = 1;
		if ((localBluetoothDevice.getType() == 2) || (localBluetoothDevice.getType() == 3))
		{
		connectBle(localBluetoothDevice, true);
		break label149;
		}
		connect(localBluetoothDevice);
		break label149;
		label294:
		connect((BluetoothDevice)localObject2);
	}
	}
	
	private void setState(int paramInt)
	{
	try
	{
		Log.d("DISTO BluetoothService", "setState() " + this.m_iState + " -> " + paramInt);
		this.m_iState = paramInt;
		if (this.m_Handler != null) {
		this.m_Handler.obtainMessage(1, paramInt, -1).sendToTarget();
		}
		return;
	}
	finally
	{
		localObject = finally;
		throw ((Throwable)localObject);
	}
	}
	
	private void start()
	{
	try
	{
		Log.d("DISTO BluetoothService", "start service");
		if (this.m_ConnectThread != null)
		{
		this.m_ConnectThread.cancel();
		this.m_ConnectThread = null;
		}
		if (this.m_ConnectedThread != null)
		{
		this.m_ConnectedThread.cancel();
		this.m_ConnectedThread = null;
		}
		setState(1);
		return;
	}
	finally {}
	}
	
	public void SendCommand(int paramInt)
	{
	try
	{
		if (this.m_iState != 3) {
		return;
		}
		ConnectedThread localConnectedThread = this.m_ConnectedThread;
		if (localConnectedThread != null)
		{
		localConnectedThread.SendCommand(paramInt);
		return;
		}
	}
	finally {}
	}
	
	public void connectDevice(DistoDevice paramDistoDevice, final BluetoothDevice paramBluetoothDevice, boolean paramBoolean)
	{
	this.dbDevice = paramDistoDevice;
	this.forceStopped = false;
	if ((paramBluetoothDevice.getType() == 2) || (paramBluetoothDevice.getType() == 3))
	{
		if (this.context != null) {
		PreferenceManager.getDefaultSharedPreferences(this.context).edit().putString("device", paramBluetoothDevice.getAddress()).commit();
		}
		this.m_Handler.post(new Runnable()
		{
		public void run()
		{
			DistoBluetoothService.this.connectBle(paramBluetoothDevice, false);
		}
		});
		return;
	}
	this.m_Handler.post(new Runnable()
	{
		public void run()
		{
		DistoBluetoothService.this.connect(paramBluetoothDevice);
		}
	});
	}
	
	public void connectToConnectedDevices()
	{
	if ((getState() == 3) || (getState() == 2) || (this.context == null)) {
		return;
	}
	Object localObject = ((BluetoothManager)this.context.getSystemService("bluetooth")).getConnectedDevices(7);
	JQuery.i("FOUND DEVICES SIZE: " + ((List)localObject).size());
	localObject = ((List)localObject).iterator();
	while (((Iterator)localObject).hasNext())
	{
		BluetoothDevice localBluetoothDevice = (BluetoothDevice)((Iterator)localObject).next();
		JQuery.i("FOUND CONNECTED DEVICE: " + localBluetoothDevice.getName());
	}
	this.m_BluetoothAdapter.getProfileProxy(this.context, new BluetoothProfile.ServiceListener()
	{
		public void onServiceConnected(int paramAnonymousInt, BluetoothProfile paramAnonymousBluetoothProfile)
		{
		JQuery.e("ON SERVICE CONNECTED: " + paramAnonymousBluetoothProfile);
		DistoBluetoothService.this.searchBluetoothProfile(paramAnonymousInt, paramAnonymousBluetoothProfile);
		}
		
		public void onServiceDisconnected(int paramAnonymousInt) {}
	}, 4);
	this.m_BluetoothAdapter.getProfileProxy(this.context, new BluetoothProfile.ServiceListener()
	{
		public void onServiceConnected(int paramAnonymousInt, BluetoothProfile paramAnonymousBluetoothProfile)
		{
		JQuery.e("ON SERVICE CONNECTED: " + paramAnonymousBluetoothProfile + " COUNT: " + paramAnonymousBluetoothProfile.getConnectedDevices().size());
		DistoBluetoothService.this.searchBluetoothProfile(paramAnonymousInt, paramAnonymousBluetoothProfile);
		}
		
		public void onServiceDisconnected(int paramAnonymousInt) {}
	}, 7);
	}
	
	public void disconnectIfNeeded()
	{
	if (this.btGatt != null)
	{
		JQuery.e("KILLING CURRENT DEVICE!!!");
		setState(1);
		this.m_Handler.removeCallbacksAndMessages(null);
		this.m_BluetoothAdapter.stopLeScan(this.leScanCallback);
		this.btGatt.disconnect();
		this.btGatt.close();
		this.dbDevice = null;
		return;
	}
	stop();
	}
	
	public BluetoothAdapter getBluetoothManager()
	{
	return this.m_BluetoothAdapter;
	}
	
	public ConnectedThread getConnectedThread()
	{
	return this.m_ConnectedThread;
	}
	
	public BluetoothDevice getCurrentDevice()
	{
	return this.currentDevice;
	}
	
	public DistoDevice getDbDevice()
	{
	return this.dbDevice;
	}
	
	public int getState()
	{
	try
	{
		int i = this.m_iState;
		return i;
	}
	finally
	{
		localObject = finally;
		throw ((Throwable)localObject);
	}
	}
	
	public boolean sendCommandBle(char paramChar)
	{
	if ((this.btGatt == null) || (getState() != 3) || (this.btGatt.getService(DISTOtransfer.DISTO_SERVICE) == null) || (this.btGatt.getService(DISTOtransfer.DISTO_SERVICE).getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_COMMAND) == null)) {}
	BluetoothGatt localBluetoothGatt;
	BluetoothGattCharacteristic localBluetoothGattCharacteristic;
	do
	{
		return false;
		localBluetoothGatt = this.btGatt;
		localBluetoothGattCharacteristic = localBluetoothGatt.getService(DISTOtransfer.DISTO_SERVICE).getCharacteristic(DISTOtransfer.DISTO_CHARACTERISTIC_COMMAND);
		JQuery.i("clientConfig: " + localBluetoothGattCharacteristic);
	} while (localBluetoothGattCharacteristic == null);
	localBluetoothGattCharacteristic.setValue(new byte[] { (byte)paramChar });
	return localBluetoothGatt.writeCharacteristic(localBluetoothGattCharacteristic);
	}
	
	public void setForceStopped(boolean paramBoolean)
	{
	this.forceStopped = paramBoolean;
	}
	
	public void setup(Handler paramHandler)
	{
	if ((this.m_Handler != null) && (this.leScanCallback != null) && (this.context != null))
	{
		BluetoothAdapter localBluetoothAdapter = ((BluetoothManager)this.context.getSystemService("bluetooth")).getAdapter();
		if (this.m_BluetoothAdapter != null) {
		localBluetoothAdapter.stopLeScan(this.leScanCallback);
		}
		this.m_Handler.removeCallbacksAndMessages(null);
	}
	this.m_Handler = paramHandler;
	}
	
	public void setupContext(Context paramContext)
	{
	this.context = paramContext;
	}
	
	public void setupInitial(Context paramContext)
	{
	this.context = paramContext;
	this.forceStopped = false;
	IntentFilter localIntentFilter = new IntentFilter();
	localIntentFilter.addAction("android.bluetooth.device.action.BOND_STATE_CHANGED");
	this.bluetoothBondReceiver = new BluetoothBondReceiver(null);
	paramContext.registerReceiver(this.bluetoothBondReceiver, localIntentFilter);
	this.m_DistoMachine.UpdateSettings(PreferenceManager.getDefaultSharedPreferences(paramContext), paramContext.getResources());
	}
	
	public void stop()
	{
	try
	{
		Log.d("DISTO BluetoothService", "stop");
		setForceStopped(true);
		if (this.btGatt != null) {
		JQuery.e("disconnecting gatt");
		}
		if (this.m_ConnectThread != null)
		{
		this.m_ConnectThread.cancel();
		this.m_ConnectThread = null;
		}
		if (this.m_ConnectedThread != null)
		{
		this.m_ConnectedThread.cancel();
		this.m_ConnectedThread = null;
		}
		setState(0);
		return;
	}
	finally {}
	}
	
	private class BluetoothBondReceiver
	extends BroadcastReceiver
	{
	private BluetoothBondReceiver() {}
	
	public void onReceive(Context paramContext, Intent paramIntent)
	{
		paramContext = paramIntent.getAction();
		BluetoothDevice localBluetoothDevice = (BluetoothDevice)paramIntent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
		if (localBluetoothDevice != null)
		{
		int i = paramIntent.getIntExtra("android.bluetooth.device.extra.BOND_STATE", 10);
		JQuery.i("BOND ACTION: " + paramContext + " STATE: " + i);
		if ((DistoBluetoothService.this.currentDevice != null) && (localBluetoothDevice.equals(DistoBluetoothService.this.currentDevice)) && ("android.bluetooth.device.action.BOND_STATE_CHANGED".equals(paramContext)))
		{
			JQuery.i("BOND STATE CHANGED!!!! " + i + " STATE: " + DistoBluetoothService.this.getState());
			if ((i == 12) && (DistoBluetoothService.this.btGatt != null) && ((DistoBluetoothService.this.getState() == 3) || (DistoBluetoothService.this.getState() == 2))) {
			JQuery.i("discovering services: " + DistoBluetoothService.this.btGatt.discoverServices());
			}
		}
		}
	}
	}
	
	private class ConnectThread extends Thread
	{
	private final BluetoothDevice m_BluetoothDevice;
	private final BluetoothSocket m_BluetoothSocket;
	
	public ConnectThread(BluetoothDevice paramBluetoothDevice)
	{
		this.m_BluetoothDevice = paramBluetoothDevice;
		paramBluetoothDevice = null;
		try
		{
		Log.d("DISTO BluetoothService", "Try createRfcommSocketToServiceRecord");
		BluetoothSocket localBluetoothSocket = this.m_BluetoothDevice.createInsecureRfcommSocketToServiceRecord(DistoBluetoothService.MY_UUID);
		this$1 = localBluetoothSocket;
		}
		catch (IOException localIOException)
		{
		for (;;)
		{
			Log.e("DISTO BluetoothService", "device.create..Record() failed", localIOException);
			DistoBluetoothService.this.m_Handler.obtainMessage(6, 1, -1).sendToTarget();
			this$1 = paramBluetoothDevice;
		}
		}
		this.m_BluetoothSocket = DistoBluetoothService.this;
	}
	
		public void cancel()
		{
			try
			{
				this.m_BluetoothSocket.close();
			}
			catch (IOException localIOException)
			{
				Log.e("DISTO BluetoothService", "close() of connect socket failed", localIOException);
				DistoBluetoothService.this.m_Handler.obtainMessage(6, 1, -1).sendToTarget();
			}
		}
	
		public void run()
		{
			Log.i("DISTO BluetoothService", "BEGIN mConnectThread");
			setName("ConnectThread");
			if (DistoBluetoothService.this.m_BluetoothAdapter.isDiscovering()) {
			DistoBluetoothService.this.m_BluetoothAdapter.cancelDiscovery();
			}
			try
			{
			Thread.sleep(50L, 0);
			Log.d("DISTO BluetoothService", "m_BluetoothSocket.connect()...");
			this.m_BluetoothSocket.connect();
			}
			catch (IOException localIOException1)
			{
			synchronized (DistoBluetoothService.this)
			{
				DistoBluetoothService.access$1502(DistoBluetoothService.this, null);
				DistoBluetoothService.this.connected(this.m_BluetoothSocket, this.m_BluetoothDevice);
				return;
				localIOException1 = localIOException1;
				Log.e("DISTO BluetoothService", "m_BluetoothSocket.connect() failed", localIOException1);
				DistoBluetoothService.this.connectionFailed();
				try
				{
				Log.d("DISTO BluetoothService", "Try BluetoothSocket.close()");
				this.m_BluetoothSocket.close();
				DistoBluetoothService.this.start();
				return;
				}
				catch (IOException localIOException2)
				{
				for (;;)
				{
					Log.e("DISTO BluetoothService", "Error m_BluetoothSocket.close() socket during connection", localIOException2);
					DistoBluetoothService.this.m_Handler.obtainMessage(6, 1, -1).sendToTarget();
				}
				}
			}
			}
			catch (InterruptedException localInterruptedException)
			{
			for (;;) {}
			}
		}
	}
	
	private class ConnectedThread extends Thread
	{
		private final InputStream mmInStream;
		private final OutputStream mmOutStream;
		private final BluetoothSocket mmSocket;
	
		public ConnectedThread(BluetoothSocket paramBluetoothSocket)
		{
			Log.d("DISTO BluetoothService", "create ConnectedThread");
			this.mmSocket = paramBluetoothSocket;
			Object localObject1 = null;
			localObject2 = null;
			try
			{
			InputStream localInputStream = paramBluetoothSocket.getInputStream();
			localObject1 = localInputStream;
			paramBluetoothSocket = paramBluetoothSocket.getOutputStream();
			this$1 = paramBluetoothSocket;
			localObject1 = localInputStream;
			}
			catch (IOException paramBluetoothSocket)
			{
			for (;;)
			{
				Log.e("DISTO BluetoothService", "temp sockets not created", paramBluetoothSocket);
				DistoBluetoothService.this.m_Handler.obtainMessage(6, 3, -1).sendToTarget();
				this$1 = (DistoBluetoothService)localObject2;
			}
			}
			this.mmInStream = ((InputStream)localObject1);
			this.mmOutStream = DistoBluetoothService.this;
		}
	
		public boolean SendCommand(int paramInt)
		{
			if (DistoBluetoothService.this.m_DistoMachine.hasDistoCmd(paramInt))
			{
			Log.d("DISTO BluetoothService", "Send: " + DistoBluetoothService.this.m_DistoMachine.getDistoCmd(paramInt));
			write(DistoBluetoothService.this.m_DistoMachine.getDistoCmd(paramInt).getBytes());
			write("\r\n".getBytes());
			}
			return false;
		}
	
		public void cancel()
		{
			try
			{
			this.mmSocket.close();
			return;
			}
			catch (IOException localIOException)
			{
			Log.e("DISTO BluetoothService", "close() of connect socket failed", localIOException);
			}
		}
	
		public void run()
		{
			Log.d("DISTO BluetoothService", "BEGIN mConnectedThread");
			byte[] arrayOfByte = new byte['࿐'];
			Ringbuffer localRingbuffer = new Ringbuffer(8000);
			for (;;)
			{
			int j;
			try
			{
				Thread.sleep(50L);
				SendCommand(5);
				try
				{
				localRingbuffer.insert(arrayOfByte, this.mmInStream.read(arrayOfByte));
				if (!localRingbuffer.contains((byte)13)) {
					continue;
				}
				localRingbuffer.dequeueUntil("@?!0135246789".getBytes("UTF8"));
				Object localObject1 = new String(arrayOfByte, 0, localRingbuffer.getLine(arrayOfByte, (byte)13));
				Log.d("DISTO BluetoothService", "line: " + (String)localObject1);
				localObject1 = ((String)localObject1).split("[ \t\n\r]");
				arrayOfMeasurementSet = new MeasurementSet[10];
				i = 0;
				k = 0;
				if (k >= localObject1.length) {
					continue;
				}
				DistoBluetoothService.this.m_DistoMachine.AnalyzeAndInterpretGsiItem(localObject1[k]);
				j = i;
				switch (DistoBluetoothService.this.m_DistoMachine.m_eResultType)
				{
				case 1:
					if (arrayOfMeasurementSet[i] == null) {
					arrayOfMeasurementSet[i] = new MeasurementSet(null);
					}
					j = i;
					if (arrayOfMeasurementSet[i].resultDistance != null)
					{
					j = i + 1;
					arrayOfMeasurementSet[j] = new MeasurementSet(null);
					}
					arrayOfMeasurementSet[j].resultDistance = DistoBluetoothService.this.m_DistoMachine.m_ResultString;
					arrayOfMeasurementSet[j].resultDistanceUnit = DistoBluetoothService.this.m_DistoMachine.m_ResultUnitString;
				}
				}
				catch (IOException localIOException)
				{
				Log.e("DISTO BluetoothService", "disconnected", localIOException);
				DistoBluetoothService.this.connectionLost();
				return;
				}
				if (arrayOfMeasurementSet[i] == null) {
				arrayOfMeasurementSet[i] = new MeasurementSet(null);
				}
				j = i;
				if (arrayOfMeasurementSet[i].resultAngle != null)
				{
				j = i + 1;
				arrayOfMeasurementSet[j] = new MeasurementSet(null);
				}
				arrayOfMeasurementSet[j].resultAngle = DistoBluetoothService.this.m_DistoMachine.m_ResultString;
				arrayOfMeasurementSet[j].resultAngleUnit = DistoBluetoothService.this.m_DistoMachine.m_ResultUnitString;
				continue;
				j = (int)Double.parseDouble(DistoBluetoothService.this.m_DistoMachine.m_ResultString);
				DistoBluetoothService.this.m_Handler.obtainMessage(8, j, -1, null).sendToTarget();
				j = i;
				continue;
				JQuery.e("sendConfirmation: " + DistoBluetoothService.this.m_DistoMachine.m_SendConfirmation);
				if ((DistoBluetoothService.this.m_DistoMachine.m_SendConfirmation > 0) && (DistoBluetoothService.this.m_DistoMachine.mDistoType != 0))
				{
				SendCommand(9);
				continue;
				if (j >= arrayOfMeasurementSet.length) {
					continue;
				}
				if ((arrayOfMeasurementSet[j] == null) || ((arrayOfMeasurementSet[j].resultDistance == null) && (arrayOfMeasurementSet[j].resultAngle == null))) {
					break label861;
				}
				DistoMeasurement localDistoMeasurement = new DistoMeasurement();
				i = 0;
				localDistoMeasurement.m_Time = new Time();
				localDistoMeasurement.m_Time.setToNow();
				localDistoMeasurement.m_bMetric = DistoBluetoothService.this.m_DistoMachine.m_bResultMetric;
				localDistoMeasurement.m_nMetricDezimals = DistoBluetoothService.this.m_DistoMachine.m_nMetricResultDezimals;
				if (DistoBluetoothService.this.m_DistoMachine.m_bResultUseEnter) {
					localDistoMeasurement.m_bEnter = true;
				}
				if (DistoBluetoothService.this.m_DistoMachine.m_bResultUseTab) {
					localDistoMeasurement.m_bTab = true;
				}
				if (arrayOfMeasurementSet[j].resultDistance != null)
				{
					localDistoMeasurement.m_Distance = arrayOfMeasurementSet[j].resultDistance;
					String str = arrayOfMeasurementSet[j].resultDistance;
					Object localObject2 = str;
					if (str.contains(" ")) {
					localObject2 = str.substring(0, str.indexOf(" "));
					}
					localDistoMeasurement.m_DistanceOriginal = Float.valueOf(((String)localObject2).replace(",", ".")).floatValue();
					if (!DistoBluetoothService.this.m_DistoMachine.m_bResultUseUnit) {
					break label868;
					}
					localDistoMeasurement.m_DistanceUnit = arrayOfMeasurementSet[j].resultDistanceUnit;
					break label868;
				}
				if (arrayOfMeasurementSet[j].resultAngle != null)
				{
					localDistoMeasurement.m_Angle = arrayOfMeasurementSet[j].resultAngle;
					if (!DistoBluetoothService.this.m_DistoMachine.m_bResultUseUnit) {
					break label873;
					}
					localDistoMeasurement.m_AngleUnit = arrayOfMeasurementSet[j].resultAngleUnit;
					break label873;
				}
				if (i == 0) {
					break label861;
				}
				DistoBluetoothService.this.m_Handler.obtainMessage(9, -1, -1, localDistoMeasurement).sendToTarget();
				}
			}
			catch (InterruptedException localInterruptedException)
			{
				MeasurementSet[] arrayOfMeasurementSet;
				int k;
				continue;
				j = i;
				k += 1;
				i = j;
				continue;
				if (arrayOfMeasurementSet[0] == null) {
				continue;
				}
				j = 0;
				continue;
			}
			label861:
			j += 1;
			continue;
			label868:
			int i = 1;
			continue;
			label873:
			i = 1;
			}
		}
	
		public void write(byte[] paramArrayOfByte)
		{
			try
			{
				this.mmOutStream.write(paramArrayOfByte);
			}
			catch (IOException err)
			{
				Log.e("DISTO BluetoothService", "Exception during write", err);
				DistoBluetoothService.this.m_Handler.obtainMessage(6, 4, -1).sendToTarget();
			}
		}
	
		private class MeasurementSet
		{
			public String resultAngle = null;
			public String resultAngleUnit = null;
			public String resultDistance = null;
			public String resultDistanceUnit = null;

			private MeasurementSet() {}
		}
	}
}

