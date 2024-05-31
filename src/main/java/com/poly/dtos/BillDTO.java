package com.poly.dtos;

import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class BillDTO {
    private Order order;
    private List<OrderDetail> orderDetails;
}
