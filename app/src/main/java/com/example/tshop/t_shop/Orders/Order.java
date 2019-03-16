package com.example.tshop.t_shop.Orders;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class Order {
    private Long count;
    private Timestamp dateCreation;
    private Long orderNumber;
    private DocumentReference basketRef;
    private String status;
    private DocumentReference userRef;
    private DocumentReference shopRef;

    protected Order(Long count,
                    Timestamp dateCreation,
                    Long orderNumber,
                    DocumentReference basketRef,
                    String status,
                    DocumentReference userRef, DocumentReference shopRef) {
        this.count = count;
        this.dateCreation = dateCreation;
        this.orderNumber = orderNumber;
        this.basketRef = basketRef;
        this.status = status;
        this.userRef = userRef;
        this.shopRef = shopRef;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public Timestamp getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Timestamp dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public DocumentReference getBasketRef() {
        return basketRef;
    }

    public void setBasketRef(DocumentReference basketRef) {
        this.basketRef = basketRef;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DocumentReference getUserRef() {
        return userRef;
    }

    public void setUserRef(DocumentReference userRef) {
        this.userRef = userRef;
    }

    public DocumentReference getShopRef() {
        return shopRef;
    }

    public void setShopRef(DocumentReference shopRef) {
        this.shopRef = shopRef;
    }
}