package com.webScraping.demo.service;

import java.io.IOException;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;

import com.webScraping.demo.model.RequestDTO;
import com.webScraping.demo.model.ResponseDTO;
import com.webScraping.demo.repositories.RequestDTORepository;
import com.webScraping.demo.repositories.ResponseDTORepository;

public class DataHandling {
	@Autowired
    private RequestDTORepository requestDTORepository;
    @Autowired
    private ResponseDTORepository responseDTORepository;
    
    public static boolean containsAllWords(String word, String ...keywords) {
        for (String k : keywords)
            if (!word.toLowerCase().contains(k.toLowerCase())) return false;
        return true;
    }
	
	private void extractDataFromAmazon(Set<ResponseDTO> responseDTOS, String url,RequestDTO request,String vehicleModel) {

        try {
            //Document document = Jsoup.connect(url).get();
            Document document = Jsoup
                    .connect(url)
                    .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/40.0.2214.38 Safari/537.36")
                    .get();
            
            //Element element = document.getElementsByClass("a-section").first();

            System.out.println(url);
            
            Elements elements;
            
            if (vehicleModel.contains("&")) {
            	
            	String[] parts = vehicleModel.split("&");
        		vehicleModel = parts[0];
        		
                 elements = document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-expand-height s-include-content-margin s-latency-cf-section s-card-border");
                //System.out.println(elements);
            }
            else {
                 elements = document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-include-content-margin s-latency-cf-section s-card-border");

            }
                for (Element ads: elements) {
                    ResponseDTO responseDTO = new ResponseDTO();
                    
                    //System.out.println(ads.child(0).text());
                    
                    if (!StringUtils.isEmpty(ads.child(0).text()) 
                    		&& containsAllWords(ads.child(0).text(), vehicleModel.split(" "))) {
                    	
                        responseDTO.setTitle(ads
                        		.getElementsByClass("a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal")
                        		.first().child(0).text());
                        responseDTO.setImage(ads.getElementsByTag("img").attr("src"));
                        
                        if(!StringUtils.isEmpty(ads.getElementsByClass("a-offscreen").html())
                        		&& !StringUtils.isEmpty(ads.getElementsByClass("a-price").first().getElementsByClass("a-offscreen").html())) {
                        	
                        responseDTO.setPrice((ads.getElementsByClass("a-price").first().getElementsByClass("a-offscreen").html().replaceAll("[$,]", "")));
                        }
                        responseDTO.setUrl(
                        		"https://www.amazon.com"+ads
                        		.getElementsByClass("a-link-normal s-underline-text s-underline-link-text s-link-style a-text-normal")
                        		.attr("href"));
                        //responseDTO.setRequest(request);
                    }
                    if (responseDTO.getUrl() != null && responseDTO.getPrice()!=null) {
                    	responseDTOS.add(responseDTO);
                    	if(responseDTORepository.yy(responseDTO.getUrl())==null)
                    		responseDTORepository.save(responseDTO);
                    	else if(responseDTORepository.yy(responseDTO.getUrl()).getPrice()!=responseDTO.getPrice())
                    		responseDTORepository.yy(responseDTO.getUrl()).setPrice(responseDTO.getPrice());
                    }
                    
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
	
	
}
