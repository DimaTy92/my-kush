package com.example.mykush;

import com.example.mykush.entity.dto.DeviceControlDTO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Objects;

@Service
public class ParserJabko {
    private static final String MODEL_SELECTOR = "#allproduct > div.wrapp-product__row > div.wrapp-product__row-img > div.mob-info > h1";
    private static final String PRICE_UA_SELECTOR = "#allproduct > div.wrapp-product__row > div.wrapp-product__row-info" +
            " > div.product-info__price > div > span.price-new__uah";
    private static final String PRICE_USA_SELECTOR = "#allproduct > div.wrapp-product__row > div.wrapp-product__row-info > div.product-info__price " +
            "> div > span.price-new__usd";
    private static final String MEMORY_SELECTOR = "#allproduct > div.wrapp-product__row > div.wrapp-product__row-info > div.product-info__option " +
            "> div.ourl.grid-2 > div.product-info__choice-memory.line_o.sorder_ > div > a.activ";

    public DeviceControlDTO getDeviceInfo(String url) {

        Document jabkoDocument = null;
        try {
            jabkoDocument = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (Objects.isNull(jabkoDocument)) {
            throw new RuntimeException("Unable to parse the page");
        }

        String elementPriceUA = jabkoDocument.select(PRICE_UA_SELECTOR).text();
        String elementPriceUSA = jabkoDocument.select(PRICE_USA_SELECTOR).text();
        String elementModel = jabkoDocument.select(MODEL_SELECTOR).text();


        return DeviceControlDTO.builder()
                .model(elementModel)
                .priceUA(elementPriceUA)
                .priceUSA(elementPriceUSA)
                .url(url)
                .lastCheckDate(LocalDate.now())
                .build();
    }
}
