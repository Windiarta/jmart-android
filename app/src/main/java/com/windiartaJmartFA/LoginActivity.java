package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.windiartaJmartFA.request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final Gson gson = new Gson();
    private static Account loggedAccount;

    public static Account getLoggedAccount(){
        return loggedAccount;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditText email = findViewById(R.id.LoginEmail);
        EditText password = findViewById(R.id.LoginPassword);
        Button button = findViewById(R.id.LoginButton);
        TextView text = findViewById(R.id.LoginText);
        TextView register = findViewById(R.id.LoginRegister);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(LoginActivity.this, "Log-in Success", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                loggedAccount = gson.fromJson(object.toString(), Account.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e){
                            Toast.makeText(LoginActivity.this, "Log-in Error",  Toast.LENGTH_SHORT);
                        }
                    }
                };

                LoginRequest request = new LoginRequest(
                        email.getText().toString(),
                        password.getText().toString(),
                        listener,
                        null
                );

                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(request);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                } catch (Throwable e){
                    Toast.makeText(LoginActivity.this, "Cannot move", Toast.LENGTH_LONG);
                }
            }
        });
    }
}