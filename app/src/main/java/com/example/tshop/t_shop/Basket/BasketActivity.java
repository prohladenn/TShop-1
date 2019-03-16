package com.example.tshop.t_shop.Basket;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tshop.t_shop.Products.PreviewDialogActivity;
import com.example.tshop.t_shop.Products.Product;
import com.example.tshop.t_shop.Products.ProductAdapter;
import com.example.tshop.t_shop.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.functions.FirebaseFunctions;
import com.google.firebase.functions.FirebaseFunctionsException;
import com.google.firebase.functions.HttpsCallableResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

public class BasketActivity extends AppCompatActivity {

    ArrayList<Product> products;
    private FirebaseFunctions mFunctions;
    ProductAdapter productAdapter;
    RecyclerView recyclerView;
    TextView amountTextView;
    TextView amountCurTextView;
    ImageView buttonBack;
    FrameLayout buttonBuy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mFunctions = FirebaseFunctions.getInstance();
        buttonBack = findViewById(R.id.basket_button_back);
        amountTextView = findViewById(R.id.basket_text_amount);
        amountCurTextView = findViewById(R.id.basket_text_amount_cur);
        recyclerView = findViewById(R.id.basket_recycler_view);
        products = generateStudentList();
        productAdapter = new ProductAdapter(products, this, this::onProductClick,
                amountTextView, amountCurTextView);
        recyclerView.setAdapter(productAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        buttonBuy = findViewById(R.id.basket_button_buy);
        buyListener();
        backListener();
    }

    void buyListener() {
        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMessage()
                        .addOnCompleteListener(new OnCompleteListener<String>() {
                            @Override
                            public void onComplete(@NonNull Task<String> task) {
                                if (!task.isSuccessful()) {
                                    Exception e = task.getException();
                                    if (e instanceof FirebaseFunctionsException) {
                                        FirebaseFunctionsException ffe = (FirebaseFunctionsException) e;
                                        FirebaseFunctionsException.Code code = ffe.getCode();
                                        Object details = ffe.getDetails();
                                    }

                                }
                            }
                        });

            }
        });
    }

    void backListener() {
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    private void onProductClick(int i) {
        Intent dialog = new Intent(BasketActivity.this, PreviewDialogActivity.class);
        dialog.putExtra("product", products.get(i));
        startActivityForResult(dialog, 1111);
    }
        private Task<String> addMessage() {
            // Create the arguments to the callable function.
            Map<String, Object> basketItem = new HashMap<>();
            ArrayList<Map> array = new ArrayList<>();
            basketItem.put("shop", getIntent().getStringExtra("shopPath"));
            for( Product pr: products){
                Map<String, Object> item = new HashMap<>();

                item.put("product",pr.getId());
                item.put("count",pr.getSelected());
                array.add(item);
            }
            basketItem.put("basketItems",array);

            Log.d("dads",basketItem.toString());
            return mFunctions
                    .getHttpsCallable("createNewOrder")
                    .call(basketItem)
                    .continueWith(new Continuation<HttpsCallableResult, String>() {
                        @Override
                        public String then(@NonNull Task<HttpsCallableResult> task) throws Exception {
                            // This continuation runs on either success or failure, but if the task
                            // has failed then getResult() will throw an Exception which will be
                            // propagated down.

                            String result = (String) task.getResult().getData();

                            return result;
                        }
                    });
        }


    @Override
    public void onBackPressed() {
        products = productAdapter.getBasket();
        Intent intent = new Intent();
        intent.putExtra("data", products);
        intent.putExtra("sum", ProductAdapter.getAmountString());
        setResult(RESULT_OK, intent);
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && resultCode == RESULT_OK && data != null) {
            Product product = (Product) data.getSerializableExtra("product");
            for (Product p :
                    products) {
                if (p.equals(product)) {
                    p.setSelected(product.getSelected());
                }
            }
            productAdapter = new ProductAdapter(products, this, this::onProductClick, amountTextView,
                    amountCurTextView);
            recyclerView.setAdapter(productAdapter);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(linearLayoutManager);
        }
    }

    private ArrayList<Product> generateStudentList() {
        ArrayList<Product> data = (ArrayList<Product>) getIntent().getSerializableExtra("data");
        ListIterator<Product> i = data.listIterator();
        while (i.hasNext()) {
            Product p = i.next();
            if (p.getSelected() == 0)
                i.remove();
        }
        return data;
    }

}