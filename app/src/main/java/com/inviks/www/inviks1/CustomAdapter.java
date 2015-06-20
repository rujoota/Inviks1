package com.inviks.www.inviks1;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
class CustomAdapter extends ArrayAdapter<String>{
    int[] iconImageArray;
    public CustomAdapter(Context context, String[] array, int[] iconImgArr) {
        super(context,R.layout.homepagelistview, array);
        this.iconImageArray=iconImgArr;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // inflate=get ready/prepare
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView=inflater.inflate(R.layout.homepagelistview, parent, false);

        String arrItem=getItem(position);

        TextView textView=(TextView)customView.findViewById(R.id.txtHomeListView);
        ImageView imgView=(ImageView)customView.findViewById(R.id.imgHomeListView);
        textView.setText(arrItem);
        imgView.setImageResource(iconImageArray[position]);
        return customView;
    }
}
