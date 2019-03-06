package com.arman.queuetube.menu;

public class MenuItem {

    protected String name, url;
    protected boolean isGroup;

    public MenuItem(String name, boolean isGroup, String url) {
        this.name = name;
        this.isGroup = isGroup;
        this.url = url;
    }

    public MenuItem(String name, String url) {
        this(name, true, url);
    }

    public int childrenCount() {
        return 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

}
