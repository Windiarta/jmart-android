package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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
import com.windiartaJmartFA.model.Account;
import com.windiartaJmartFA.model.Serializable;
import com.windiartaJmartFA.model.Store;
import com.windiartaJmartFA.request.RefreshAccountRequest;
import com.windiartaJmartFA.request.StoreRequest;
import com.windiartaJmartFA.request.TopUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * This will show some data like :
 * 1. About account : name, email, balance, top up feature
 * 2. Store : name, address, phone number
 * 3. Store Activity : details in {@link ViewStoreActivity}
 * 4. Payment History : details in {@link BuyerHistoryActivity}
 */
public class AboutMeActivity extends AppCompatActivity {
    Gson gson = new Gson();
    private static Store myStore;
    private double topUpValue = 0;
    private static Account loggedAccount = LoginActivity.getLoggedAccount();

    /**
     *  This will refresh the logged account
     */
    public void refreshAccount () {
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null){
                        loggedAccount = gson.fromJson(object.toString(), Account.class);
                    } else {
                        Toast.makeText(AboutMeActivity.this, "Something Went Wrong", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        };

        RefreshAccountRequest request = new RefreshAccountRequest(
                LoginActivity.getLoggedAccount().id, listener, null
        );
        RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
        queue.add(request);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        MenuItem addbox = menu.findItem(R.id.addbox);
        MenuItem account = menu.findItem(R.id.account);
        MenuItem history = menu.findItem(R.id.history);
        MenuItem logout = menu.findItem(R.id.logout);

        search.setVisible(false);
        addbox.setVisible(false);
        account.setVisible(false);

        history.setVisible(true);
        history.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(AboutMeActivity.this, BuyerHistoryActivity.class);
                startActivity(intent);
                return true;
            }
        });

        logout.setVisible(true);
        logout.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(AboutMeActivity.this, LoginActivity.class);
                startActivity(intent);
                return true;
            }
        });

        MenuItem back = menu.findItem(R.id.back);
        back.setVisible(true);
        back.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(AboutMeActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);

        TextView name = findViewById(R.id.account_name);
        TextView email = findViewById(R.id.account_email);
        TextView balance = findViewById(R.id.account_balance);
        EditText topUp = findViewById(R.id.account_top_up);
        Button topUpButton = findViewById(R.id.account_top_up_button);

        Button register = findViewById(R.id.register_store_button);

        CardView registerStoreCard = findViewById(R.id.register_store_card);
        EditText storeNameInput = findViewById(R.id.reg_store_name);
        EditText storeAddressInput = findViewById(R.id.reg_store_address);
        EditText storePhoneNumberInput = findViewById(R.id.reg_store_phone_number);
        Button registerStore = findViewById(R.id.reg_store_button);
        Button cancelStore = findViewById(R.id.cancel_store_button);

        CardView aboutStoreCard = findViewById(R.id.about_store_card);
        TextView storeName = findViewById(R.id.store_name);
        TextView storeAddress = findViewById(R.id.store_address);
        TextView storePhoneNumber = findViewById(R.id.store_phone_num);
        Button viewStore = findViewById(R.id.view_store);

        refreshAccount();

        name.setText(loggedAccount.name);
        email.setText(loggedAccount.email);
        balance.setText(String.valueOf(loggedAccount.balance));

        if(loggedAccount.store == null){
            register.setVisibility(View.VISIBLE);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    register.setVisibility(View.INVISIBLE);
                    registerStoreCard.setVisibility(View.VISIBLE);
                }
            });
        } else {
            register.setVisibility(View.INVISIBLE);
            aboutStoreCard.setVisibility(View.VISIBLE);
            storeName.setText(loggedAccount.store.name);
            storeAddress.setText(loggedAccount.store.address);
            String bal = String.valueOf(loggedAccount.store.phoneNumber);
            storePhoneNumber.setText(bal);
        }

        cancelStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register.setVisibility(View.VISIBLE);
                registerStoreCard.setVisibility(View.INVISIBLE);
            }
        });

        registerStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Store Registered", Toast.LENGTH_LONG).show();
                                myStore = gson.fromJson(object.toString(), Store.class);
                                loggedAccount.store = myStore;
                                aboutStoreCard.setVisibility(View.VISIBLE);
                                storeName.setText(myStore.name);
                                storeAddress.setText(myStore.address);
                                String num = String.valueOf(myStore.phoneNumber);
                                storePhoneNumber.setText(num);
                                registerStoreCard.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e){
                            Toast.makeText(AboutMeActivity.this, "Cannot Register", Toast.LENGTH_LONG).show();
                        }
                    }
                };

                StoreRequest request = new StoreRequest(
                        storeNameInput.getText().toString(),
                        storeAddressInput.getText().toString(),
                        storePhoneNumberInput.getText().toString(),
                        listener, null
                );

                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(request);
            }
        });

        topUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = topUp.getText().toString();
                if(number.equals("")){
                    topUpValue = 0;
                } else {
                    topUpValue = Double.valueOf(number);
                }

                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            Boolean acceptance = new Boolean(response);
                            if(acceptance == true){
                                loggedAccount.balance += topUpValue;
                                balance.setText(String.valueOf(loggedAccount.balance));
                                Toast.makeText(AboutMeActivity.this, "Top Up Successful", Toast.LENGTH_LONG).show();
                            } else{
                                Toast.makeText(AboutMeActivity.this, "Top Up Failed", Toast.LENGTH_LONG).show();
                            }

                        } catch (Throwable e){
                            Toast.makeText(AboutMeActivity.this, "Top Up Failed due to Unknown Issue", Toast.LENGTH_LONG).show();
                        }

                    }
                };

                TopUpRequest request = new TopUpRequest(
                        topUpValue,
                        listener,
                        null
                );
                RequestQueue queue = Volley.newRequestQueue(AboutMeActivity.this);
                queue.add(request);

                topUp.setText("");
            }
        });

        viewStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutMeActivity.this, ViewStoreActivity.class);
                startActivity(intent);
            }
        });


    }


}