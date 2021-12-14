package com.windiartaJmartFA.request;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.windiartaJmartFA.model.ProductCategory;

/**
 * This will send a GET Request and receive a list of products that has filtered
 * @author Windiarta
 */

public class ProductRequest extends StringRequest{
    private final static String URL_FORMAT = "http://10.0.2.2:6969/product/getFiltered?page=%d&pageSize=%d&accountId=%d&" +
            "search=%s&minPrice=%s&maxPrice=%s&category=%s";

    public ProductRequest(int page, int pageSize, int accountId, String search,
                          int minPrice, int maxPrice, String category,
                          Response.Listener<String> listener, Response.ErrorListener errorListener){
        super(Method.GET, getURL(page, pageSize, accountId, search, minPrice, maxPrice, category), listener, errorListener);
    }

    public static String getURL(
            int page, int pageSize, int accountId, String search,
            int minPrice, int maxPrice, String category)
    {
        System.out.println(search);
        if(category == "null"){
            category = "";
        }
        return String.format(URL_FORMAT, page, pageSize, accountId, search, minPrice, maxPrice, category);
    }

}
