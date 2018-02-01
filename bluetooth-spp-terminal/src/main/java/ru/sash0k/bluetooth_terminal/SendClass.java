package ru.sash0k.bluetooth_terminal;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import ru.sash0k.bluetooth_terminal.bluetooth.BluetoothCallback;
import ru.sash0k.bluetooth_terminal.bluetooth.BluetoothResponseHandler;
import ru.sash0k.bluetooth_terminal.bluetooth.DeviceConnector;


public class SendClass {

    private static DeviceConnector connector;
    private final Context context;
    private static BluetoothResponseHandler mHandler;

    public SendClass(Context context, BluetoothResponseHandler handler) {
        this.context = context;
        mHandler = handler;
    }

    public boolean isConnected() {
        return (connector != null) && (connector.getState() == DeviceConnector.STATE_CONNECTED);
    }

    public void stopConnection() {
        if (connector != null) {
            connector.stop();
            connector = null;
        }
    }

    public void setupConnector(BluetoothDevice connectedDevice) {
        stopConnection();
        try {
            String emptyName = context.getString(R.string.empty_device_name);
            DeviceData data = new DeviceData(connectedDevice, emptyName);
            connector = new DeviceConnector(data, mHandler);
            connector.connect();
        } catch (IllegalArgumentException e) {
            Utils.log("setupConnector failed: " + e.getMessage());
          }
    }

    public void sendFunc(String commandText) {
        if (isConnected()) {
            byte[] command = (commandText.getBytes());
            command = Utils.concat(command, "/n".getBytes());
            connector.write(command);
        }
    }
}
