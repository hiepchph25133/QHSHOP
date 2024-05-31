package com.poly.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;


@SuppressWarnings("serial")
@Data
@Entity 
@Table(name = "ImageProduct")
public class ImageProduct implements Serializable {
	
	@Id	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name= "ID")
	Integer ID;
	String path ;
	Integer ProductID;
}
