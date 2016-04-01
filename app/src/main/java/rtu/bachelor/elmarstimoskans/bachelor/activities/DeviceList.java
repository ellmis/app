package rtu.bachelor.elmarstimoskans.bachelor.activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import rtu.bachelor.elmarstimoskans.bachelor.R;

/**
 * Created by elmars.timoskans on 3/22/2016.
 */
public class DeviceList extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    private ListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_list);

        ListView listView = (ListView) findViewById(R.id.list_view);

        mAdapter = new ListAdapter();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            mAdapter.addSeparatorItem(getResources().getString(R.string.paired_devices));
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                mAdapter.addItem(device.getName(), device.getAddress());
            }
        }
        mAdapter.addDivider();
        mAdapter.addSeparatorItem(getResources().getString(R.string.available_devices));

        doDiscovery();

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);

        listView.setDivider(null);
        listView.setAdapter(mAdapter);
    }

    // Create a BroadcastReceiver for ACTION_FOUND
    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            // When discovery finds a device
                        if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Get the BluetoothDevice object from the Intent
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // Add the name and address to an array adapter to show in a ListView
                mAdapter.addItem(device.getName(), device.getAddress());
            }
        }
    };

    private void doDiscovery(){
        if (mBluetoothAdapter.isDiscovering()) {
            mBluetoothAdapter.cancelDiscovery();
        }
        mBluetoothAdapter.startDiscovery();
    }

    protected void onDestroy() {
        super.onDestroy();
        if (mBluetoothAdapter != null) {
            mBluetoothAdapter.cancelDiscovery();
        }
        this.unregisterReceiver(mReceiver);
    }


    private class ListAdapter extends BaseAdapter{

        private static final int TYPE_ITEM = 0;
        private static final int TYPE_SEPARATOR = 1;
        private static final int TYPE_DIVIDER = 2;
        private static final int TYPE_MAX_COUNT = 3;

        private int itemType;
        private String title;
        private String subtitle;
//        private ArrayList<String> mTitleArray = new ArrayList<>();
//        private ArrayList<String> mSubtitleArray = new ArrayList<>();
        private LayoutInflater mInflater;
        private List<ListAdapter> listItems = new ArrayList<>();
        private ListAdapter listItem;



//        private TreeSet mSeparatorsSet = new TreeSet();

        public ListAdapter() {
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public ListAdapter(int itemType, String title, String subtitle){
            this.itemType = itemType;
            this.title = title;
            this.subtitle = subtitle;
        }

        public int getType() {
            return itemType;
        }

        public String getTitle(){
            return title;
        }

        public String getSubtitle(){
            return subtitle;
        }

        public void addItem(final String title, final String subtitle) {
//            mTitleArray.add(title);
//            mSubtitleArray.add(subtitle);
            listItem = new ListAdapter(TYPE_ITEM, title, subtitle);
            listItems.add(listItem);
            notifyDataSetChanged();
        }

        public void addSeparatorItem(final String title) {
//            mTitleArray.add(title);
//            mSubtitleArray.add(null);
            // save separator position
//            mSeparatorsSet.add(mTitleArray.size() - 1);
            listItem = new ListAdapter(TYPE_SEPARATOR, title, null);
            listItems.add(listItem);
            notifyDataSetChanged();
        }

        public void addDivider(){
//            mTitleArray.add(null);
//            mSubtitleArray.add(null);
            listItem = new ListAdapter(TYPE_DIVIDER, null, null);
            listItems.add(listItem);
            notifyDataSetChanged();
        }

        @Override
        public boolean isEnabled(int position) {
            int type = getItemViewType(position);
            // Make separator items non-selectable and non-clickable
            return type==0;
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            ViewHolder holder = null;
            int type = getItemViewType(position);
            System.out.println("getView " + position + " " + convertView + " type = " + type);
            if (convertView == null) {
                holder = new ViewHolder();
                switch (type) {
                    case TYPE_ITEM:
                        convertView = mInflater.inflate(R.layout.listview_item, null);
                        holder.title = (TextView)convertView.findViewById(R.id.itemTitle);
                        holder.subtitle = (TextView)convertView.findViewById(R.id.itemSubtitle);
                        break;
                    case TYPE_SEPARATOR:
                        convertView = mInflater.inflate(R.layout.separator_item, null);
                        holder.title = (TextView)convertView.findViewById(R.id.separatorItem);
                        isEnabled(position);
                        break;
                    case TYPE_DIVIDER:
                        convertView = mInflater.inflate(R.layout.divider_item, null);
                        holder.divider = (View)convertView.findViewById(R.id.divider);
                        break;
                }
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder)convertView.getTag();
            }

            if (holder.title != null ) {
//                holder.title.setText(mTitleArray.get(position));
                holder.title.setText(listItems.get(position).getTitle());
            }
            if (holder.subtitle != null ) {
//                holder.subtitle.setText(mSubtitleArray.get(position));
                holder.subtitle.setText(listItems.get(position).getSubtitle());
            }
            if (holder.divider != null ) {
//                holder.divider.setBackgroundColor(getResources().getColor(R.color.dividerColor));
            }
            return convertView;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

//        @Override
//        public String getItem(int position) {
//            return listItem[position].getTitle();
//        }

        @Override
        public int getItemViewType(int position) {
//            return mSeparatorsSet.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
            return listItems.get(position).getType();
        }

        @Override
        public int getViewTypeCount() {
            return TYPE_MAX_COUNT;
        }

    }

    public static class ViewHolder {
        public TextView title;
        public TextView subtitle;
        public View divider;
    }
}
