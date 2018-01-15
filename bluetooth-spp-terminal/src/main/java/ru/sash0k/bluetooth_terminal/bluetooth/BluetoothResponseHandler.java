package ru.sash0k.bluetooth_terminal.bluetooth;

import android.os.Handler;
import android.os.Message;

import ru.sash0k.bluetooth_terminal.Utils;
import ru.sash0k.bluetooth_terminal.activity.BaseActivity;

/**
 * Обработчик приёма данных от bluetooth-потока
 */
public class BluetoothResponseHandler extends Handler {

    private final BluetoothCallback bluetoothCallback;


    public BluetoothResponseHandler(BluetoothCallback bluetoothCallback) {
        this.bluetoothCallback = bluetoothCallback;
    }

    @Override
    public void handleMessage(Message msg) {
        if (bluetoothCallback != null) {
            switch (msg.what) {
                case BaseActivity.MESSAGE_STATE_CHANGE:

                    Utils.log("MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case DeviceConnector.STATE_CONNECTED:
                            bluetoothCallback.onStateConnected();
                            break;
                        case DeviceConnector.STATE_CONNECTING:
                            bluetoothCallback.onStateConnecting();
                            break;
                        case DeviceConnector.STATE_NONE:
                            bluetoothCallback.onStateNone();
                            break;
                    }
                    break;

                case BaseActivity.MESSAGE_READ:
                    final String readMessage = (String) msg.obj;
                    if (readMessage != null) {
                        bluetoothCallback.onMessageText(readMessage);
                    }
                    break;

                case BaseActivity.MESSAGE_DEVICE_NAME:
                    bluetoothCallback.onMessageDevice((String) msg.obj);
                    break;

                case BaseActivity.MESSAGE_WRITE:
                    // stub
                    break;

                case BaseActivity.MESSAGE_TOAST:
                    // stub
                    break;
            }
        }
    }
}
