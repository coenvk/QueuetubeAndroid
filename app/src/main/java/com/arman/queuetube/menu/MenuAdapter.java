package com.arman.queuetube.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.arman.queuetube.R;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<MenuItem> menuItems;

    public MenuAdapter(Context context, List<MenuItem> menuItems) {
        this.context = context;
        this.menuItems = menuItems;
    }

    public MenuAdapter(Context context) {
        this(context, new ArrayList<MenuItem>());
    }

    public boolean add(MenuItem menuItem) {
        return this.menuItems.add(menuItem);
    }

    public boolean addAll(Collection<MenuItem> menuItems) {
        return this.menuItems.addAll(menuItems);
    }

    public boolean remove(MenuItem menuItem) {
        return this.menuItems.remove(menuItem);
    }

    public MenuItem remove(int i) {
        return this.menuItems.remove(i);
    }

    @Override
    public int getGroupCount() {
        return this.menuItems.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return this.menuItems.get(i).childrenCount();
    }

    @Override
    public MenuItem getGroup(int i) {
        return this.menuItems.get(i);
    }

    @Override
    public MenuItem getChild(int i, int i1) {
        MenuItem menuItem = this.menuItems.get(i);
        if (menuItem instanceof DropdownMenuItem) {
            return ((DropdownMenuItem) menuItem).get(i1);
        }
        return null;
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        String menuName = this.getGroup(i).getName();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.menu_list_header, null);
        }
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }

}
