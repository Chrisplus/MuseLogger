package com.chrisplus.muselogger.adapters;

import com.choosemuse.libmuse.Muse;
import com.chrisplus.muselogger.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by chrisplus on 22/7/16.
 */
public class MuseListAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Muse> muses;

    public MuseListAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        if (muses == null) {
            return 0;
        }

        return muses.size();
    }

    @Override
    public Muse getItem(int position) {
        if (position < 0 || getCount() == 0 || position >= getCount()) {
            return null;
        } else {
            return muses.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        Muse item = getItem(position);
        if (item == null) {
            return -1L;
        } else {
            return item.getMacAddress().hashCode();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MuseViewHolder viewHolder;
        View view = convertView;

        if (view == null) {
            view = inflater.inflate(R.layout.view_muse_item, parent, false);
            viewHolder = new MuseViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (MuseViewHolder) view.getTag();
        }

        Muse item = getItem(position);
        if (item != null) {
            viewHolder.nameView.setText(item.getName());
            viewHolder.macView.setText(item.getMacAddress());
            switch (item.getConnectionState()) {

                case NEEDS_UPDATE:
                    viewHolder.statusView.setText(R.string.muse_connection_status_need_update);
                    break;
                case CONNECTING:
                    viewHolder.statusView.setText(R.string.muse_connection_status_connecting);
                    break;
                case CONNECTED:
                    viewHolder.statusView.setText(R.string.muse_connection_status_connected);
                    break;
                case DISCONNECTED:
                    viewHolder.statusView.setText(R.string.muse_connection_status_disconnected);
                    break;
                case UNKNOWN:
                default:
                    viewHolder.statusView.setText(R.string.muse_connection_status_unknown);

            }
        } else {
            viewHolder.nameView.setText(R.string.muse_name_default);
            viewHolder.macView.setText(R.string.muse_mac_default);
            viewHolder.statusView.setText(R.string.muse_connection_status_unknown);
        }

        return view;

    }

    public void setMuses(List<Muse> museList) {
        if (museList != null) {
            muses = museList;
            notifyDataSetChanged();
        }

    }

    public static class MuseViewHolder {
        @BindView(R.id.muse_name)
        public TextView nameView;

        @BindView(R.id.muse_mac)
        public TextView macView;

        @BindView(R.id.muse_status)
        public TextView statusView;

        public MuseViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
