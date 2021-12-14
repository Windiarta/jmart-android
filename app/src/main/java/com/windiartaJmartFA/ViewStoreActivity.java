package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.windiartaJmartFA.model.Invoice;
import com.windiartaJmartFA.model.Payment;
import com.windiartaJmartFA.model.Product;
import com.windiartaJmartFA.request.PaymentAcceptRequest;
import com.windiartaJmartFA.request.PaymentCancelRequest;
import com.windiartaJmartFA.request.PaymentListRequest;
import com.windiartaJmartFA.request.PaymentSubmitRequest;
import com.windiartaJmartFA.request.ProductIdRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This will show the payment from the seller view
 * @author Windiarta
 */

public class ViewStoreActivity extends AppCompatActivity {
    public static int pageNumber = 0;
    public static Payment selected;
    private static List<Payment> array = new ArrayList<>();
    private static List<String> arrayString = new ArrayList<>();
    private Gson gson = new Gson();
    ArrayAdapter<String> adapter;
    static final int pageSize = 10;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        MenuItem addbox = menu.findItem(R.id.addbox);
        MenuItem account = menu.findItem(R.id.account);

        search.setVisible(false);
        addbox.setVisible(false);
        account.setVisible(false);

        MenuItem back = menu.findItem(R.id.back);
        back.setVisible(true);
        back.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(ViewStoreActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_store);

        ListView list = (ListView) findViewById(R.id.payment_list);
        CardView show = findViewById(R.id.submit_confirmation);
        TextView close = findViewById(R.id.submit_close_button);
        Button submit = findViewById(R.id.payment_submit);
        Button cancel = findViewById(R.id.payment_cancel_submit);
        Button next = findViewById(R.id.payment_next_button);
        Button prev = findViewById(R.id.payment_prev_button);
        TextView text = findViewById(R.id.payment_text1);
        EditText page = findViewById(R.id.payment_page);
        Button go = findViewById(R.id.payment_go_button);
        TextView text2 = findViewById(R.id.payment_text2);
        Button okay = findViewById(R.id.progress_submit);
        TextView close2 = findViewById(R.id.progress_cancel_button);
        CardView show2 = findViewById(R.id.progress_confirmation);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayString);
        list.setAdapter(adapter);
        request(list);
        list.invalidateViews();
        list.refreshDrawableState();

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show.setVisibility(View.INVISIBLE);
            }
        });

        close2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                show2.setVisibility(View.INVISIBLE);
            }
        });

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber = new Integer(page.getText().toString());
                request(list);
            }
        });

        next.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                pageNumber+=1;
                String num = String.valueOf(pageNumber);
                page.setText(num);
                request(list);
            }
        });

        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageNumber > 0){
                    pageNumber -= 1;
                    String num = String.valueOf(pageNumber);
                    page.setText(num);
                    request(list);
                } else{
                    Toast.makeText(ViewStoreActivity.this, "You are in the first page", Toast.LENGTH_LONG).show();
                }
            }
        });

        okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //accept here
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Boolean object = new Boolean(response);
                            if(object){
                                Toast.makeText(ViewStoreActivity.this, "Action Success", Toast.LENGTH_LONG);
                                request(list);
                                show2.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(ViewStoreActivity.this, "Action Failed", Toast.LENGTH_LONG);
                            }
                        } catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                };
                PaymentAcceptRequest request = new PaymentAcceptRequest(listener, null);
                RequestQueue queue = Volley.newRequestQueue(ViewStoreActivity.this);
                queue.add(request);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //submit here
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Boolean object = new Boolean(response);
                            System.out.println("SUBMIT" + object);
                            if(object){
                                Toast.makeText(ViewStoreActivity.this, "Action Success", Toast.LENGTH_LONG);
                                request(list);
                                show.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(ViewStoreActivity.this, "Action Failed", Toast.LENGTH_LONG);
                            }
                        } catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                };
                PaymentSubmitRequest request = new PaymentSubmitRequest(listener, null);
                RequestQueue queue = Volley.newRequestQueue(ViewStoreActivity.this);
                queue.add(request);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cancel here
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Boolean object = new Boolean(response);
                            System.out.println("CANCEL" + object);
                            if(object){
                                Toast.makeText(ViewStoreActivity.this, "Action Success", Toast.LENGTH_LONG);
                                request(list);
                                show.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(ViewStoreActivity.this, "Action Failed", Toast.LENGTH_LONG);
                            }
                        } catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                };
                PaymentCancelRequest request = new PaymentCancelRequest(selected.id, listener, null);
                RequestQueue queue = Volley.newRequestQueue(ViewStoreActivity.this);
                queue.add(request);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = array.get(position);
                if(selected.history.get(selected.history.size()-1).status == Invoice.Status.ON_PROGRESS){
                    show.setVisibility(View.VISIBLE);
                    show2.setVisibility(View.INVISIBLE);
                    text.setText(String.format("Product Name : %s\nProduct Count : %d\nShipment : %s", arrayString.get(position), selected.productCount,
                            getShipmentString(selected.shipment.plan)));
                } else if (selected.history.get(selected.history.size()-1).status == Invoice.Status.WAITING_CONFIRMATION){
                    text2.setText(String.format("Product Name : %s\nProduct Count : %d\nShipment : %s", arrayString.get(position), selected.productCount,
                            getShipmentString(selected.shipment.plan)));
                    show.setVisibility(View.INVISIBLE);
                    show2.setVisibility(View.VISIBLE);
                } else if (selected.history.get(selected.history.size()-1).status == Invoice.Status.CANCELLED){
                    Toast.makeText(ViewStoreActivity.this, "CANCELLED", Toast.LENGTH_LONG).show();
                } else if (selected.history.get(selected.history.size()-1).status == Invoice.Status.ON_DELIVERY){
                    Toast.makeText(ViewStoreActivity.this, "ON DELIVERY", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void request(ListView list){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray objects = new JSONArray(response);
                    array.clear();
                    arrayString.clear();
                    for(int i = 0; i < objects.length(); i++){
                        JSONObject object = objects.getJSONObject(i);
                        array.add(gson.fromJson(object.toString(), Payment.class));
                        arrayString.add("");
                    }
                    for(Payment payment : array) {
                        requestProductById(list, payment, new Integer(array.indexOf(payment)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        PaymentListRequest paymentListRequest = new PaymentListRequest(pageNumber, pageSize, LoginActivity.getLoggedAccount().id, listener, null);
        RequestQueue queue = Volley.newRequestQueue(ViewStoreActivity.this);
        queue.add(paymentListRequest);
    }

    private void requestProductById (ListView list, Payment payment, int index){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject myObject = new JSONObject(response);
                    if (myObject != null) {
                        Product selectedProduct = gson.fromJson(myObject.toString(), Product.class);
                        arrayString.set(index ,selectedProduct.name);
                    } else {
                        System.out.println("NULL");
                    }
                    list.invalidateViews();
                    list.refreshDrawableState();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProductIdRequest productIdRequest = new ProductIdRequest(payment.productId, listener, null);
        RequestQueue queue = Volley.newRequestQueue(ViewStoreActivity.this);
        queue.add(productIdRequest);
    }

    public static String getShipmentString(byte bytes){
        switch (bytes){
            case 0:
                return "INSTANT";
            case 1:
                return "SAME DAY";
            case 2:
                return "NEXT DAY";
            case 3:
                return "REGULER";
            case 4:
                return "KARGO";
            default:
                return "No Shipment";
        }
    }
}