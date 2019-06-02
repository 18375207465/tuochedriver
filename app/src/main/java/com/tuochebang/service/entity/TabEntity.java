package com.tuochebang.service.entity;


import com.framework.app.component.view.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public int selectedIcon;
    public String title;
    public int unSelectedIcon;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title) {
        this.title = title;
    }

    public String getTabTitle() {
        return this.title;
    }

    public int getTabSelectedIcon() {
        return this.selectedIcon;
    }

    public int getTabUnselectedIcon() {
        return this.unSelectedIcon;
    }
}
