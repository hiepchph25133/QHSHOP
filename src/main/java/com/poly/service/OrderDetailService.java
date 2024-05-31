package com.poly.service;

import java.util.List;

import com.poly.entity.OrderDetail;

public interface OrderDetailService {

	List<OrderDetail> findByOrderID(Integer orderid);
	OrderDetail findOneByOrderDetailID(Integer orderDetailId);
	void update(OrderDetail orderDetail);
	void delete(Integer orderDetailId);
	void add(OrderDetail orderDetail);
	OrderDetail findOneByOrderIDAndProductID(Integer orderId, Integer productID);
}
