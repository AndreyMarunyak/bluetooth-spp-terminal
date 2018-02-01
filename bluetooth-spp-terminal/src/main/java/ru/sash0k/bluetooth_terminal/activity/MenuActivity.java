package ru.sash0k.bluetooth_terminal.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ru.sash0k.bluetooth_terminal.R;
import ru.sash0k.bluetooth_terminal.SendClass;
import ru.sash0k.bluetooth_terminal.Utils;
import ru.sash0k.bluetooth_terminal.bluetooth.BluetoothCallback;
import ru.sash0k.bluetooth_terminal.bluetooth.BluetoothResponseHandler;
import ru.sash0k.bluetooth_terminal.bluetooth.DeviceListActivity;

public class MenuActivity extends BaseActivity {

    public static final String COMMAND_ON = "1";
    public static final String COMMAND_OFF = "0";
    boolean check = false;
    private SendClass turn;
    private Button myBtn;
    private BluetoothResponseHandler handler;
    private BluetoothCallback bluetoothCallback = new MenuBluetoothCallback();

    public static void show(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        handler = new BluetoothResponseHandler(bluetoothCallback);
        turn = new SendClass(this, handler);
        myBtn = findViewById(R.id.lamp_button);
        myBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLampButtonClicked();
            }
        });



    }

    public void onLampButtonClicked() {
        if (check) {
            check = false;
            myBtn.setText(getString(R.string.menu_activity_on));
            turn.sendFunc(COMMAND_OFF);
        } else {
            myBtn.setText(getString(R.string.menu_activity_off));
            check = true;
            turn.sendFunc(COMMAND_ON);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_menu_activity, menu);
        final MenuItem bluetooth = menu.findItem(R.id.menu_search);
        if (bluetooth != null) bluetooth.setIcon(turn.isConnected() ?
                R.drawable.ic_action_device_bluetooth_connected :
                R.drawable.ic_action_device_bluetooth);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            if (super.isAdapterReady()) {
                if (turn.isConnected()) {
                    turn.stopConnection();
                } else {
                    startDeviceListActivity();
                }
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
            return true;
        } else if (item.getItemId() == R.id.menu_terminal){
            Intent intent = new Intent(this, DeviceControlActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void startDeviceListActivity() {
        turn.stopConnection();
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    BluetoothDevice device = btAdapter.getRemoteDevice(address);
                    if (super.isAdapterReady()) {
                        turn.setupConnector(device);
                    }
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                super.pendingRequestEnableBt = false;
                if (resultCode != Activity.RESULT_OK) {
                    Utils.log("BT not enabled");
                }
                break;
        }
    }

    private class MenuBluetoothCallback implements BluetoothCallback {

        @Override
        public void onStateConnected() {
            getActionBar().setSubtitle(getString(R.string.msg_connected));
            invalidateOptionsMenu();
        }

        @Override
        public void onStateConnecting() {
            getActionBar().setSubtitle(getString(R.string.msg_connecting));
            invalidateOptionsMenu();
        }

        @Override
        public void onStateNone() {
            getActionBar().setSubtitle(getString(R.string.msg_not_connected));
            invalidateOptionsMenu();
        }

        @Override
        public void onMessageText(String message) {
            switch (message)
            {
                case "OFF\r\n": {
                    myBtn.setText(getString(R.string.menu_activity_on));
                    check = false;
                }
                case "ON\r\n": {
                    myBtn.setText(getString(R.string.menu_activity_off));
                    check = true;
                }
            }
        }

        @Override
        public void onMessageDevice(String deviceName) {
            getActionBar().setSubtitle(deviceName);
            turn.sendFunc("?");
        }
    }
}
