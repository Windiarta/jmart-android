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
import com.windiartaJmartFA.request.BuyerHistoryRequest;
import com.windiartaJmartFA.request.PaymentCancelRequest;
import com.windiartaJmartFA.request.PaymentListRequest;
import com.windiartaJmartFA.request.ProductIdRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * This will show the Buyer Payment History
 * Contains : List of payment that processed by the account
 * Buyer can cancel the payment and get a refund
 * @author Windiarta
 */
public class BuyerHistoryActivity extends AppCompatActivity {
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
                Intent intent = new Intent(BuyerHistoryActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyer_history);

        ListView list = (ListView) findViewById(R.id.history_list);
        CardView show = findViewById(R.id.history_details);
        TextView close = findViewById(R.id.history_close_button);
        Button cancel = findViewById(R.id.history_cancel_button);
        Button next = findViewById(R.id.history_next_button);
        Button prev = findViewById(R.id.history_prev_button);
        TextView text = findViewById(R.id.history_text1);
        EditText page = findViewById(R.id.history_page);
        Button go = findViewById(R.id.history_go_button);

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
                    Toast.makeText(BuyerHistoryActivity.this, "You are in the first page", Toast.LENGTH_LONG).show();
                }
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
                                Toast.makeText(BuyerHistoryActivity.this, "Action Success", Toast.LENGTH_LONG);
                                request(list);
                                show.setVisibility(View.INVISIBLE);
                            } else {
                                Toast.makeText(BuyerHistoryActivity.this, "Action Failed", Toast.LENGTH_LONG);
                            }
                        } catch (Throwable e){
                            e.printStackTrace();
                        }
                    }
                };
                PaymentCancelRequest request = new PaymentCancelRequest(selected.id, listener, null);
                RequestQueue queue = Volley.newRequestQueue(BuyerHistoryActivity.this);
                queue.add(request);
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = array.get(position);

                show.setVisibility(View.VISIBLE);
                text.setText(String.format("Product Name : %s\nProduct Count : %d\nShipment : %s\nStatus : %s", arrayString.get(position), selected.productCount,
                        getShipmentString(selected.shipment.plan), selected.history.get(selected.history.size() - 1).status.toString()));

                if(selected.history.get(selected.history.size()-1).status == Invoice.Status.WAITING_CONFIRMATION ||
                        selected.history.get(selected.history.size()-1).status == Invoice.Status.ON_PROGRESS
                ){
                    cancel.setVisibility(View.VISIBLE);
                } else {
                    cancel.setVisibility(View.INVISIBLE);
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
                    for(Payment payment : array){
                        requestProductById(list, payment, new Integer(array.indexOf(payment)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        BuyerHistoryRequest buyerHistoryRequest = new BuyerHistoryRequest(pageNumber, pageSize, LoginActivity.getLoggedAccount().id, listener, null);
        RequestQueue queue = Volley.newRequestQueue(BuyerHistoryActivity.this);
        queue.add(buyerHistoryRequest);
    }

    private void requestProductById (ListView list, Payment payment, int index){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject myObject = new JSONObject(response);
                    if (myObject != null) {
                        Product selectedProduct = gson.fromJson(myObject.toString(), Product.class);
                        arrayString.set(index, selectedProduct.name);
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
        RequestQueue queue = Volley.newRequestQueue(BuyerHistoryActivity.this);
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