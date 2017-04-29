package com.example.ishaandhamija.sunodilse;

import android.net.Uri;

/**
 * Created by ishaandhamija on 19/04/17.
 */

public class Genre {

    String name;
    Uri uri;

    public Genre(String name, Uri uri) {
        this.name = name;
        this.uri = uri;
    }

    public String getName() {
        return name;
    }

    public Uri getUri() {
        return uri;
    }

}
