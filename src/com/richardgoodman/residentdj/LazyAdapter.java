package com.richardgoodman.residentdj;

//Import statements
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import info.androidhive.tabsswipe.R;

public class LazyAdapter extends BaseAdapter {
    
	//Variable Initialisation
    private Activity activity;
    private List<PlayItem> data;
    private static LayoutInflater inflater=null;
    public ImageLoader imageLoader;
    List<PlayItem> results;
    PlayItem item;
    
    public LazyAdapter(Activity a, List<PlayItem> items) {
        activity = a;
        data = items;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    public int getCount() {
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }
    
    public View getView(int position, View convertView, ViewGroup parent) {
        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.item, null);

        TextView text = (TextView)vi.findViewById(R.id.listViewText);
        ImageView image=(ImageView)vi.findViewById(R.id.listViewImage);
        
        item = data.get(position);
        
        text.setText(item.getTitle() + "\n" + item.getID());
        imageLoader.DisplayImage(item.getThumb(), image);
        
        return vi;
    }
}