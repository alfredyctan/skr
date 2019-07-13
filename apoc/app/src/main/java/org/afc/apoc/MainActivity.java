package org.afc.apoc;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

        try {
            new Thread(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void run() {
                    try (ServerSocket ss = new ServerSocket(12240)) {
                        Socket socket = ss.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            ).start();

            for (final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces!= null && interfaces.hasMoreElements(); ) {
                final NetworkInterface cur = interfaces.nextElement();
                if (cur.isLoopback()) {
                    continue;
                }
                System.out.println("interface " + cur.getName());

                for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                    final InetAddress inet_addr = addr.getAddress();
                    if (!(inet_addr instanceof Inet4Address)) {
                        continue;
                    }
                    System.out.println("  address: " + inet_addr.getHostAddress() + "/" + addr.getNetworkPrefixLength());
                    System.out.println("  broadcast address: " + addr.getBroadcast().getHostAddress());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, MsgSentActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra("msg", message);
        startActivity(intent);

    }
}
