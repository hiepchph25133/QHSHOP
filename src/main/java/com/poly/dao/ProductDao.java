package com.poly.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.poly.entity.Account;
import com.poly.entity.Authority;
import com.poly.entity.Category;
import com.poly.entity.Product;

import com.poly.entity.ReportCategory;
import com.poly.entity.ReportTrademark;

public interface ProductDao extends JpaRepository<Product, Integer>, JpaSpecificationExecutor<Product> {
//	Page<Product> findByQuantityGreaterThan(Integer quantity,Pageable pageable);
	@Query("SELECT p FROM Product p WHERE p.category.id=?1")
	List<Product> findByCategoryId(Integer cid);

	@Query("SELECT p FROM Product p WHERE p.trademark.id=?1")
	List<Product> findByTrademarkId(Integer tid);
	
	
	@Query("SELECT p FROM Product p WHERE p.Distcount > 0")
	List<Product> findByDis();

	@Query(value = "SELECT p FROM Product p WHERE p.Lastest = true")
	List<Product> findByLatest();

		@Query("SELECT p FROM Product p WHERE p.Special = true")
	List<Product> findBySpecial();

		@Query("SELECT p FROM Product p "
				+ " WHERE p.Name LIKE %:kw% OR p.category.name LIKE %:kw%")
		List<Product> findByKeywords(@Param("kw") String keywords);

		@Query("SELECT new ReportCategory(o.category.Category_id,o.category.name, sum(o.Unit_price), count(o)) "
				+ " FROM Product o "
				+ " GROUP BY o.category.Category_id ,o.category.name"
				+ " ORDER BY sum(o.Unit_price) DESC")
		List<ReportCategory> getReportCategory();
		@Query("SELECT p FROM Product p WHERE p.Distcount > 0")
		List<Product> findByAllDis();
		@Query("SELECT p FROM Product p WHERE p.Special = true")
		List<Product> findByAllSpe();
		@Query(value = "SELECT p FROM Product p WHERE p.Lastest = true")
		List<Product> findByAllLat();
		
		@Query("SELECT new ReportTrademark(o.trademark.Trademark_id,o.trademark.name, sum(o.Unit_price), count(o)) "
				+ " FROM Product o "
				+ " GROUP BY o.trademark.Trademark_id ,o.trademark.name"
				+ " ORDER BY sum(o.Unit_price) DESC")
		List<ReportTrademark> getReportTrademark();
		
		@Query(value="SELECT TOP (10) * FROM Products p ORDER BY p.Product_date DESC",nativeQuery = true)
		List<Product> getTop10();
		
		@Query(value="SELECT * FROM Products p ORDER BY p.Unit_price DESC",nativeQuery = true)
		List<Product> getDesc();
		
		@Query(value="SELECT * FROM Products p ORDER BY p.Unit_price ASC",nativeQuery = true)
		List<Product> getAsc();
		
		@Query(value="select * from Products where Unit_price between ?1 and ?2  ",nativeQuery = true)
		List<Product> find(@Param("MinPrice") Integer unit_price, @Param("MaxPrice") Integer unit_price1
				);


		@Query(value="SELECT *\n" +
				"FROM Products\n" +
				"WHERE Products.Unit_price BETWEEN ?1 AND ?2\n" +
				"    AND Products.Category_id LIKE CONCAT('%', ?3, '%')\n" +
				"    AND Products.Trademark_id LIKE CONCAT('%', ?4, '%')\n" +
				"    AND Products.Size_id LIKE CONCAT('%', ?5, '%')\n" +
				"    AND Products.Color_id LIKE CONCAT('%', ?6, '%')\n" +
				"    AND Products.Material_id LIKE CONCAT('%', ?7, '%');\n", nativeQuery = true)
		List<Product> findByAllKeyWord(
				@Param("MinPrice") Integer unit_price, @Param("MaxPrice") Integer unit_price1  ,
					@Param("Category_id") String Category_id , @Param("Trademark_id") String Trademark_id , @Param("Size_id") String Size_id ,
				@Param("Color_id") String Color_id , @Param("Material_id") String Material_id);
		
		@Query(value="select * from Products where Category_id like '1'", nativeQuery = true)
		List<Product> findByLaptop();
		@Query(value = "select * from Products where Product_id like %:kw% or \r\n"
				+ " Name like %:kw%  " , nativeQuery = true)
		List<Product> finbyIdOrName(@Param("kw") String keywords);

		@Query(value="select * from Products INNER JOIN product_detail on Products.Product_id = product_detail.ProductID " +
				" where Products.Unit_price between ?1 and ?2 and Products.Category_id  like %?3% and Products.Trademark_id like %?4% " +
				"   and  Products.Status like %?5%  ", nativeQuery = true)
		List<Product> findByAllKeyWordAdmin(@Param("MinPrice") Integer unit_price, @Param("MaxPrice") Integer unit_price1   ,
				@Param("Category_id") String Category_id , @Param("Trademark_id") String Trademark_id , 
				@Param("Status") String Status );
		
}
