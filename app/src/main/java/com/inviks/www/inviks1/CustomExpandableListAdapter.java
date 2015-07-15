package com.inviks.www.inviks1;

import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Rujoota on 09-07-2015.
 */
public class CustomExpandableListAdapter extends BaseExpandableListAdapter
{
    private static final int[] EMPTY_STATE_SET = {};
    private static final int[] GROUP_EXPANDED_STATE_SET = { android.R.attr.state_expanded };
    private static final int[][] GROUP_STATE_SETS = { EMPTY_STATE_SET, GROUP_EXPANDED_STATE_SET };
    private Context context;
    LinkedHashMap<String,List<String>> map;
    List<String> headers;
    public CustomExpandableListAdapter(Context context,LinkedHashMap<String,List<String>> hash,List<String> headers)
    {
        this.map=hash;
        this.context=context;
        this.headers=headers;
    }
    @Override
    public int getGroupCount()
    {
        return headers.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        if(map.get(headers.get(groupPosition))!=null)
            return map.get(headers.get(groupPosition)).size();
        else
        {

            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return headers.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return map.get(headers.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds()
    {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        String title=(String)getGroup(groupPosition);
        if(convertView==null)
        {
            LayoutInflater inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.expandable_parent_layout,parent,false);
        }
        TextView parentText=(TextView)convertView.findViewById(R.id.txtParent);
        parentText.setText(title);
        //Image view which you put in row_group_list.xml
        View ind = convertView.findViewById(R.id.imgParent);
        if (ind != null)
        {
            ImageView indicator = (ImageView) ind;
            if (getChildrenCount(groupPosition) == 0)
            {
                indicator.setVisibility(View.INVISIBLE);
            }
            else
            {
                indicator.setVisibility(View.VISIBLE);
                int stateSetIndex = (isExpanded ? 1 : 0);
                if(isExpanded)
                    indicator.setImageResource(R.drawable.up_collapse);
                else
                    indicator.setImageResource(R.drawable.down_expand);
                Drawable drawable = indicator.getDrawable();
                drawable.setState(GROUP_STATE_SETS[stateSetIndex]);
            }
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        try
        {
            if (getChild(groupPosition, childPosition) != null && getChildrenCount(groupPosition)>0)
            {
                String childStr = (String) getChild(groupPosition, childPosition);
                // means that view for that row is not created
                if (convertView == null)
                {
                    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = inflater.inflate(R.layout.expandable_child_layout, parent, false);
                }
                TextView childText = (TextView) convertView.findViewById(R.id.txtChild);
                childText.setText(childStr);
            }

        }
        catch(Exception ex)
        {
            Log.i("Inviks", ex.getMessage());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition)
    {
        return true;
    }
}
