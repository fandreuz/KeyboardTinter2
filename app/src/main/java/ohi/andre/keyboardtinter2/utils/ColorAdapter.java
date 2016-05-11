package ohi.andre.keyboardtinter2.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ColorAdapter extends BaseAdapter {

    List<Integer> colors;
    Context mContext;

    public ColorAdapter(Context context, List<Integer> colors) {
        mContext = context;
        this.colors = new ArrayList<>();
        this.colors.addAll(colors);
    }

    @Override
    public int getCount() {
        return colors.size();
    }

    @Override
    public Object getItem(int position) {
        return colors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void add(int color) {
        colors.add(color);
        notifyDataSetChanged();
    }

    public void add(String color) {
        try {
            this.add(Color.parseColor(color));
        } catch (IllegalArgumentException exc) {
            Toast.makeText(mContext, "Invalid color", Toast.LENGTH_SHORT).show();
        }
    }

    public void addAll(List<Integer> c) {
        colors.addAll(c);
        notifyDataSetChanged();
    }

    public int remove(int position) {
        int color = colors.remove(position);
        notifyDataSetChanged();
        return color;
    }

    public void removeAll() {
        colors.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getAll() {
        return colors;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) mContext).getLayoutInflater().inflate(android.R.layout.simple_list_item_activated_1, null);
        }
        ((TextView) convertView).setText("color " + position);
        convertView.setBackgroundColor(colors.get(position));

        return convertView;
    }

}
