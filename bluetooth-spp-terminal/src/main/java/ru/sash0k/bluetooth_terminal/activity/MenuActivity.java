package ru.sash0k.bluetooth_terminal.activity;

import android.app.Activity;
//import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;




import ru.sash0k.bluetooth_terminal.R;



public class MenuActivity extends Activity {

    boolean check = false;


    public static void show(Context context) {
        Intent intent = new Intent(context, MenuActivity.class);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

    }

    DeviceControlActivity Turn = new DeviceControlActivity();




    public void LampCommand(View view) {
        Button myBtn = (Button) findViewById(R.id.button);



        if (check) {
            check = false;
            myBtn.setText("On");
            Turn.sendFunc("1");
        }
            else {
            myBtn.setText("Off");
            check = true;
            Turn.sendFunc("0");
            }

    }
}
