package com.example.android.com.example.android.calllogs;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.android.effectivenavigation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by KH-Chen on 14/12/29.
 */
public class callLogFragment extends ListFragment{

    public callLogFragment(){}
    private ArrayList<String> conNames;
    private ArrayList<String> conNumbers;
    private ArrayList<String> conTime;
    private ArrayList<String> conDate;
    private ArrayList<String> conType;
    private MyAdapter mMyAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i("1229","oncreateView");
        conNames = new ArrayList<String>();
        conNumbers = new ArrayList<String>();
        conTime = new ArrayList<String>();
        conDate = new ArrayList<String>();
        conType = new ArrayList<String>();

        Cursor curLog = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());

        setCallLogs(curLog);
//        ArrayAdapter<String> adapter = new MyAdapter<String>(
//                inflater.getContext(), android.R.layout.simple_list_item_1, conNames);
//
        mMyAdapter = new MyAdapter(getActivity(), android.R.layout.simple_list_item_1,
                R.id.tvNameMain, conNames);
        setListAdapter(mMyAdapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    private void setCallLogs(Cursor curLog) {

        conNames.clear();
        conNumbers.clear();
        conTime.clear();
        conDate.clear();
        conType.clear();
        while (curLog.moveToNext()) {
            String callNumber = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.NUMBER));
            conNumbers.add(callNumber);

            String callName = curLog
                    .getString(curLog
                            .getColumnIndex(android.provider.CallLog.Calls.CACHED_NAME));
            if (callName == null) {
                conNames.add("Unknown");
            } else
                conNames.add(callName);

            String callDate = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DATE));
            SimpleDateFormat formatter = new SimpleDateFormat(
                    "dd-MMM-yyyy HH:mm");
            String dateString = formatter.format(new Date(Long
                    .parseLong(callDate)));
            conDate.add(dateString);

            String callType = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.TYPE));
            if (callType.equals("1")) {
                conType.add("Incoming");
            } else
                conType.add("Outgoing");

            String duration = curLog.getString(curLog
                    .getColumnIndex(android.provider.CallLog.Calls.DURATION));
            conTime.add(duration);

        }
    }
    private class MyAdapter extends ArrayAdapter<String> {

        public MyAdapter(Context context, int resource, int textViewResourceId,
                         ArrayList<String> conNames) {
            super(context, resource, textViewResourceId, conNames);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View row = setList(position, parent);
            return row;
        }

        private View setList(int position, ViewGroup parent) {
            LayoutInflater inf = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inf.inflate(R.layout.calllog_liststyle, parent, false);

            TextView tvName = (TextView) row.findViewById(R.id.tvNameMain);
            TextView tvNumber = (TextView) row.findViewById(R.id.tvNumberMain);
            TextView tvTime = (TextView) row.findViewById(R.id.tvTime);
            TextView tvDate = (TextView) row.findViewById(R.id.tvDate);
            TextView tvType = (TextView) row.findViewById(R.id.tvType);

            tvName.setText(conNames.get(position));
            tvNumber.setText(conNumbers.get(position));
            tvTime.setText("( " + conTime.get(position) + "sec )");
            tvDate.setText(conDate.get(position));
            tvType.setText("( " + conType.get(position) + " )");

            return row;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("1229","onresume");
        Cursor curLog = CallLogHelper.getAllCallLogs(getActivity().getContentResolver());
        setCallLogs(curLog);
        mMyAdapter.notifyDataSetChanged();
    }
}
