package com.poly.mappers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.poly.dtos.BillDTO;
import com.poly.entity.Order;
import com.poly.entity.OrderDetail;

import java.util.List;
import java.util.stream.Collectors;

public class JsonMapper {
    public static BillDTO convertToBillDTO(JsonNode orderData){
        ObjectMapper mapper= new ObjectMapper();
        Order order = mapper.convertValue(orderData, Order.class);
        TypeReference<List<OrderDetail>> type =new TypeReference<List<OrderDetail>>() {};
        List<OrderDetail> details=mapper.convertValue(orderData.get("orderDetails"),type )
                .stream().peek(d ->d.setOrder(order)).collect(Collectors.toList());
        BillDTO billDTO = BillDTO.builder().orderDetails(details).order(order).build();
        return billDTO;
    }
}
