package com.finan.WebScrapingAndSearch.service;

import com.finan.WebScrapingAndSearch.model.dto.ScrappedDataDTO;
import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;
import com.finan.WebScrapingAndSearch.persistence.mapper.ScrappedDataMapper;
import com.finan.WebScrapingAndSearch.persistence.repository.ScrappedDataRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScrappedDataServiceTest {

    @Mock
    private ScrappedDataRepository scrappedDataRepository;
    @Mock
    private ScrappedDataMapper scrappedDataMapper;
    @InjectMocks
    private ScrappedDataService scrappedDataService;

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
}
