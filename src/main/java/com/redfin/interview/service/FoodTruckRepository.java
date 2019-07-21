package com.redfin.interview.service;

import java.util.List;

import com.redfin.interview.entity.FoodTruck;
import com.redfin.interview.sfo.data.loader.service.SanFransiscoFoodTruckInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface FoodTruckRepository extends CrudRepository<FoodTruck, Long> {

  @Query("select c from FoodTruck c ")
  Page<FoodTruck> findAllPage(Pageable pageable);

  void save(List<SanFransiscoFoodTruckInfo> sanFransiscoFoodTruckInfos);
}
