package com.poly.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.poly.entity.Vote;


public interface VoteDao extends JpaRepository<Vote, Integer> {
	@Query("select v from Vote v where v.product.Product_id=?1 order by v.Vote_id desc")
	List<Vote> findbyProductId(Integer product_id);

	@Query("SELECT CASE WHEN COUNT(v) > 0 THEN TRUE ELSE FALSE END FROM Vote v WHERE v.account.Username = ?1 AND v.product.Product_id = ?2")
	boolean existsByAccount_UsernameAndProduct_ProductId(String username, Integer productId);
	
}
