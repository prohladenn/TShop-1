package com.example.tshop.t_shop.Products;


import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tshop.t_shop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final ArrayList<Product> products;
    private static TextView amountTextView;
    private static TextView amountCurTextView;
    private static Activity parent;
    private final Listener onProductClickListener;
    static private int sum;

    public ProductAdapter(ArrayList<Product> products, Activity parent, Listener onProductClickListener,
                          TextView amountTextView, TextView amountCurTextView) {
        this.products = products;
        this.onProductClickListener = onProductClickListener;
        ProductAdapter.parent = parent;
        ProductAdapter.amountTextView = amountTextView;
        ProductAdapter.amountCurTextView = amountCurTextView;
        ProductAdapter.sum = 0;
        for (Product p : products) {
            sum += p.getSelected() * p.getPriceAmount();
        }
        ProductAdapter.amountTextView.setText(String.valueOf(ProductAdapter.sum));
        ProductAdapter.amountCurTextView.setText(products.get(0).getPriceCurrency());
        if (sum == 0) {
            amountTextView.setVisibility(View.INVISIBLE);
            amountCurTextView.setVisibility(View.INVISIBLE);
        } else {
            amountTextView.setVisibility(View.VISIBLE);
            amountCurTextView.setVisibility(View.VISIBLE);
        }
    }

    public ArrayList<Product> getBasket() {
        return products;
    }

    public static String getAmountString() {
        return amountTextView.getText().toString();
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.product_item, viewGroup, false);
        view.setOnClickListener(v -> onProductClickListener.onProductClick((Integer) v.getTag()));
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i) {
        Product product = products.get(i);
        productViewHolder.bind(product);
        productViewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static final class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView countTextView;
        private TextView priceTextView;
        private TextView priceCurTextView;
        private ImageView avatarImageView;
        private FrameLayout buyButton;
        private FrameLayout addButton;
        private FrameLayout deleteButton;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.product_item_text_desc);
            countTextView = itemView.findViewById(R.id.product_item_text_count);
            priceTextView = itemView.findViewById(R.id.product_item_text_price);
            priceCurTextView = itemView.findViewById(R.id.product_item_text_price_cur);
            avatarImageView = itemView.findViewById(R.id.product_item_image_photo);
            buyButton = itemView.findViewById(R.id.product_item_button_buy);
            addButton = itemView.findViewById(R.id.product_item_button_add);
            deleteButton = itemView.findViewById(R.id.product_item_button_delete);
        }

        private void bind(@NonNull Product product) {

            countTextView.setText(String.valueOf(product.getSelected()));
            nameTextView.setText(product.getName());
            priceTextView.setText(String.valueOf(product.getPriceAmount()));
            priceCurTextView.setText(product.getPriceCurrency());
            Picasso.get().load(product.getPictureSource()).resize(80, 80).into(avatarImageView);

            if (product.getSelected() > 0)
                inBasket(product);
            else notInBasket(product);
        }

        void inBasket(Product product) {
            buyButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.VISIBLE);
            countTextView.setVisibility(View.VISIBLE);
            addButton.setOnClickListener(v1 -> {
                int selected = Integer.valueOf(countTextView.getText().toString());
                if (amountTextView.getText().toString().equals("0")) {
                    amountTextView.setVisibility(View.VISIBLE);
                    amountCurTextView.setVisibility(View.VISIBLE);
                }
                if (selected == product.getCount()) {
                    Toast.makeText(parent, "Больше нет", Toast.LENGTH_SHORT).show();
                } else {
                    countTextView.setText(String.format(Integer.toString(selected + 1), ""));
                    sum += product.getPriceAmount();
                    amountTextView.setText(String.valueOf(sum));
                    product.setSelected(product.getSelected() + 1);
                }
            });
            deleteButton.setOnClickListener(v1 -> {
                int selected = Integer.valueOf(countTextView.getText().toString());
                sum -= product.getPriceAmount();
                amountTextView.setText(String.valueOf(sum));
                if (amountTextView.getText().toString().equals("0")) {
                    amountTextView.setVisibility(View.INVISIBLE);
                    amountCurTextView.setVisibility(View.INVISIBLE);
                }
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
                if (amountTextView.getText().toString().isEmpty() || amountTextView.getText().toString().equals("0")) {
                    amountTextView.setVisibility(View.VISIBLE);
                    sum += product.getPriceAmount();
                    amountTextView.setText(String.valueOf(sum));
                    amountCurTextView.setVisibility(View.VISIBLE);
                    amountCurTextView.setText(product.getPriceCurrency());
                } else {
                    sum += product.getPriceAmount();
                    amountTextView.setText(String.valueOf(sum));
                }
                inBasket(product);
                product.setSelected(product.getSelected() + 1);
            });
        }

    }

    public interface Listener {

        void onProductClick(int i);

    }

}