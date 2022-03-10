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

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

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
    @GetMapping(path = "/h")
    public ResponseDTO indByTitle() {
    	//
        return  re.findByTitle("Samsung Galaxy A52 (128GB, 8GB) 6.5\\\" AMOLED 90Hz, 4500mAh Battery, Dual SIM GSM Unlocked (T-Mobile, AT&T, Metro) 4G Volte International Model - A525F/DS (Fast Car Charger Bundle, Awesome Violet)");
    }
    
    @GetMapping(path = "/hh")
    public ResponseDTO h() {
    	
        return  re.findByUrl("https://www.amazon.com/gp/slredirect/picassoRedirect.html/ref=pa_sp_btf_electronics_sr_pg1_1?ie=UTF8&adId=A05302743M0EVD7USRC4C&url=%2FELLYNOV-Shockproof-Compatible-Samsumg-Military%2Fdp%2FB093TJ6VFL%2Fref%3Dsr_1_29_sspa%3Fkeywords%3Dsamsung%2Ba52%26qid%3D1646940361%26s%3Delectronics%26sr%3D1-29-spons%26psc%3D1%26smid%3DA1TYHRZUK891B0&qualifier=1646940361&id=6521149370738497&widgetName=sp_btf");
    }
}
