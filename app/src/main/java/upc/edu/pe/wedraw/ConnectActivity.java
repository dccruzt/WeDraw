package upc.edu.pe.wedraw;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.connectsdk.device.ConnectableDevice;

public class ConnectActivity extends AppCompatActivity {

    ListView mTvListView;
    TvAdapter mTvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
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
