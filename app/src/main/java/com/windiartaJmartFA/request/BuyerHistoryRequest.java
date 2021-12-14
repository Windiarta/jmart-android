package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * This will send a GET Request and will receive payments of the buyer
 * @author Windiarta
 */

public class BuyerHistoryRequest extends StringRequest {
    private static String URI = "http://10.0.2.2:6969/payment/getAccountFiltered?page=%d&pageSize=%d&accountId=%d";

    public BuyerHistoryRequest (
            int page, int pageSize, int accountId, Response.Listener listener, Response.ErrorListener errorListener
    ) {
        super(Method.GET, String.format(URI, page, pageSize, accountId), listener, errorListener);
    }
}
