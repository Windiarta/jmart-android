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
import com.windiartaJmartFA.request.StoreRequest;
import com.windiartaJmartFA.request.TopUpRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class AboutMeActivity extends AppCompatActivity {
    Gson gson = new Gson();
    private static Store myStore;
    private double topUpValue = 0;
    Account loggedAccount = LoginActivity.getLoggedAccount();

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

        TextView nama = findViewById(R.id.accountname);
        TextView email = findViewById(R.id.accountemail);
        TextView balance = findViewById(R.id.accountbalance);
        EditText topUp = findViewById(R.id.accounttopup);
        Button topUpButton = findViewById(R.id.accounttopupbutton);

        CardView registercard = findViewById(R.id.registerstorebuttoncard);
        Button register = findViewById(R.id.registerstorebutton);

        CardView registerstorecard = findViewById(R.id.registerstorecard);
        EditText storeNameInput = findViewById(R.id.regstorename);
        EditText storeAddressInput = findViewById(R.id.regstoreaddress);
        EditText storePhoneNumberInput = findViewById(R.id.regstorephonenumber);
        Button registerStore = findViewById(R.id.regstorebutton);
        Button cancelStore = findViewById(R.id.cancelstorebutton);

        CardView aboutstorecard = findViewById(R.id.aboutstorecard);
        TextView storeName = findViewById(R.id.storename);
        TextView storeAddress = findViewById(R.id.storeaddress);
        TextView storePhoneNumber = findViewById(R.id.storephonenum);

        nama.setText(loggedAccount.name);
        email.setText(loggedAccount.email);
        balance.setText(String.valueOf(loggedAccount.balance));

        if(loggedAccount.store == null){
            registercard.setVisibility(View.VISIBLE);
            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    registercard.setVisibility(View.INVISIBLE);
                    registerstorecard.setVisibility(View.VISIBLE);
                }
            });
        } else {
            registercard.setVisibility(View.INVISIBLE);
            aboutstorecard.setVisibility(View.VISIBLE);
            storeName.setText(loggedAccount.store.name);
            storeAddress.setText(loggedAccount.store.address);
            String bal = String.valueOf(loggedAccount.store.phoneNumber);
            storePhoneNumber.setText(bal);
        }

        cancelStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registercard.setVisibility(View.VISIBLE);
                registerstorecard.setVisibility(View.INVISIBLE);
            }
        });

        registerStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(AboutMeActivity.this, "Store Registered", Toast.LENGTH_LONG).show();
                                myStore = gson.fromJson(object.toString(), Store.class);
                                loggedAccount.store = myStore;
                                aboutstorecard.setVisibility(View.VISIBLE);
                                storeName.setText(myStore.name);
                                storeAddress.setText(myStore.address);
                                String num = String.valueOf(myStore.phoneNumber);
                                storePhoneNumber.setText(num);
                                registerstorecard.setVisibility(View.INVISIBLE);
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

                System.out.println(storeNameInput.getText().toString());

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

    }
}