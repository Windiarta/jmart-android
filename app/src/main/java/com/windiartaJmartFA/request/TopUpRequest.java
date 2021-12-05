package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.LoginActivity;

import java.util.HashMap;
import java.util.Map;

public class TopUpRequest extends StringRequest {
    private static final String URI = "http://10.0.2.2:6969/account/%d/topUp";
    private static final String URL = String.format(URI, LoginActivity.getLoggedAccount().id);
    private final Map<String, String> params;

    public TopUpRequest(double balance, Response.Listener<String> listener,
                        Response.ErrorListener errorListener)
    {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("balance", String.valueOf(balance));
    }

    public Map<String, String> getParams(){
        return params;
    }
}
