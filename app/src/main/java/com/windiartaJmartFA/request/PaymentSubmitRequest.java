package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.ViewStoreActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * This will send a POST Request and receive true if submission success, else false if failed
 * @author Windiarta
 */

public class PaymentSubmitRequest extends StringRequest {
    private static String URL = String.format("http://10.0.2.2:6969/payment/%d/submit", ViewStoreActivity.selected.id);
    Map<String, String> params;

    public PaymentSubmitRequest (
            Response.Listener listener, Response.ErrorListener errorListener
    ){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("reciept", "reciept");
    }

    public Map<String, String> getParams (){
        return params;
    }
}
