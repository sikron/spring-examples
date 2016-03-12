package com.skronawi.spring.examples.jsp.communication;

public class ViewState {

    private boolean showOnlyOpen = true;

    public ViewState() {
    }

    public boolean isShowOnlyOpen() {
        return showOnlyOpen;
    }

    public void setShowOnlyOpen(boolean showOnlyOpen) {
        this.showOnlyOpen = showOnlyOpen;
    }
}
