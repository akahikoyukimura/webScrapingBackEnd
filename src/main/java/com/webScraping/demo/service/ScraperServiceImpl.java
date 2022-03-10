package com.webScraping.demo.service;

import java.io.IOException;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.repository.query.parser.Part.IgnoreCaseType;
import org.springframework.stereotype.Service;

import com.webScraping.demo.model.RequestDTO;
import com.webScraping.demo.model.ResponseDTO;
import com.webScraping.demo.repositories.RequestDTORepository;
import com.webScraping.demo.repositories.ResponseDTORepository;

@Service
public class ScraperServiceImpl implements ScraperService {

    @Value("#{'${website.urls}'.split(',')}")
    List<String> urls;
    
    @Autowired
    private RequestDTORepository requestDTORepository;
    @Autowired
    private ResponseDTORepository responseDTORepository;
    
    
    public static boolean containsAllWords(String word, String ...keywords) {
        for (String k : keywords)
            if (!word.toLowerCase().contains(k.toLowerCase())) return false;
        return true;
    }

    @Override
    public Set<ResponseDTO> getVehicleByModel(String vehicleModel) {
    	

    	/*if (requestDTORepository.findByKeyWord(vehicleModel)!=null) {
			return responseDTORepository.findByRequest(requestDTORepository.findByKeyWord(vehicleModel));
		}*/
    	
        Set<ResponseDTO> responseDTOS = new HashSet<>();
        /*RequestDTO request=null;
        
        if(requestDTORepository.findByKeyWord(vehicleModel)==null) {
        	
        request=new RequestDTO(vehicleModel);
        requestDTORepository.save(request);
        }*/

        
        for (String url: urls) {

            if (url.contains("ikman")) {

                //extractDataFromIkman(responseDTOS, url + vehicleModel,request);
            } else if (url.contains("riyasewana")) {
                //extractDataFromRiyasewana(responseDTOS, url + vehicleModel,request);
            }else if(url.contains("amazon")) {
            	
            	if (vehicleModel.contains("&")) {
            		
            		String[] parts = vehicleModel.split("&");
            		String part1 = parts[0];
            		String part2 = parts[1];
            		
            		if (vehicleModel.contains(" ")) {
            			extractDataFromAmazon(responseDTOS,url + part1.replaceAll(" ", "+")+"&i="+part2+"-intl-ship", vehicleModel);
					}else
						extractDataFromAmazon(responseDTOS, url + part1+"&i="+part2+"-intl-ship", vehicleModel);
				}
            	else if(vehicleModel.contains(" "))
            		extractDataFromAmazon(responseDTOS, url + vehicleModel.replaceAll(" ", "+"), vehicleModel);
            	else
            		extractDataFromAmazon(responseDTOS, url + vehicleModel, vehicleModel);
            }

        }

        
        return responseDTOS;
    
    	}
    
    
    
    private void extractDataFromAmazon(Set<ResponseDTO> responseDTOS, String url,String vehicleModel) {


        
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
            if (vehicleModel.contains("&")) {
            	
            	String[] parts = vehicleModel.split("&");
        		vehicleModel = parts[0];
        		
        		if(vehicleModel.contains(" "))
        			elements=document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-expand-height s-include-content-margin s-latency-cf-section s-card-border");
        		else
                 elements = document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-include-content-margin s-latency-cf-section s-card-border");
            }
            else {
                 elements = document.getElementsByClass("s-card-container s-overflow-hidden aok-relative s-include-content-margin s-latency-cf-section s-card-border");
            }
            
            for (Element ads: elements) {
                    ResponseDTO responseDTO = new ResponseDTO();
                    
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
                        responseDTO.setStore("Amazon");
                        
                    }
                    if (responseDTO.getUrl() != null && responseDTO.getPrice()!=null) {
                    	/*if(responseDTORepository.findByUrl(responseDTO.getUrl())!=null)
                        {	
                        }else
                        {responseDTO.setRequest(request);
                        }*/
                    	String s=responseDTO.getTitle();
                    	String p=responseDTO.getPrice();
                    	ResponseDTO r=responseDTORepository.findByTitle(s);
                    			
                    	System.out.println(r);
                    	if(r==null) {
                    		System.out.println("farst");
                    		responseDTOS.add(responseDTO);
                    		responseDTORepository.save(responseDTO);}
                    	else if(!r.getPrice().equals(p)) {
                    		System.out.println("seconde");
                    		r.setPrice(responseDTO.getPrice());
                    		responseDTOS.add(r);
                    		}
                    	else
                    		responseDTOS.add(r);
                    }
                    
                }

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    

    private void extractDataFromRiyasewana(Set<ResponseDTO> responseDTOS, String url,RequestDTO request) {

        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementById("content");

            Elements elements = element.getElementsByTag("a");

            for (Element ads: elements) {
                ResponseDTO responseDTO = new ResponseDTO();

                if (!StringUtils.isEmpty(ads.attr("title")) ) {
                    responseDTO.setTitle(ads.attr("title"));
                    responseDTO.setUrl(ads.attr("href"));
                    //responseDTO.setRequest(request);
                }
                if (responseDTO.getUrl() != null) {
                	responseDTOS.add(responseDTO);
                	responseDTORepository.save(responseDTO);
                	//request.getResponses().add(responseDTO);
                }
                
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void extractDataFromIkman(Set<ResponseDTO> responseDTOS, String url,RequestDTO request) {
        try {
            Document document = Jsoup.connect(url).get();
            Element element = document.getElementsByClass("list--3NxGO").first();

            Elements elements = element.getElementsByTag("a");

            for (Element ads: elements) {

                ResponseDTO responseDTO = new ResponseDTO();

                if (StringUtils.isNotEmpty(ads.attr("href"))) {
                    responseDTO.setTitle(ads.attr("title"));
                    responseDTO.setUrl("https://ikman.lk"+ ads.attr("href"));
                    //responseDTO.setRequest(request);
                }
                if (responseDTO.getUrl() != null) {
                	responseDTOS.add(responseDTO);
                	responseDTORepository.save(responseDTO);
                	//request.getResponses().add(responseDTO);
                }

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
