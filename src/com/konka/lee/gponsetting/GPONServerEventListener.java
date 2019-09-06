package com.konka.lee.gponsetting;

import java.io.IOException;

public interface GPONServerEventListener {
    void onPostFailure(IOException e);

    void onPostResponse(String response);

    void onGetFailure(IOException e);

    void onGetResponse(String response);

//    void onServerEvent(EasyPlayServerEvent event);
}
