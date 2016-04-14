package upc.edu.pe.wedraw.connection;

import com.connectsdk.service.sessions.WebAppSession;
import com.connectsdk.service.sessions.WebAppSessionListener;

import upc.edu.pe.wedraw.ConnectActivity;
import upc.edu.pe.wedraw.InputNameActivity;
import upc.edu.pe.wedraw.SplashActivity;
import upc.edu.pe.wedraw.StartGameActivity;

/**
 * Created by Andres Revolledo on 4/12/16.
 * Esta clase implementa WebAppSessionListener encargada de procesar los
 * mensajes recividos desde la aplicacion web.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 * @
 */
public class DesaplgListener implements WebAppSessionListener{

    private SplashActivity mSplashActivity;
    private ConnectActivity mConnectActivity;
    private InputNameActivity mInputNameActivity;
    private StartGameActivity mStartGameActivity;

    public SplashActivity getSplashActivity() {
        return mSplashActivity;
    }

    public void setSplashActivity(SplashActivity splashActivity) {
        mSplashActivity = splashActivity;
    }

    public ConnectActivity getConnectActivity() {
        return mConnectActivity;
    }

    public void setConnectActivity(ConnectActivity connectActivity) {
        mConnectActivity = connectActivity;
    }

    public InputNameActivity getInputNameActivity() {
        return mInputNameActivity;
    }

    public void setInputNameActivity(InputNameActivity inputNameActivity) {
        mInputNameActivity = inputNameActivity;
    }

    public StartGameActivity getStartGameActivity() {
        return mStartGameActivity;
    }

    public void setStartGameActivity(StartGameActivity startGameActivity) {
        mStartGameActivity = startGameActivity;
    }

    @Override
    public void onReceiveMessage(WebAppSession webAppSession, Object message) {

    }

    @Override
    public void onWebAppSessionDisconnect(WebAppSession webAppSession) {

    }


}
