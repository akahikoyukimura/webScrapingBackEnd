package com.webScraping.demo.service;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.webScraping.demo.model.RequestDTO;
import com.webScraping.demo.model.ResponseDTO;
import com.webScraping.demo.repositories.RequestDTORepository;
import com.webScraping.demo.repositories.ResponseDTORepository;

public class HandleData {
	
	ResponseDTORepository responseDTORepository;
	RequestDTORepository requestDTORepository;
	
	public static boolean containsAllWords(String word, String ...keywords) {
        for (String k : keywords)
            if (!word.toLowerCase().contains(k.toLowerCase())) return false;
        return true;
    }

private void extractDataFromEbay(Set<ResponseDTO> responseDTOS, String url,String vehicleModel) {


        
        if(requestDTORepository.findByKeyWord(vehicleModel)==null) {
        RequestDTO request=null;	
        request=new RequestDTO(vehicleModel);
        requestDTORepository.save(request);
        }
        try {
            Document document = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
                    .get();

            System.out.println(url);
            Elements elements;
            if (vehicleModel.contains(" ")) {
            	
            	elements = document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-include-content-margin s-latency-cf-section s-card-border");
            }
            else {
                 elements = document.getElementsByClass("s-item s-item__pl-on-bottom");
            }
            
            for (Element ads: elements) {
                    ResponseDTO responseDTO = new ResponseDTO();
                    
                    if (!StringUtils.isEmpty(ads.child(0).text()) 
                    		&& containsAllWords(ads.child(0).text(), vehicleModel.split(" "))) {
                    	
                        responseDTO.setTitle(ads
                        		.getElementsByClass("s-item__title")
                        		.first().child(0).text());
                        responseDTO.setImage(ads.getElementsByTag("img").attr("src"));
                        
                        if(!StringUtils.isEmpty(ads.getElementsByClass("s-item__price").html())
                        		&& !StringUtils.isEmpty(ads.getElementsByClass("s-item__detail s-item__detail--primary")
                        				.first()
                        				.getElementsByClass("s-item__price").html())) {
                        	
                        responseDTO.setPrice((ads.getElementsByClass("s-item__detail s-item__detail--primary")
                        		.first().getElementsByClass("s-item__price").html().replaceAll("[$,]", "")));
                        }
                        responseDTO.setUrl(
                        		ads
                        		.getElementsByClass("s-item__link")
                        		.attr("href"));
                        responseDTO.setStore("Ebay");
                        
                    }
                    if (responseDTO.getUrl() != null && responseDTO.getPrice()!=null) {
                    	responseDTOS.add(responseDTO);
                    }
                    
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
