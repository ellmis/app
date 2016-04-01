package rtu.bachelor.elmarstimoskans.bachelor;

import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import rtu.bachelor.elmarstimoskans.bachelor.activities.DeviceList;

public class MainActivity extends AppCompatActivity {

    static int REQUEST_ENABLE_BT = 1;

    BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bluetoothIcon = (Button) findViewById(R.id.scan_button);
        bluetoothIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkBluetoothCon();
            }
        });
    }

    private boolean checkBluetoothCon() {
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            // Device does not support Bluetooth
            showNotSupportedDialog();
            return false;
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                showBluetoothOnDialog();
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                return true;
            } else {
                Intent deviceList = new Intent(this, DeviceList.class);
                startActivity(deviceList);
                return true;
            }
        }
    }

    public void showNotSupportedDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.bt_not_supported)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showBluetoothOnDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this)
                .setMessage(R.string.bt_not_supported)
                .setCancelable(false)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();


    }


}
