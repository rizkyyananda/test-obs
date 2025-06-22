package com.obs.test.dto.response;

import java.io.Serializable;

public class InventoryResponseDTO implements Serializable {
    private Long id;
    private Long itemId;
    private int qty;
    private String type;

    public InventoryResponseDTO(long id, long itemId, int qty, String type) {
        this.id = id;
        this.itemId = itemId;
        this.qty = qty;
        this.type = type;
    }

    public InventoryResponseDTO() {

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
