package com.obs.test.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Data
public class Item extends Auditable{
    @Id
    @SequenceGenerator(allocationSize = 1, name = "item_idgen", sequenceName = "item_seq")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_idgen")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    @OneToMany(mappedBy = "item")
    private List<Order> orders;

    @OneToMany(mappedBy = "item")
    private List<Inventory> inventories;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Inventory> getInventories() {
        return inventories;
    }

    public void setInventories(List<Inventory> inventories) {
        this.inventories = inventories;
    }
}
