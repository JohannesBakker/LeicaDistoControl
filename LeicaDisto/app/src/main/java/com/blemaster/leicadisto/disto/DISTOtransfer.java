package com.blemaster.leicadisto.disto;

import android.bluetooth.BluetoothDevice;
import de.ffuf.leica.sketchlibrary.classes.JQuery;
import java.util.UUID;

public class DISTOtransfer
{
  private static final String[] ALLOWED_DEVICES = { "DISTO".toLowerCase(), "WDM".toLowerCase(), "STABILA".toLowerCase(), "DEWALT".toLowerCase(), "STANLEY".toLowerCase() };
  public static final UUID DISTO_CHARACTERISTIC_COMMAND;
  public static final UUID DISTO_CHARACTERISTIC_DISTANCE;
  public static final UUID DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT;
  public static final UUID DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION;
  public static final UUID DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION_DISTPLAY_UNIT;
  public static final UUID DISTO_CHARACTERISTIC_HORIZONTAL_INCLINE;
  public static final UUID DISTO_CHARACTERISTIC_INCLINATION;
  public static final UUID DISTO_CHARACTERISTIC_INCLINATION_DISPLAY_UNIT;
  public static final UUID DISTO_CHARACTERISTIC_MODEL_NAME = UUID.fromString("3ab1010c-f831-4395-b29d-570977d5bf94");
  public static final UUID DISTO_CHARACTERISTIC_STATE_RESPONSE;
  public static final UUID DISTO_CHARACTERISTIC_VERTICAL_INCLINE;
  public static final UUID DISTO_DESCRIPTOR = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");
  public static final UUID DISTO_SERVICE = UUID.fromString("3ab10100-f831-4395-b29d-570977d5bf94");
  
  static
  {
    DISTO_CHARACTERISTIC_DISTANCE = UUID.fromString("3ab10101-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_DISTANCE_DISPLAY_UNIT = UUID.fromString("3ab10102-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_INCLINATION = UUID.fromString("3ab10103-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_INCLINATION_DISPLAY_UNIT = UUID.fromString("3ab10104-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION = UUID.fromString("3ab10105-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_GEOGRAPHIC_DIRECTION_DISTPLAY_UNIT = UUID.fromString("3ab10106-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_HORIZONTAL_INCLINE = UUID.fromString("3ab10107-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_VERTICAL_INCLINE = UUID.fromString("3ab10108-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_COMMAND = UUID.fromString("3ab10109-f831-4395-b29d-570977d5bf94");
    DISTO_CHARACTERISTIC_STATE_RESPONSE = UUID.fromString("3ab1010a-f831-4395-b29d-570977d5bf94");
  }
  
  public static boolean isValidDevice(BluetoothDevice paramBluetoothDevice)
  {
    if ((paramBluetoothDevice == null) || (paramBluetoothDevice.getName() == null)) {}
    for (;;)
    {
      return false;
      paramBluetoothDevice = paramBluetoothDevice.getName().toLowerCase();
      String[] arrayOfString = ALLOWED_DEVICES;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        if (paramBluetoothDevice.startsWith(arrayOfString[i])) {
          return true;
        }
        i += 1;
      }
    }
  }
  
  public static boolean isValidDevice(String paramString)
  {
    if (paramString == null) {}
    for (;;)
    {
      return false;
      paramString = paramString.toLowerCase();
      JQuery.i("ALLOWED DEVICE: " + ALLOWED_DEVICES[0] + " NAME: " + paramString);
      String[] arrayOfString = ALLOWED_DEVICES;
      int j = arrayOfString.length;
      int i = 0;
      while (i < j)
      {
        if (paramString.startsWith(arrayOfString[i])) {
          return true;
        }
        i += 1;
      }
    }
  }
}

