package ru.sash0k.bluetooth_terminal;

import android.bluetooth.BluetoothDevice;

import ru.sash0k.bluetooth_terminal.activity.BaseActivity;
import ru.sash0k.bluetooth_terminal.bluetooth.DeviceConnector;


public class SendClass extends BaseActivity {


    private static DeviceConnector connector;
    private String deviceName;
//    private static BluetoothResponseHandler mHandler;


    public String command_ending;


    private String getCommandEnding() {
        String result = Utils.getPrefence(this, getString(R.string.pref_commands_ending));
        if (result.equals("\\r\\n")) result = "\r\n";
        else if (result.equals("\\n")) result = "\n";
        else if (result.equals("\\r")) result = "\r";
        else result = "";
        return result;
    }

    private boolean isConnected() {
        return (connector != null) && (connector.getState() == DeviceConnector.STATE_CONNECTED);
    }

    private void stopConnection() {
        if (connector != null) {
            connector.stop();
            connector = null;
            deviceName = null;
        }
    }

    private void setupConnector(BluetoothDevice connectedDevice) {
        stopConnection();
        try {
            String emptyName = getString(R.string.empty_device_name);
            DeviceData data = new DeviceData(connectedDevice, emptyName);
//            connector = new DeviceConnector(data, mHandler);
//            connector.connect();
        } catch (IllegalArgumentException e) {
            Utils.log("setupConnector failed: " + e.getMessage());
        }
    }

    public void sendFunc(String commandText) {


        byte[] command = (true ? Utils.toHex(commandText) : commandText.getBytes());
        if (true) {
            if (command_ending != null) command = Utils.concat(command, "/n".getBytes());
            connector.write(command);

        }

    }


}
