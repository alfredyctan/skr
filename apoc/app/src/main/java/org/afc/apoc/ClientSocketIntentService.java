package org.afc.apoc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class ClientSocketIntentService extends JobIntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "org.afc.apoc.action.FOO";
    private static final String ACTION_BAZ = "org.afc.apoc.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "org.afc.apoc.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "org.afc.apoc.extra.PARAM2";

    private ExecutorService executorService;

    public ClientSocketIntentService() {
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
        Intent intent = new Intent(context, ClientSocketIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.i("service", "starting client");
        try {
            Socket socket = new Socket();
            socket.connect(new InetSocketAddress("127.0.0.1", 12240));
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            writer.println(intent.getStringExtra("msg"));
            writer.flush();
            writer.close();

        } catch (Exception e) {
            Log.i("", "", e);
        }

    }
}
