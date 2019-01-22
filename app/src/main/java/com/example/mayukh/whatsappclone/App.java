package com.example.mayukh.whatsappclone;

import com.parse.Parse;
import android.app.Application;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HtXHB2pJeiEpaKTdahjbQuIo0LYPLRT3qiyB47Ad")
                // if defined
                .clientKey("qTbpoAvjrSCnwY6RSt52AEvAtRtuhZI8G8zopQMF")
                .server("https://parseapi.back4app.com/")
                .build()
        );
    }
}