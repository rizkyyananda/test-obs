package com.obs.test.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ItemResponseDTO implements Serializable {
    private Long id;
    private String name;
    private double price;

    public ItemResponseDTO(long id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    public ItemResponseDTO() {
        super();
    }

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
}
