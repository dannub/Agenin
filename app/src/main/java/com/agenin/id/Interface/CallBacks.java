package com.agenin.id.Interface;

public interface CallBacks {
    void callbackObserver(Object obj);
    interface playerCallBack {
        void onItemClickOnItem(Integer albumId);
        void onPlayingEnd();
    }
}