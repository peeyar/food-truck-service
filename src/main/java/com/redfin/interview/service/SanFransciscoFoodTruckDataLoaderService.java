package com.redfin.interview.service;

import com.redfin.interview.com.redfin.interview.entity.FoodTruck;
import com.redfin.interview.com.redfin.interview.entity.FoodTruckRepository;
import com.redfin.interview.com.redfin.interview.entity.SanFransiscoFoodTruckInfo;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

/**
 * Created by VijaySidhu on 7/21/2019.
 */
@Configuration
@EnableScheduling
public class SanFransciscoFoodTruckDataLoaderService implements InitializingBean {


  private final RestTemplate restTemplate;

  private final String sfoUri;

  @Autowired
  private FoodTruckRepository foodTruckRepository;

  public SanFransciscoFoodTruckDataLoaderService(RestTemplate restTemplate,
      @Value("${sfo.uri}") String sfoUri) {
    this.restTemplate = restTemplate;
    this.sfoUri = sfoUri;
  }

  private static List<FoodTruck> mapToFoodTruckEntity(
      List<SanFransiscoFoodTruckInfo> foodTruckEntities) {
    List<FoodTruck> foodTruckReponses = foodTruckEntities.stream()
        .filter(p->p.isOpen())
        .collect(
            Collectors.mapping(
                p -> new FoodTruck(p.getTruckName(), p.getAddress()),
                Collectors.toList()));
    return foodTruckReponses;
  }

  @Scheduled(cron="0 0 * ? * *")
  public List<SanFransiscoFoodTruckInfo> loadFoodTruckInfo() {
    ResponseEntity<SanFransiscoFoodTruckInfo[]> sfoFoodTrucks = getSFOData();
    return Arrays.asList(sfoFoodTrucks.getBody());

  }

  private ResponseEntity<SanFransiscoFoodTruckInfo[]> getSFOData() {
    ResponseEntity<SanFransiscoFoodTruckInfo[]> sfoFoodTrucks = restTemplate
        .getForEntity(sfoUri, SanFransiscoFoodTruckInfo[].class);
    if (foodTruckRepository.count() >= 1L) {
      foodTruckRepository.deleteAll();
    }
    foodTruckRepository.saveAll(mapToFoodTruckEntity(Arrays.asList(sfoFoodTrucks.getBody())));
    return sfoFoodTrucks;
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    getSFOData();
  }
}
