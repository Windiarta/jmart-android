package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This will send a POST Request of email and password and return an account with the same email and same encrypted password
 * @author Windiarta
 */
public class LoginRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:6969/account/login";
    private final Map<String, String> params;

    public LoginRequest(String email, String password, Response.Listener<String> listener,
                        Response.ErrorListener errorListener)
    {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("email", email);
        params.put("password", password);
    }

    public Map<String, String> getParams(){
        return params;
    }
}
