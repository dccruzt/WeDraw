package upc.edu.pe.wedraw;

import android.app.Activity;
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

import upc.edu.pe.wedraw.helpers.ConnectionHelper;

public class ConnectActivity extends AppCompatActivity {

    ListView mTvListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);

        ConnectionHelper.sDesaplgListener.setConnectActivity(this);
        mTvListView = (ListView) findViewById(R.id.activity_connect_tv_list);

        listTvs();
    }

    private void listTvs(){
        DevicePicker picker = new DevicePicker(this);
        ListView tvPickerListView = picker.getListView();

        mTvListView.setAdapter(new TvAdapter(tvPickerListView.getAdapter(), this));
        mTvListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    ConnectionHelper.sConnectableDevice = (ConnectableDevice)parent.getItemAtPosition(position);
                    ConnectionHelper.sWebOSTVService = (WebOSTVService)ConnectionHelper.sConnectableDevice.getServiceByName(WebOSTVService.ID);
                    ConnectionHelper.sWebOSTVService.connect();
                    //Lanzar Web app WeDraw

                } catch (Exception ex) {
                    new AlertDialog.Builder(getApplicationContext())
                            .setTitle("WeDraw")
                            .setMessage("No se pudo connectar a la TV")
                            .create();
                }
            }
        });
    }


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
            LayoutInflater inflater = mActivity.getLayoutInflater();
            View view = inflater.inflate(R.layout.list_item_tv, null);

            ConnectableDevice device = (ConnectableDevice) mListAdapter.getItem(position);
            TextView tvNombreTv = (TextView) view.findViewById(R.id.item_tv);
            tvNombreTv.setText(device.getFriendlyName());
            return view;
        }
    }

}
