package com.windiartaJmartFA;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.windiartaJmartFA.model.Product;

/**
 * This will show the product details
 * @author Windiarta
 */

public class ProductViewActivity extends AppCompatActivity {
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
                Intent intent = new Intent(ProductViewActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

        TextView price = findViewById(R.id.product_price_view);
        TextView name = findViewById(R.id.product_name_view);
        TextView discount = findViewById(R.id.desc_discount);
        TextView weight = findViewById(R.id.desc_weight);
        TextView condition = findViewById(R.id.desc_condition);
        TextView category = findViewById(R.id.desc_category);
        TextView shipment = findViewById(R.id.desc_shipment);
        Button buyProduct = findViewById(R.id.buy_product);

        Product product = MainActivity.getProduct();

        price.setText(String.format("%.2f", product.price));
        name.setText(product.name);
        discount.setText(String.format("%.2f", product.discount));
        weight.setText(String.valueOf(product.weight));
        if(product.conditionUsed){
            condition.setText("Used");
        } else {
            condition.setText("New");
        }
        category.setText(String.valueOf(product.category));
        shipment.setText(getShipmentString(product.shipmentPlans));

        buyProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProductViewActivity.this, PaymentActivity.class);
                startActivity(intent);
            }
        });

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
