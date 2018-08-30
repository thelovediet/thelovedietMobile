package lovedient.com.thelovedietandroid.utils;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import lovedient.com.thelovedietandroid.R;

public  class MySpinnerAdapter extends ArrayAdapter<String> {

    public MySpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTextColor(SystemUtils.getActivity().getResources().getColor(R.color.black));
        view.setTypeface(SystemUtils.robotoFont());
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTextColor(SystemUtils.getActivity().getResources().getColor(R.color.black));
        view.setTypeface(SystemUtils.robotoFont());
        return view;
    }
}