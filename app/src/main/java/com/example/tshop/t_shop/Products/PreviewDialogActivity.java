package com.example.tshop.t_shop.Products;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tshop.t_shop.Products.Product;
import com.example.tshop.t_shop.R;
import com.squareup.picasso.Picasso;

public class PreviewDialogActivity extends Activity {

    private ImageView avatarDescImage;
    private TextView nameDescText;
    private TextView amountDescText;
    private TextView unitDescText;
    private TextView descDescTest;

    private TextView countTextView;
    private TextView priceTextView;
    private TextView priceCurTextView;
    private FrameLayout buyButton;
    private FrameLayout addButton;
    private FrameLayout deleteButton;

    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_dialog);

        Intent intent = getIntent();

        avatarDescImage = findViewById(R.id.product_desk_avatar);
        nameDescText = findViewById(R.id.product_desk_name);
        amountDescText = findViewById(R.id.product_desk_value_amount);
        unitDescText = findViewById(R.id.product_desk_value_unit);
        descDescTest = findViewById(R.id.product_desk_value_desc);

        countTextView = findViewById(R.id.product_desk_text_count);
        priceTextView = findViewById(R.id.product_desk_text_price);
        priceCurTextView = findViewById(R.id.product_desk_text_price_cur);
        buyButton = findViewById(R.id.product_desk_button_buy);
        addButton = findViewById(R.id.product_desk_button_add);
        deleteButton = findViewById(R.id.product_desk_button_delete);

        product = (Product) intent.getSerializableExtra("product");

        Picasso.get().load(product.getPictureSource()).into(avatarDescImage);
        nameDescText.setText(product.getName());
        amountDescText.setText(product.getValueAmount());
        unitDescText.setText(product.getValueUnits());
        descDescTest.setText(product.getDescription());
        countTextView.setText(String.valueOf(product.getSelected()));
        priceTextView.setText(String.valueOf(product.getPriceAmount()));
        priceCurTextView.setText(product.getPriceCurrency());

        //todo режим для диалогового окна без + и -
        if (intent.getBooleanExtra("order", false)) {
            buyButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            countTextView.setVisibility(View.VISIBLE);
            // todo нужно другое поле Не getCount()
            countTextView.setText(product.getCount().toString() + "шт.");
        } else {
            if (product.getSelected() > 0)
                inBasket(product);
            else notInBasket(product);
        }
    }

    void inBasket(Product product) {
        buyButton.setVisibility(View.INVISIBLE);
        addButton.setVisibility(View.VISIBLE);
        deleteButton.setVisibility(View.VISIBLE);
        countTextView.setVisibility(View.VISIBLE);
        addButton.setOnClickListener(v1 -> {
            int selected = Integer.valueOf(countTextView.getText().toString());
            if (selected == product.getCount()) {
                Toast.makeText(this, "Больше нет", Toast.LENGTH_SHORT).show();
            } else {
                countTextView.setText(String.format(Integer.toString(selected + 1), ""));
                product.setSelected(product.getSelected() + 1);
            }
        });
        deleteButton.setOnClickListener(v1 -> {
            int selected = Integer.valueOf(countTextView.getText().toString());
            if (selected == 1) {
                notInBasket(product);
                product.setSelected(product.getSelected() - 1);
            } else {
                countTextView.setText(String.format(Integer.toString(selected - 1), ""));
                product.setSelected(product.getSelected() - 1);
            }
        });
    }

    void notInBasket(Product product) {
        countTextView.setText("1");
        buyButton.setVisibility(View.VISIBLE);
        addButton.setVisibility(View.INVISIBLE);
        deleteButton.setVisibility(View.INVISIBLE);
        countTextView.setVisibility(View.INVISIBLE);
        buyButton.setOnClickListener(v -> {
            inBasket(product);
            product.setSelected(product.getSelected() + 1);
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("product", product);
        setResult(RESULT_OK, intent);
        finish();
    }
}