package upc.edu.pe.wedraw;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.connectsdk.device.ConnectableDevice;
import com.connectsdk.device.DevicePicker;
import com.connectsdk.service.WebOSTVService;
import com.connectsdk.service.capability.listeners.ResponseListener;
import com.connectsdk.service.command.ServiceCommandError;
import com.connectsdk.service.sessions.WebAppSession;

import upc.edu.pe.wedraw.helpers.ConnectionHelper;
import upc.edu.pe.wedraw.helpers.JsonHelper;

/*
 * Clase de la actividad de conexion a las TVs WebOS maneja el listado de
 * un listView con las TVs encontradas y la creacion de la session del jugador
 * con la TV.
 *
 * @author Daniela Cruz
 * @author Victor Vasquez
 * @author Andres Revolledo
 */
public class ConnectActivity extends AppCompatActivity {

    static final String WEBAPP = "wedraw";
    ListView mTvListView;
    private TvAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ConnectionHelper.sDesaplgListener.setConnectActivity(this);
        mTvListView = (ListView) findViewById(R.id.activity_connect_tv_list);

        listTvs();
    }

    /*
     *  Este metodo obtiene la lista de TVs encontradas y llena el mTvListView con ellas
     *  E implementa la conexion con la TV selecionada
     */
    private void listTvs(){
        //Obtener lista de devices provista por el sdk
        DevicePicker picker = new DevicePicker(this);
        ListView tvPickerListView = picker.getListView();
        ListAdapter pickerAdapter = tvPickerListView.getAdapter();
        //Setear el adapter que mostrará la lista de TVs
        mAdapter = new TvAdapter(pickerAdapter, this);
        mTvListView.setAdapter(mAdapter);
        //Detectar los cambios en la lista de dispositivos y actualizar la lista
        pickerAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                mAdapter.notifyDataSetChanged();
            }
        });
        //Detectar cuando se selecciona un item de la lista
        mTvListView.setOnItemClickListener(new TVSelectedCallback());
    }

    /**
     * Clase que implementa se encargará de manejar la selección de uno de los items
     * de la lista de TVs
     */
    class TVSelectedCallback implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                ConnectionHelper.sConnectableDevice = (ConnectableDevice)parent.getItemAtPosition(position);
                ConnectionHelper.sWebOSTVService = (WebOSTVService)ConnectionHelper.sConnectableDevice.getServiceByName(WebOSTVService.ID);
                ConnectionHelper.sWebOSTVService.connect();
                //Lanzar Web app WeDraw
                ConnectionHelper.sWebOSTVService.joinWebApp(WEBAPP, new WebAppSession.LaunchListener() {
                    @Override
                    public void onSuccess(WebAppSession object) {
                        ConnectionHelper.sWebAppSession = object;
                        //Conecta Jugador a Tv
                        ConnectPlayerTv();
                    }

                    @Override
                    public void onError(ServiceCommandError error) {
                        ConnectionHelper.sWebOSTVService.launchWebApp(WEBAPP, new WebAppSession.LaunchListener() {
                            @Override
                            public void onSuccess(WebAppSession object) {
                                ConnectionHelper.sWebAppSession = object;
                                //Conecta Jugador a Tv
                                ConnectPlayerTv();
                            }

                            @Override
                            public void onError(ServiceCommandError error) {
                                new AlertDialog.Builder(getApplicationContext())
                                        .setTitle("WeDraw")
                                        .setMessage("No se pudo connectar a la TV")
                                        .create();
                            }
                        });

                    }
                });
                Intent i = new Intent(ConnectActivity.this, InputNameActivity.class);
                startActivity(i);

            } catch (Exception ex) {
                new AlertDialog.Builder(ConnectActivity.this)
                        .setTitle("WeDraw")
                        .setMessage("No se pudo connectar a la TV")
                        .show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        ConnectionHelper.sDesaplgListener.setConnectActivity(null);
        System.gc();
        super.onDestroy();
    }

    /*
     * Este metodo se encarga de conectar el jugador con una TV inicializando el listener y enviando
     * un mensaje de conexion.
     */
    private void ConnectPlayerTv(){
        ConnectionHelper.sWebAppSession.setWebAppSessionListener(ConnectionHelper.sDesaplgListener);
        ConnectionHelper.sWebAppSession.sendMessage(JsonHelper.ConnectTv(), new ResponseListener<Object>() {
            @Override
            public void onSuccess(Object object) {

            }

            @Override
            public void onError(ServiceCommandError error) {

            }
        });
    }


    /*
     * Clase derivada de BaseAdapter encargada de manejar el contenido de los elementos de
     * mTvListView
     */
    private class TvAdapter extends BaseAdapter {
        ListAdapter mListAdapter;
        Activity mActivity;

        public TvAdapter(ListAdapter listAdapter, Activity activity) {
            mListAdapter = listAdapter;
            mActivity = activity;
        }

        @Override
        public int getCount() {
            return mListAdapter.getCount();
        }

        @Override
        public Object getItem(int position) {
            return mListAdapter.getItem(position);
        }

        @Override
        public long getItemId(int position) {
            return mListAdapter.getItemId(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null)
                convertView = LayoutInflater.from(ConnectActivity.this).inflate(R.layout.list_item_tv, parent, false);
            ConnectableDevice device = (ConnectableDevice) mListAdapter.getItem(position);
            TextView tvNombreTv = (TextView) convertView.findViewById(R.id.item_tv);
            tvNombreTv.setText(device.getFriendlyName());
            return convertView;
        }
    }

}
