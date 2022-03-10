package com.webScraping.demo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class ResponseDTO {

	private @Id @GeneratedValue Long id;
	private String title;
	@Column(length = 455)
	private String url;
	private String image;
	private String price;
	
	//@ManyToOne
    //@JoinColumn(name="request_id", nullable=false)
    //private RequestDTO request;
	
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
	public ResponseDTO() {
		super();
	}
	
	
	public ResponseDTO(String title, String url, String image, String price) {
		super();
		this.title = title;
		this.url = url;
		this.image = image;
		this.price = price;
		//this.request = request;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	
    
    
}
