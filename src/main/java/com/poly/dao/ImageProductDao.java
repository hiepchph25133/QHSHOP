package com.poly.dao;

import com.poly.entity.ImageProduct;
import com.poly.entity.ProductDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageProductDao extends JpaRepository<ImageProduct, Integer>{
   @Modifying
   @Query(value = "DELETE FROM image_product WHERE image_product.ProductID = :productId",nativeQuery = true)
   void deleteByProductID(Integer productId);

   @Query(value = "Select * FROM image_product  WHERE image_product.ProductID = :productId",nativeQuery = true)
   List<ImageProduct> findByProductID(Integer productId);
}
