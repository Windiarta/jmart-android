package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.windiartaJmartFA.model.Payment;
import com.windiartaJmartFA.request.PaymentRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This will make a new payment for buying a new product
 * @author Windiarta
 */

public class PaymentActivity extends AppCompatActivity {
    int counter = 1;
    Gson gson = new Gson();
    private static Payment onPayment;

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
                Intent intent = new Intent(PaymentActivity.this, ProductViewActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button decrement = findViewById(R.id.dec_count);
        Button increment = findViewById(R.id.inc_count);
        EditText productCount = findViewById(R.id.prod_count);
        EditText address = findViewById(R.id.address);
        TextView type = findViewById(R.id.shipment_type);
        Button confirm = findViewById(R.id.payment_create);
        Button cancel = findViewById(R.id.payment_cancel);

        type.setText(ProductViewActivity.getShipmentString(MainActivity.getProduct().shipmentPlans));

        increment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter++;
                productCount.setText(String.valueOf(counter));
            }
        });
        decrement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(counter > 1){
                    counter--;
                }
                productCount.setText(String.valueOf(counter));
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PaymentActivity.this, ProductViewActivity.class);
                startActivity(intent);
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counter = new Integer(productCount.getText().toString());
                if(counter < 1 || address.getText().toString().equals("")) {
                    Toast.makeText(PaymentActivity.this, "Invalid Input", Toast.LENGTH_LONG);
                } else {
                    Response.Listener listener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object != null) {
                                    onPayment = gson.fromJson(object.toString(), Payment.class);
                                    Toast.makeText(PaymentActivity.this, "Payment Success", Toast.LENGTH_LONG).show();
                                    Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(PaymentActivity.this, "Out Of Stock", Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                Toast.makeText(PaymentActivity.this, "Cannot Buy This Product", Toast.LENGTH_LONG).show();
                                e.printStackTrace();
                            }
                        }
                    };
                    PaymentRequest request = new PaymentRequest(
                            LoginActivity.getLoggedAccount().id,
                            MainActivity.getProduct().id,
                            counter,
                            address.getText().toString(),
                            MainActivity.getProduct().shipmentPlans,
                            listener,
                            null
                    );
                    RequestQueue queue = Volley.newRequestQueue(PaymentActivity.this);
                    queue.add(request);
                }
            }
        });

    }
}