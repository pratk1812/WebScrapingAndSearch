package com.finan.WebScrapingAndSearch.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import com.finan.WebScrapingAndSearch.persistence.mapper.ScrappedDataMapper;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ScrappedDataServiceTest {

  @Mock private ScrappedDataRepository scrappedDataRepository;
  @Mock private ScrappedDataMapper scrappedDataMapper;
  @InjectMocks private ScrappedDataService scrappedDataService;
  @Captor private ArgumentCaptor<List<ScrappedDataEntity>> listCaptor;

  @Test
  void testSave_shouldMapAndPersistEntities() {
    ScrappedDataDTO dto = new ScrappedDataDTO();
    ScrappedDataEntity entity = new ScrappedDataEntity();

    when(scrappedDataRepository.saveAll(anyList())).thenReturn(List.of(entity));
    when(scrappedDataMapper.toEntity(any())).thenReturn(entity);

    scrappedDataService.save(List.of(dto));

    verify(scrappedDataMapper, times(1)).toEntity(any(ScrappedDataDTO.class));
    verify(scrappedDataRepository, times(1)).saveAll(anyList());
  }

  @Test
  void testFetchAll_shouldReturnMappedDTOs() {
    ScrappedDataEntity entity = new ScrappedDataEntity();
    ScrappedDataDTO dto = new ScrappedDataDTO();

    when(scrappedDataRepository.findAll()).thenReturn(List.of(entity));
    when(scrappedDataMapper.toDto(entity)).thenReturn(dto);

    List<ScrappedDataDTO> result = scrappedDataService.fetch();

    assertEquals(1, result.size());
    assertEquals(dto, result.get(0));
  }

  @Test
  void testFetchByJobId_shouldReturnMappedDTOs() {
    ScrappedDataEntity entity = new ScrappedDataEntity();
    ScrappedDataDTO dto = new ScrappedDataDTO();

    when(scrappedDataRepository.findByJobId(anyString())).thenReturn(List.of(entity));
    when(scrappedDataMapper.toDto(entity)).thenReturn(dto);

    List<ScrappedDataDTO> result = scrappedDataService.fetch("a");

    assertEquals(1, result.size());
    assertEquals(dto, result.get(0));
  }

  @Test
  void upsertAll_withEmptyList_callsRepoWithEmptyLists() {
    // arrange
    List<ScrappedDataDTO> dtos = Collections.emptyList();
    when(scrappedDataRepository.findByUrlIn(Collections.emptyList()))
        .thenReturn(Collections.emptyList());

    // act
    scrappedDataService.upsertAll(dtos);

    // assert
    verify(scrappedDataRepository).findByUrlIn(Collections.emptyList());
    verify(scrappedDataRepository).saveAll(listCaptor.capture());
    assertTrue(
        listCaptor.getValue().isEmpty(),
        "saveAll should be called with an empty list when input is empty");
  }

  @Test
  void upsertAll_withAllNewDtos_createsEntitiesFromScratch() {
    // arrange
    LocalDateTime now = LocalDateTime.now();
    ScrappedDataDTO dto1 = new ScrappedDataDTO();
    dto1.setUrl("u1");
    dto1.setJobId("j1");
    dto1.setData("d1");
    dto1.setTimeStamp(now);
    dto1.getKeyWords().add("k1");

    ScrappedDataDTO dto2 = new ScrappedDataDTO();
    dto2.setUrl("u2");
    dto2.setJobId("j2");
    dto2.setData("d2");
    dto2.setTimeStamp(now.plusHours(1));
    dto2.getKeyWords().add("k2");

    List<ScrappedDataDTO> dtos = List.of(dto1, dto2);
    // no existing
    when(scrappedDataRepository.findByUrlIn(List.of("u1", "u2"))).thenReturn(List.of());

    // act
    scrappedDataService.upsertAll(dtos);

    // assert findByUrlIn called with distinct URLs
    verify(scrappedDataRepository).findByUrlIn(List.of("u1", "u2"));

    // capture saveAll
    verify(scrappedDataRepository).saveAll(listCaptor.capture());
    List<ScrappedDataEntity> saved = listCaptor.getValue();
    assertEquals(2, saved.size(), "Should create one entity per DTO");

    // check first
    ScrappedDataEntity e1 = saved.get(0);
    assertEquals("u1", e1.getUrl());
    assertEquals("j1", e1.getJobId());
    assertEquals("d1", e1.getData());
    assertEquals(now, e1.getTimeStamp());
    assertTrue(e1.getKeyWords().contains("k1"));

    // check second
    ScrappedDataEntity e2 = saved.get(1);
    assertEquals("u2", e2.getUrl());
    assertEquals("j2", e2.getJobId());
    assertEquals("d2", e2.getData());
    assertEquals(now.plusHours(1), e2.getTimeStamp());
    assertTrue(e2.getKeyWords().contains("k2"));
  }

  @Test
  void upsertAll_withSomeExisting_updatesAndMergesKeywords() {
    // arrange
    LocalDateTime oldTime = LocalDateTime.of(2025, 1, 1, 0, 0);
    ScrappedDataEntity existing = new ScrappedDataEntity();
    existing.setUrl("uX");
    existing.setJobId("oldJob");
    existing.setData("oldData");
    existing.setTimeStamp(oldTime);
    existing.getKeyWords().add("oldKey");

    when(scrappedDataRepository.findByUrlIn(List.of("uX"))).thenReturn(List.of(existing));

    ScrappedDataDTO dto = new ScrappedDataDTO();
    dto.setUrl("uX");
    dto.setJobId("newJob");
    dto.setData("newData");
    LocalDateTime newTime = LocalDateTime.of(2025, 2, 2, 2, 2);
    dto.setTimeStamp(newTime);
    dto.getKeyWords().addAll(List.of("newKey1", "newKey2"));

    // act
    scrappedDataService.upsertAll(List.of(dto));

    // assert only one find
    verify(scrappedDataRepository).findByUrlIn(List.of("uX"));

    // capture
    verify(scrappedDataRepository).saveAll(listCaptor.capture());
    List<ScrappedDataEntity> saved = listCaptor.getValue();
    assertEquals(1, saved.size());

    ScrappedDataEntity merged = saved.get(0);
    // same instance should be updated
    assertSame(existing, merged);

    // url always overwritten (same)
    assertEquals("uX", merged.getUrl());
    // jobId updated
    assertEquals("newJob", merged.getJobId());
    // data updated
    assertEquals("newData", merged.getData());
    // timestamp updated
    assertEquals(newTime, merged.getTimeStamp());

    // keywords merged
    assertTrue(merged.getKeyWords().contains("oldKey"));
    assertTrue(merged.getKeyWords().contains("newKey1"));
    assertTrue(merged.getKeyWords().contains("newKey2"));
  }

  @Test
  void upsertAll_withDuplicateUrls_onlyQueriesDistinctAndCreatesPerDto() {
    // arrange
    ScrappedDataDTO dto1 = new ScrappedDataDTO();
    dto1.setUrl("dup");
    dto1.setJobId("jA");
    dto1.setData("dA");
    dto1.setTimeStamp(LocalDateTime.now());
    dto1.getKeyWords().add("kA");

    ScrappedDataDTO dto2 = new ScrappedDataDTO();
    dto2.setUrl("dup");
    dto2.setJobId("jB");
    dto2.setData("dB");
    dto2.setTimeStamp(LocalDateTime.now().plusMinutes(5));
    dto2.getKeyWords().add("kB");

    List<ScrappedDataDTO> dtos = List.of(dto1, dto2);

    // no existing for either
    when(scrappedDataRepository.findByUrlIn(List.of("dup"))).thenReturn(List.of());

    // act
    scrappedDataService.upsertAll(dtos);

    // only one distinct URL queried
    verify(scrappedDataRepository).findByUrlIn(List.of("dup"));

    // two saves (one per dto)
    verify(scrappedDataRepository).saveAll(listCaptor.capture());
    List<ScrappedDataEntity> saved = listCaptor.getValue();
    assertEquals(2, saved.size());

    // ensure each entity reflects its own dto
    assertEquals("jA", saved.get(0).getJobId());
    assertEquals("dA", saved.get(0).getData());
    assertTrue(saved.get(0).getKeyWords().contains("kA"));

    assertEquals("jB", saved.get(1).getJobId());
    assertEquals("dB", saved.get(1).getData());
    assertTrue(saved.get(1).getKeyWords().contains("kB"));
  }
}
