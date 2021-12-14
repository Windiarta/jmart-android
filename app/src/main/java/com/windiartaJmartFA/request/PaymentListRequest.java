package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * This will send a GET Request and receive a list of payment
 * @author Windiarta
 */
public class PaymentListRequest extends StringRequest {
    private static String URL = "http://10.0.2.2:6969/payment/getFiltered?page=%d&pageSize=%d&storeId=%d";

    public PaymentListRequest (int page, int pageSize, int storeId, Response.Listener listener, Response.ErrorListener errorListener){
        super(Method.GET, String.format(URL, page, pageSize, storeId), listener, errorListener);
    }
}