package com.obs.test.dto.request;

import java.io.Serializable;

public class InventoryRequestDTO implements Serializable {
    private Long id;
    private Long itemId;
    private int qty;
    private String type;

    public InventoryRequestDTO(Long itemId, int qty, String type) {
        this.itemId =itemId;
        this.qty = qty;
        this.type = type;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getItemId() {
        return itemId;
    }

    public void setItemId(Long itemId) {
        this.itemId = itemId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
