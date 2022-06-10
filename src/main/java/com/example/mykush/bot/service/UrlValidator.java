package com.example.mykush.bot.service;

import com.example.mykush.ParserJabko;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Locale;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UrlValidator {
    private final ParserJabko parserJabko;

    public boolean isUrlValid(String url) {
        url = url.trim().toLowerCase(Locale.ROOT);
        return org.apache.commons.validator.routines.UrlValidator.getInstance().isValid(url)
                && !Objects.isNull(parserJabko.getDeviceInfo(url));
    }
}