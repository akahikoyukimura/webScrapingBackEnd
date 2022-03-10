package com.webScraping.demo.service;

import java.util.Set;

import org.jsoup.select.Elements;

import com.webScraping.demo.model.ResponseDTO;

public interface ScraperService {

	Set<ResponseDTO> getVehicleByModel(String vehicleModel);
	
	
}
