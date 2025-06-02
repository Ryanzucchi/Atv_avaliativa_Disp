package br.com.unemat.ryan.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;

public class CrecheAdapter extends BaseAdapter {
    private Context context;
    private List<String> crecheList;

    public CrecheAdapter(Context context, List<String> crecheList) {
        this.context = context;
        this.crecheList = crecheList;
    }

    @Override
    public int getCount() {
        return crecheList.size();
    }

    @Override
    public Object getItem(int position) {
        return crecheList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        CheckBox checkBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_creche, parent, false);
            holder = new ViewHolder();
            holder.checkBox = convertView.findViewById(R.id.checkbox_creche);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String creche = crecheList.get(position);
        holder.checkBox.setText(creche);

        return convertView;
    }
}
