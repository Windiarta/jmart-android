package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * This will send a POST Request and receive a new payment if success, and null if failed
 * @author Windiarta
 */
public class PaymentRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:6969/payment/create";
    private Map<String, String> params;

    public PaymentRequest (int buyerId, int productId, int productCount, String shipmentAddress,
                           byte shipmentPlan, Response.Listener listener, Response.ErrorListener errorListener)
    {
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("buyerId", String.valueOf(buyerId));
        params.put("productId", String.valueOf(productId));
        params.put("productCount", String.valueOf(productCount));
        params.put("shipmentAddress", shipmentAddress);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
    }
    public Map<String, String> getParams(){
        return params;
    }
}