package com.webScraping.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.webScraping.demo.model.ResponseDTO;
import com.webScraping.demo.repositories.ResponseDTORepository;
import com.webScraping.demo.service.ScraperService;

import java.util.Set;


@CrossOrigin(origins = "http://localhost:3000/")
@RestController
@RequestMapping(path = "/")
public class ScraperController {

	@Autowired
    ScraperService scraperService;
	@Autowired
	ResponseDTORepository re;

    @GetMapping(path = "/{vehicleModel}")
    public Set<ResponseDTO> getVehicleByModel(@PathVariable String vehicleModel) {
        return  scraperService.getVehicleByModel(vehicleModel);
    }
}
