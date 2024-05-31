package com.poly.dao;

import com.poly.entity.ProductDetail;
import com.poly.entity.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductDetailDao extends JpaRepository<ProductDetail, Integer>{
    @Modifying
    @Query(value = "DELETE FROM product_detail  WHERE product_detail.ProductID = :productId",nativeQuery = true)
    void deleteByProductID(Integer productId);
    @Query(value = "Select * FROM product_detail  WHERE product_detail.ProductID = :productId",nativeQuery = true)
    ProductDetail findByProductID(Integer productId);
}
