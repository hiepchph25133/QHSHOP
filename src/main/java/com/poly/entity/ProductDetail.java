package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;


@SuppressWarnings("serial")
@Data
//@Builder
@Entity
@Table(name = "ProductDetail")
public class ProductDetail implements Serializable {
	public ProductDetail() {

	}
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "DetailID")
	Integer ID;
	String Detail ;
	String Description;
	Integer ProductID;
}
