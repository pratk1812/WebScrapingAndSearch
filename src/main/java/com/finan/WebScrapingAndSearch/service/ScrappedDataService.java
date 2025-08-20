package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import com.finan.WebScrapingAndSearch.persistence.mapper.ScrappedDataMapper;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(rollbackFor = Exception.class)
public class ScrappedDataService {
  private static final Logger LOGGER = LogManager.getLogger(ScrappedDataService.class);
  private final ScrappedDataRepository scrappedDataRepository;
  private final ScrappedDataMapper scrappedDataMapper;

  @Autowired
  public ScrappedDataService(
      ScrappedDataRepository scrappedDataRepository, ScrappedDataMapper scrappedDataMapper) {
    this.scrappedDataRepository = scrappedDataRepository;
    this.scrappedDataMapper = scrappedDataMapper;
  }

  public void save(List<ScrappedDataDTO> scrappedDataDTOS) {
    LOGGER.info("saving scraped data");
    List<ScrappedDataEntity> entities =
        scrappedDataDTOS.stream().map(scrappedDataMapper::toEntity).toList();
    scrappedDataRepository.saveAll(entities);
  }

  public List<ScrappedDataDTO> fetch() {
    LOGGER.info("fetching all scraped data");
    return scrappedDataRepository.findAll().stream().map(scrappedDataMapper::toDto).toList();
  }

  public List<ScrappedDataDTO> fetch(String jobId) {
    LOGGER.info("fetching scraped data for {}", jobId);
    return scrappedDataRepository.findByJobId(jobId).stream()
        .map(scrappedDataMapper::toDto)
        .toList();
  }

  public void upsertAll(List<ScrappedDataDTO> scrappedDataDTOS) {
    LOGGER.info("Executing upsert operation");

    List<String> urls = scrappedDataDTOS.stream().map(ScrappedDataDTO::getUrl).distinct().toList();

    List<ScrappedDataEntity> existing = scrappedDataRepository.findByUrlIn(urls);

    Map<String, ScrappedDataEntity> existingMap =
        existing.stream()
            .collect(Collectors.toMap(ScrappedDataEntity::getUrl, Function.identity()));

    List<ScrappedDataEntity> toSave = new ArrayList<>(scrappedDataDTOS.size());
    for (ScrappedDataDTO dto : scrappedDataDTOS) {
      ScrappedDataEntity entity = existingMap.getOrDefault(dto.getUrl(), new ScrappedDataEntity());
      entity.setUrl(dto.getUrl());
      entity.setJobId(dto.getJobId());
      entity.setData(dto.getData());
      entity.setTimeStamp(dto.getTimeStamp());
      entity.getKeyWords().addAll(dto.getKeyWords());
      toSave.add(entity);
    }
    scrappedDataRepository.saveAll(toSave);
  }
}
