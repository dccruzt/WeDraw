package upc.edu.pe.wedraw.helpers;

import android.content.Context;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.discovery.DiscoveryManager;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.sessions.WebAppSession;

import upc.edu.pe.wedraw.connection.DesaplgListener;

/**
 * Created by Andres Revolledo on 4/12/16.
 */
public class ConnectionHelper {
    public static DiscoveryManager sDiscoveryManager;
    public static ConnectableDevice sConnectableDevice;
    public static WebOSTVService sWebOSTVService;
    public static WebAppSession sWebAppSession;
    public static DesaplgListener sDesaplgListener;
    public static Context sContext;

    public static void closeApplication(boolean closeWebApp) {
        if( closeWebApp ) {
            sWebAppSession.close(new ResponseListener<Object>() {
                @Override
                public void onSuccess(Object object) {

                }

                @Override
                public void onError(ServiceCommandError error) {

                }
            });

            if (sWebAppSession != null) {
                sWebAppSession.disconnectFromWebApp();
                sWebAppSession = null;
            }
        }

        System.exit(1);
    }
}
