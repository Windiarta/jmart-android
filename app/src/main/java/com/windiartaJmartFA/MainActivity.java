package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.MenuItemCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.windiartaJmartFA.model.Product;
import com.windiartaJmartFA.model.ProductCategory;
import com.windiartaJmartFA.request.ProductRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Gson gson = new Gson();
    private static List<Product> array = new ArrayList<>();
    private static List<String> arrayString = new ArrayList<>();
    private static int pageNumber = 0;
    private static int accountId = -1;
    private static final int pageSize = 10;
    private static String nameSearch = "";
    private static int conditionUsed = 0; //0 = both, 1 = used, 2 = new
    private static byte shipmentPlan = 1<<0;
    private static int minPrice = 0;
    private static int maxPrice = 0;
    private static ProductCategory category = null;
    public static Product selected;

    ArrayAdapter<String> adapter;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        MenuItem account = menu.findItem(R.id.account);
        account.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, AboutMeActivity.class);
                startActivity(intent);
                return true;
            }
        });
        MenuItem addbox = menu.findItem(R.id.addbox);
        addbox.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(MainActivity.this, CreateProductActivity.class);
                startActivity(intent);
                return true;
            }
        });
        if(LoginActivity.getLoggedAccount().store == null){
            addbox.setVisible(false);
        }

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView product = findViewById(R.id.product);
        TextView filter = findViewById(R.id.filter);
        CardView productCard = findViewById(R.id.product_card);
        Button prev = findViewById(R.id.prev_button);
        Button next = findViewById(R.id.next_button);
        EditText page = findViewById(R.id.page);
        Button go = findViewById(R.id.go_button);
        ListView list = (ListView)findViewById(R.id.list);
        CardView filterCard = findViewById(R.id.filter_card);
        EditText name = findViewById(R.id.name_area);
        EditText low = findViewById(R.id.lowest_filter);
        EditText high = findViewById(R.id.highest_filter);
        CheckBox newFilter = findViewById(R.id.new_check_box);
        CheckBox usedFilter = findViewById(R.id.used_check_box);
        Spinner categorySp = findViewById(R.id.category_spinner);
        CheckBox storeOnly = findViewById(R.id.only_store);
        Button applyFilter = findViewById(R.id.apply_filter);
        Button clearFilter = findViewById(R.id.clear_filter);

        request(list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayString);
        list.setAdapter(adapter);
        list.invalidateViews();
        list.refreshDrawableState();

        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pageNumber = new Integer(page.getText().toString());
                request(list);
            }
        });

        product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(nameSearch);
                productCard.setVisibility(View.VISIBLE);
                filterCard.setVisibility(View.INVISIBLE);
                request(list);
            }
        });

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filterCard.setVisibility(View.VISIBLE);
                productCard.setVisibility(View.INVISIBLE);
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
                    Toast.makeText(MainActivity.this, "You are in the first page", Toast.LENGTH_LONG).show();
                }
            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSearch = "";
                pageNumber = 0;
                accountId = -1;
                minPrice = 0;
                maxPrice = 0;
                conditionUsed = 1<<0;
                category = null;
                shipmentPlan = 1<<0;
                Toast.makeText(MainActivity.this, "All Filter Cleared", Toast.LENGTH_SHORT).show();
            }
        });

        applyFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameSearch = name.getText().toString();
                if(low.getText().toString() == null){
                    minPrice = 0;
                } else {
                    minPrice = new Integer(low.getText().toString());
                }
                if(high.getText().toString() == null){
                    maxPrice = 0;
                } else {
                    maxPrice = new Integer(high.getText().toString());
                }
                if(newFilter.isChecked() && !usedFilter.isChecked()){
                    conditionUsed = 1<<1;
                } else if(!newFilter.isChecked() && usedFilter.isChecked()){
                    conditionUsed = 1<<2;
                } else {
                    conditionUsed = 1<<0;
                }
                if(categorySp.getSelectedItem().toString().equals("ALL")){
                    category = null;
                } else {
                    category = ProductCategory.valueOf(categorySp.getSelectedItem().toString());
                }
                if(storeOnly.isChecked()){
                    accountId = LoginActivity.getLoggedAccount().id;
                } else {
                    accountId = -1;
                }
                Toast.makeText(MainActivity.this, "All Filter Applied", Toast.LENGTH_LONG).show();
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected = array.get(position);
                Intent intent = new Intent(MainActivity.this, ProductViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public static Product getProduct(){
        return selected;
    }

    void request(ListView list){
        Response.Listener listener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray objects = new JSONArray(response);
                    array.clear();
                    arrayString.clear();
                    for(int i = 0; i < objects.length(); i++){
                        JSONObject object = objects.getJSONObject(i);
                        array.add(gson.fromJson(object.toString(), Product.class));
                    }
                    for (Product prod : array){
                        arrayString.add(String.valueOf(prod.name));
                    }
                    list.invalidateViews();
                    list.refreshDrawableState();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        ProductRequest request = new ProductRequest(
                pageNumber,
                pageSize,
                accountId,
                nameSearch,
                minPrice,
                maxPrice,
                String.valueOf(category),
                listener,
                null
        );
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        queue.add(request);
    }
}