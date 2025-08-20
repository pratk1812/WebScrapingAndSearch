package com.finan.WebScrapingAndSearch.persistence.repository;

import com.finan.WebScrapingAndSearch.persistence.enitity.ScrappedDataEntity;

import java.util.Collection;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScrappedDataRepository extends JpaRepository<ScrappedDataEntity, Long> {
  List<ScrappedDataEntity> findByJobId(String jobId);

  List<ScrappedDataEntity> findByUrlIn(Collection<String> urls);
}
