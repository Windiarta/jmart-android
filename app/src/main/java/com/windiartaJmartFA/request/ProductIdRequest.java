package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

/**
 * This will send a GET Request and receive a product if available, and null if unavailable
 * @author Windiarta
 */

public class ProductIdRequest extends StringRequest {
    private static String URL = "http://10.0.2.2:6969/product/%d";

    public ProductIdRequest (int id, Response.Listener listener, Response.ErrorListener errorListener) {
        super(Method.GET, String.format(URL, id), listener, errorListener);
    }

}
