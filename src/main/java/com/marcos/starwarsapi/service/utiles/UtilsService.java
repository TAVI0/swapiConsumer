package com.marcos.starwarsapi.service.utiles;

import java.util.List;

public interface UtilsService {
    String transformUrl(String url);
    List<String> transformUrls(List<String> urls);
}
