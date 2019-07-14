package org.afc.apoc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {
        System.out.println("view " + view.getId());
        Log.i("tt", "view " + view.getId());

        Intent intent = new Intent(this, MsgSentActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra("msg", message);
        startActivity(intent);
        ClientSocketIntentService.enqueueWork(getBaseContext(), ClientSocketIntentService.class, 100, intent);
    }

    public void startListen(View view) {
        Intent intent = new Intent(this, ServerSocketIntentService.class);
        intent.putExtra("port", "11224");
        ServerSocketIntentService.enqueueWork(getBaseContext(), ServerSocketIntentService.class, 100, intent);
//    startActivity(intent);
    }
}
