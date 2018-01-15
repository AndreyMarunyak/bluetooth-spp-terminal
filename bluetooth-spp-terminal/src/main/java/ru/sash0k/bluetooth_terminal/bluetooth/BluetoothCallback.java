package ru.sash0k.bluetooth_terminal.bluetooth;

public interface BluetoothCallback {

    void onStateConnected();

    void onStateConnecting();

    void onStateNone();

    void onMessageText(String message);

    void onMessageDevice(String deviceName);
}
