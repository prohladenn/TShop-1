package com.example.tshop.t_shop.Orders;


import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tshop.t_shop.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    ArrayList<Order> orders;
    private final Listener onOrderClickListener;

    public OrderAdapter(ArrayList<Order> orders, Listener onOrderClickListener) {
        this.orders = orders;
        this.onOrderClickListener = onOrderClickListener;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
        view.setOnClickListener(v -> onOrderClickListener.onOrderClick((Integer) v.getTag()));
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i) {
        Order order = orders.get(i);
        orderViewHolder.bind(order);
        orderViewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    static final class OrderViewHolder extends RecyclerView.ViewHolder {

        private TextView orderNumber;
        private TextView date;
        private TextView count;

        OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            orderNumber = itemView.findViewById(R.id.order_number_text_view);
            date = itemView.findViewById(R.id.date_text_view);
            count = itemView.findViewById(R.id.count_text_view);
        }

        private void bind(@NonNull Order order) {

            orderNumber.setText("Заказ №" + order.getOrderNumber());
            count.setText(order.getCount().toString()+"р");
            SimpleDateFormat dateFormat= new SimpleDateFormat("dd MMM HH:mm",new Locale("ru"));
            date.setText(dateFormat.format(order.getDateCreation().toDate()));

        }

    }

    interface Listener {

        void onOrderClick(int i);

    }

}