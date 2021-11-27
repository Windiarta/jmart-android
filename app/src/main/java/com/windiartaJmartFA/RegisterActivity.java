package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.windiartaJmartFA.request.RegisterRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        EditText email = findViewById(R.id.RegisterEmail);
        EditText password = findViewById(R.id.RegisterPassword);
        EditText name = findViewById(R.id.RegisterName);
        Button button = findViewById(R.id.RegisterButton);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Response.Listener listener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject object = new JSONObject(response);
                            if(object != null){
                                Toast.makeText(RegisterActivity.this, "Register Success", Toast.LENGTH_SHORT);
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }
                        } catch (JSONException e){
                            Toast.makeText(RegisterActivity.this, "Register Failed", Toast.LENGTH_SHORT);
                        }
                    }
                };

                RegisterRequest request = new RegisterRequest(
                        name.getText().toString(),
                        email.getText().toString(),
                        password.getText().toString(),
                        listener,
                        null
                );

                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(request);
            }
        });
    }
}