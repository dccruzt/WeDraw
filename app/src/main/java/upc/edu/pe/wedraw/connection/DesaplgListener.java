package upc.edu.pe.wedraw.connection;

import com.connectsdk.service.sessions.WebAppSession;
import com.connectsdk.service.sessions.WebAppSessionListener;

import upc.edu.pe.wedraw.SplashActivity;

/**
 * Created by Andres Revolledo on 4/12/16.
 */
public class DesaplgListener implements WebAppSessionListener{

    private SplashActivity mSplashActivity;

    public SplashActivity getSplashActivity() {
        return mSplashActivity;
    }

    public void setSplashActivity(SplashActivity splashActivity) {
        mSplashActivity = splashActivity;
    }

    @Override
    public void onReceiveMessage(WebAppSession webAppSession, Object message) {

    }

    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }
}
