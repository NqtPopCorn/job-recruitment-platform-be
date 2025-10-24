package com.popcorn.jrp.helper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Listing string like "abc, xyz, ..."
 * @author truong
 */
@Converter(autoApply = true)
public class ListStringConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> values) {
        if(values == null || values.isEmpty()) return "";
        return String.join(", ", values);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if(dbData == null || dbData.isEmpty()) return new ArrayList<>();
        return Arrays.stream(dbData.split(", ")).map(String::trim).toList();
    }
}
