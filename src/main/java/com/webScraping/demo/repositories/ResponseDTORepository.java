package com.webScraping.demo.repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.webScraping.demo.model.RequestDTO;
import com.webScraping.demo.model.ResponseDTO;

public interface ResponseDTORepository extends JpaRepository<ResponseDTO,Long>{

	//Set<ResponseDTO> findByRequest(RequestDTO request);
	
	ResponseDTO findByTitle(String title);
	List<ResponseDTO> findByPrice(Double price);
	ResponseDTO findByUrl(String url);
	@Query(value = "SELECT URL FROM RESPONSEDTO r WHERE r.URL = :url", 
	  nativeQuery = true)
	ResponseDTO yy(
			  @Param("url") String url);
}
