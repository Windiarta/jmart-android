package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.windiartaJmartFA.model.ProductCategory;
import com.windiartaJmartFA.request.NewProductRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This will make a new product to the list
 * @author Windiarta
 */

public class CreateProductActivity extends AppCompatActivity {
    boolean conditionUsed = false;
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
                Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);

        EditText name = findViewById(R.id.product_name);
        EditText weight = findViewById(R.id.product_weight);
        EditText price = findViewById(R.id.product_price);
        EditText discount = findViewById(R.id.product_discount);
        Spinner category = findViewById(R.id.category_spinner);
        Spinner shipment = findViewById(R.id.shipment_plan_spinner);
        RadioButton newRadio = findViewById(R.id.condition_new_radio);
        RadioButton usedRadio = findViewById(R.id.condition_used_radio);
        Button create = findViewById(R.id.createproduct);
        Button cancel = findViewById(R.id.cancel_product);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(CreateProductActivity.this, "Product Added", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(CreateProductActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                };
                if(newRadio.isChecked()){
                    conditionUsed = false;
                } else {
                    conditionUsed = true;
                }
                NewProductRequest request = new NewProductRequest(
                        LoginActivity.getLoggedAccount().id,
                        name.getText().toString(),
                        weight.getText().toString(),
                        conditionUsed,
                        price.getText().toString(),
                        discount.getText().toString(),
                        category.getSelectedItem().toString(),
                        getShipmentPlan(shipment),
                        listener, null
                );
                RequestQueue queue = Volley.newRequestQueue(CreateProductActivity.this);
                queue.add(request);

            }
        });
    }

    byte getShipmentPlan(Spinner spinner){
        byte value;
        switch (spinner.getSelectedItem().toString()){
            case "INSTANT":
                value = (1<<0);
                break;
            case "SAME DAY":
                value = (1<<1);
                break;
            case "NEXT DAY":
                value = (1<<2);
                break;
            case "REGULER":
                value = (1<<3);
                break;
            case "KARGO":
                value = (1<<4);
                break;
            default:
                value = (1<<5);
                break;
        }
        return value;
    }
}