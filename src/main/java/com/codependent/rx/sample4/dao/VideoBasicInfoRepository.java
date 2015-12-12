package com.codependent.rx.sample4.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.codependent.rx.sample4.dto.VideoBasicInfo;

@Repository
public interface VideoBasicInfoRepository extends JpaRepository<VideoBasicInfo, Integer>{

}
