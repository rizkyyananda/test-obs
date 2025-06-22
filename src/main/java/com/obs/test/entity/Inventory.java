package com.obs.test.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Inventory extends Auditable {

    @Id
    @SequenceGenerator(allocationSize = 1, name = "inventory_idgen", sequenceName = "inventory_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "inventory_idgen")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    @Column(nullable = false)
    private int qty;

    @Column(nullable = false)
    private String type;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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