package com.redfin.interview.service;


import com.redfin.interview.entity.FoodTruck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public class FoodTruckService {

  private FoodTruckRepository foodTruckRepository;


  public FoodTruckService(FoodTruckRepository foodTruckRepository) {
    this.foodTruckRepository = foodTruckRepository;
  }

  public Page<FoodTruck> getOpenFoodTruckAtCurrentTime(Pageable pageable) {
    return foodTruckRepository.findAllPage(pageable);
  }

}