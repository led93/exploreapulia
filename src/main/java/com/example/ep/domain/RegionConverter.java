package com.example.ep.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RegionConverter implements AttributeConverter<Region, String> {
    @Override
    public String convertToDatabaseColumn(Region attribute) {
        return attribute.getLabel();
    }

    @Override
    public Region convertToEntityAttribute(String dbData) {
        return Region.findByLabel(dbData);
    }
}
