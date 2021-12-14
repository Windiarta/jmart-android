package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.ViewStoreActivity;

/**
 * This will send a POST Request and receive a true boolean if acceptance success, else false if failed
 * @author Windiarta
 */
public class PaymentAcceptRequest extends StringRequest {
    private static String URL = String.format("http://10.0.2.2:6969/payment/%d/accept", ViewStoreActivity.selected.id);
    public PaymentAcceptRequest (
            Response.Listener listener, Response.ErrorListener errorListener
    ){
        super(Method.POST, URL, listener, errorListener);
    }
}
