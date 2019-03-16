package com.example.tshop.t_shop.Orders;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tshop.t_shop.Products.Product;
import com.example.tshop.t_shop.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderSimpleAdapter extends RecyclerView.Adapter<OrderSimpleAdapter.ProductViewHolder> {

    private final ArrayList<Product> products;
    private final Listener onProductClickListener;

    public OrderSimpleAdapter(ArrayList<Product> products, Listener onProductClickListener) {
        this.products = products;
        this.onProductClickListener = onProductClickListener;
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
            // todo нужно другое поле Не getCount()
            countTextView.setText(product.getCount().toString() + "шт.");
            buyButton.setVisibility(View.INVISIBLE);
            addButton.setVisibility(View.INVISIBLE);
            deleteButton.setVisibility(View.INVISIBLE);
            countTextView.setVisibility(View.VISIBLE);
        }

    }

    public interface Listener {

        void onProductClick(int i);

    }

}