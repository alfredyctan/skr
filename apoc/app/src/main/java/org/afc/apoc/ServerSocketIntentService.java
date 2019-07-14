package org.afc.apoc;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.JobIntentService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ServerSocketIntentService extends JobIntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "org.afc.apoc.action.FOO";
    private static final String ACTION_BAZ = "org.afc.apoc.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "org.afc.apoc.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.afc.apoc.extra.PARAM2";

    private ExecutorService executorService;

    public ServerSocketIntentService() {
        super();
        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionFoo(Context context, String param1, String param2) {
        Intent intent = new Intent(context, ServerSocketIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }


    @Override
    protected void onHandleWork(Intent intent) {
        Log.i("service", "starting server");
        showNetwork();
        try {
            ServerSocket ss = new ServerSocket(12240, 10, InetAddress.getByName("127.0.0.1"));


            Socket socket = null;
            while ((socket = ss.accept()) != null) {
                Log.i("socket", "new socket accepted");
                final Socket finalSocket = socket;
                executorService.execute(() -> {
                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(finalSocket.getInputStream()));
                        String line;
                        StringBuilder allLine = new StringBuilder();
                        while ((line = reader.readLine()) != null) {
                            allLine.append(line);
                        }
                        Log.i("line", allLine.toString());
                    } catch (IOException e) {
                        Log.i("", "", e);
                    }
                });
            }

        } catch (Exception e) {
            Log.i("", "", e);
        }

    }

    private static void showNetwork() {
        try {
            for (final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces!= null && interfaces.hasMoreElements(); ) {
                final NetworkInterface cur = interfaces.nextElement();
                if (cur.isLoopback()) {
                    continue;
                }
                Log.i("interface ", cur.getName());

                for (final InterfaceAddress addr : cur.getInterfaceAddresses()) {
                    final InetAddress inet_addr = addr.getAddress();
                    if (!(inet_addr instanceof Inet4Address)) {
                        continue;
                    }
                    Log.i("  address: ", inet_addr.getHostAddress() + "/" + addr.getNetworkPrefixLength());
                    Log.i("  broadcast address: ", addr.getBroadcast().getHostAddress());
                }
            }
        } catch (SocketException e) {
            Log.i("network", "", e);
        }
    }
}
