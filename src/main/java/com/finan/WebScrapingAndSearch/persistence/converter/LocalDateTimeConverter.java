package com.finan.WebScrapingAndSearch.persistence.converter;

import jakarta.persistence.Converter;
import jakarta.persistence.AttributeConverter;

import java.sql.Date;
import java.time.LocalDateTime;

@Converter(autoApply = true)
public class LocalDateConverter implements AttributeConverter<LocalDateTime, Date> {

  @Override
  public Date convertToDatabaseColumn(LocalDateTime localDateTime) {
    return localDateTime.;
  }

  @Override
  public LocalDateTime convertToEntityAttribute(Date date) {
    return null;
  }
}
