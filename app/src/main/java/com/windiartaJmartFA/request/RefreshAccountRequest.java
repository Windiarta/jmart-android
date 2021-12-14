package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class RefreshAccountRequest extends StringRequest {
    private static String URL = "http://10.0.2.2:6969/account/%d";

    public RefreshAccountRequest (int id, Response.Listener listener, Response.ErrorListener errorListener){
        super (Method.GET, String.format(URL, id), listener, errorListener);
    }
}
