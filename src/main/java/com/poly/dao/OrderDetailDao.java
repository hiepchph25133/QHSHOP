package com.poly.dao;

import java.util.List;

import javax.transaction.Transactional;

import com.poly.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetail, Integer>{
	@Query("SELECT o FROM OrderDetail o WHERE o.order.Order_id = ?1")
	List<OrderDetail> findByOrderID(Integer orderid);
	@Transactional
	@Modifying
	@Query(value="delete  from OrderDetails where Order_id = :order_id" ,nativeQuery = true)
	void deleteOrderId(@Param("order_id") Integer order_id);

	@Query(value="select *  from OrderDetails where Order_id = :order_id" ,nativeQuery = true)
	List<OrderDetail> findByOrder(@Param("order_id") Integer idOrder);

	@Query("SELECT o FROM OrderDetail o WHERE o.Orderdetail_id = ?1")
	OrderDetail findOneByOrderDetailId(Integer orderid);

	@Query("SELECT o FROM OrderDetail o WHERE o.order.Order_id = ?1 and o.product.Product_id = ?2")
	OrderDetail findOneByOrderIdAndProductID(Integer orderID, Integer productID);

	OrderDetail findByProduct(Product product);

}