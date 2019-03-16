package com.example.tshop.t_shop.Orders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;

import com.example.tshop.t_shop.Products.ProductsActivity;
import com.example.tshop.t_shop.R;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class OrdersActivity extends AppCompatActivity {

    ArrayList<Order> orders = new ArrayList<>();
    OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    ImageView buttonBack;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFirestore;
    private FirebaseUser currntUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        mAuth = FirebaseAuth.getInstance();
        currntUser = mAuth.getCurrentUser();
        mFirestore = FirebaseFirestore.getInstance();
        buttonBack = findViewById(R.id.orders_button_back);
        recyclerView = findViewById(R.id.orders_recycler_view);

        generateOrderList();

        orderAdapter = new OrderAdapter(orders, this::onOrderListener);
        recyclerView.setAdapter(orderAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        backListener();
    }

    private void onOrderListener(int i) {
        /*
        todo inventoryRef с тебя, а флвг готов
         */
        Intent intent = new Intent(OrdersActivity.this, ProductsActivity.class);
        intent.putExtra("refPath", orders.get(i).getBasketRef().getPath());
        intent.putExtra("order", true);
        startActivity(intent);
    }

    void backListener() {
        buttonBack.setOnClickListener(v -> onBackPressed());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /*
    todo на тебе через бд
     */
    private void generateOrderList() {
        mFirestore.collection("orders").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (e != null) {
                return;
            }
            for (QueryDocumentSnapshot docItem : queryDocumentSnapshots) {
                if (docItem.getDocumentReference("customer").equals(mFirestore.collection("users").document(currntUser.getUid()))) {
                    Task<DocumentSnapshot> task = docItem.getDocumentReference("basket").get();
                    while (!task.isComplete()) {
                        Log.d("LOGI", "WaitOwen");
                    }
                    DocumentSnapshot doc = task.getResult();
                    orders.add(new Order(doc.getLong("totalPrice"),
                            docItem.getTimestamp("dateCreation"),
                            docItem.getLong("number"),
                            docItem.getDocumentReference("basket"),
                            docItem.getString("status"),
                            docItem.getDocumentReference("customer"),
                            docItem.getDocumentReference("shop")

                    ));
                }

            }
            orderAdapter = new OrderAdapter(orders, this::onOrderListener);
            recyclerView.setAdapter(orderAdapter);
        });

    }
}