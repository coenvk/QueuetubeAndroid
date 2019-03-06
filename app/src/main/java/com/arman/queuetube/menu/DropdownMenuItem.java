package com.arman.queuetube.menu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class DropdownMenuItem extends MenuItem {

    private List<MenuItem> menuItems;

    public DropdownMenuItem(String name, boolean isGroup, String url) {
        super(name, isGroup, url);
        this.menuItems = new ArrayList<>();
    }

    public DropdownMenuItem(String name, String url) {
        super(name, url);
        this.menuItems = new ArrayList<>();
    }

    public boolean add(MenuItem menuItem) {
        if (menuItem instanceof DropdownMenuItem) {
            return false;
        }
        return this.menuItems.add(menuItem);
    }

    public boolean addAll(Collection<MenuItem> menuItems) {
        for (MenuItem menuItem : menuItems) {
            if (!this.add(menuItem)) {
                return false;
            }
        }
        return true;
    }

    public boolean remove(MenuItem menuItem) {
        return this.menuItems.remove(menuItem);
    }

    public MenuItem remove(int i) {
        return this.menuItems.remove(i);
    }

    public MenuItem get(int i) {
        return this.menuItems.get(i);
    }

    @Override
    public int childrenCount() {
        return this.menuItems.size();
    }

}
