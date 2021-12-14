package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.model.ProductCategory;

import java.util.HashMap;
import java.util.Map;

/**
 * This will send a POST Request and receive a new product
 * @author Windiarta
 */
public class NewProductRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:6969/product/create";
    private final Map<String, String> params;

    public NewProductRequest(int accountId, String name, String weight, boolean conditionUsed,
                             String price, String discount, String category, byte shipmentPlan,
                             Response.Listener listener, Response.ErrorListener errorListener){
        super(Method.POST, URL, listener, errorListener);
        params = new HashMap<>();
        params.put("accountId", String.valueOf(accountId));
        params.put("name", name);
        params.put("weight", weight);
        params.put("conditionUsed", String.valueOf(conditionUsed));
        params.put("price", price);
        params.put("discount", discount);
        params.put("category", category);
        params.put("shipmentPlan", String.valueOf(shipmentPlan));
    }
    public Map<String, String> getParams(){
        return params;
    }
}
