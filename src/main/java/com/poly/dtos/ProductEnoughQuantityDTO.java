package com.poly.dtos;

import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import com.poly.entity.Product;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder

public class ProductEnoughQuantityDTO {
    private List<String> errors;
    private List<Product> products;
}
