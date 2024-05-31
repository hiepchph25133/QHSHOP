package com.poly.service;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.poly.dtos.BillDTO;
import com.poly.entity.Order;
import com.poly.entity.OrderDetail;
import com.poly.entity.Product;

public interface OrderService {


	Order create(JsonNode orderData,String code) throws Throwable;

	Order createBillSell(BillDTO billDTO,String code) throws Throwable;
	Order createBillSellPending(BillDTO billDTO,String code) throws Throwable;

	Order findById(Integer id);

	List<Order> findByUsername(String username);

	List<Order> findByAllDesc();

	void deleteById(Integer id);

	Order update(Order order);
	List<Order> findByStatus(int status);
	BillDTO getBillDetail(int idOrder);

	public void addProductToOrder(Order order, Product product, int quantity);

	public Order findOrderById(Integer orderId);

	public void updateOrder(Order order);
}