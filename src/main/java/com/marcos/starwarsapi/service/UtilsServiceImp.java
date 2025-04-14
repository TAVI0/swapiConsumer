package com.marcos.starwarsapi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class UtilsServiceImp implements UtilsService{
    @Value("${swapi-url}")
    private String baseUrl;

    @Value("${base-url}")
    private String apiUrl;
    @Override
    public String transformUrl(String url) {
        if (url != null && url.startsWith(baseUrl)) {
            return apiUrl + url.substring(baseUrl.length());
        }
        return url;
    }

    @Override
    public List<String> transformUrls(List<String> urls) {
        if(urls==null){
            return null;
        }
        List<String> newList = new ArrayList<>();
        for (String url : urls ){
            newList.add(transformUrl(url));
        }
        return newList;
    }
}
