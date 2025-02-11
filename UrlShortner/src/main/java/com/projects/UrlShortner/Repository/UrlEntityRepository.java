package com.projects.UrlShortner.Repository;

import com.projects.UrlShortner.Model.UrlEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UrlEntityRepository extends JpaRepository<UrlEntity,String> {
}
