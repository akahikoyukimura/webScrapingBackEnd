package com.webScraping.demo.repositories;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webScraping.demo.model.RequestDTO;
import com.webScraping.demo.model.ResponseDTO;

public interface ResponseDTORepository extends JpaRepository<ResponseDTO,Long>{

	Set<ResponseDTO> findByRequest(RequestDTO request);
}
