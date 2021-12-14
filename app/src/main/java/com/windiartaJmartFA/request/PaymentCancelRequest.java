package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.ViewStoreActivity;

/**
 * This will send a POST Request and receive a true if cancellation success, else false if failed
 * @author Windiarta
 */
public class PaymentCancelRequest extends StringRequest {

    public PaymentCancelRequest (
            int id, Response.Listener listener, Response.ErrorListener errorListener
    ){
        super(Method.POST, String.format("http://10.0.2.2:6969/payment/%d/cancel", id), listener, errorListener);
    }
}
