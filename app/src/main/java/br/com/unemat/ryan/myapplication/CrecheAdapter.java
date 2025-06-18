package br.com.unemat.ryan.myapplication;

import android.content.Context;
import android.content.SharedPreferences; // Import SharedPreferences
import android.util.Log; // For logging, helpful for debugging
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.List;

public class CrecheAdapter extends BaseAdapter {
    private static final String TAG = "CrecheAdapter"; // Tag for logging
    private static final String PREFS_NAME = "CrecheSelectionPrefs"; // Name for your SharedPreferences file

    private Context context;
    private List<String> crecheList;
    private SharedPreferences sharedPreferences; // Declare SharedPreferences

    public CrecheAdapter(Context context, List<String> crecheList) {
        this.context = context;
        this.crecheList = crecheList;
        // Initialize SharedPreferences in the constructor
        this.sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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

        final String crecheName = crecheList.get(position); // Use final for inner class access
        holder.checkBox.setText(crecheName);

        // 1. Load the saved state for this specific checkbox
        // The key for SharedPreferences should be unique for each creche.
        // We'll use the crecheName itself as the key.
        boolean isChecked = sharedPreferences.getBoolean(crecheName, false); // 'false' is the default if not found
        holder.checkBox.setChecked(isChecked);
        Log.d(TAG, "Loading state for '" + crecheName + "': " + isChecked);


        // 2. Set a listener to save the state when the checkbox is clicked
        holder.checkBox.setOnCheckedChangeListener((buttonView, isUserChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(crecheName, isUserChecked); // Save the new state
            editor.apply(); // Apply changes asynchronously (good practice)
            Log.d(TAG, "Saving state for '" + crecheName + "': " + isUserChecked);
        });

        return convertView;
    }
}