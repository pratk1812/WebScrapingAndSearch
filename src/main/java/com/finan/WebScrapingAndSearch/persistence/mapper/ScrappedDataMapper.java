package com.finan.WebScrapingAndSearch.persistence.mapper;

import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(
    unmappedTargetPolicy = ReportingPolicy.IGNORE,
    componentModel = MappingConstants.ComponentModel.SPRING)
public interface ScrappedDataMapper {
  ScrappedDataEntity toEntity(ScrappedDataDTO scrappedDataDTO);

  ScrappedDataDTO toDto(ScrappedDataEntity scrappedDataEntity);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  ScrappedDataEntity partialUpdate(
      ScrappedDataDTO scrappedDataDTO, @MappingTarget ScrappedDataEntity scrappedDataEntity);
}
