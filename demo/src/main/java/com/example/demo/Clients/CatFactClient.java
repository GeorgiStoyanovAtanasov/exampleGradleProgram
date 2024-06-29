package com.example.demo.Clients;

import com.example.demo.Entities.CatFact;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.GetMapping;

@ComponentScan
@FeignClient(name = "catfactservice", url = "https://catfact.ninja")
public interface CatFactClient {
    @GetMapping("/fact")
    CatFact getCatFact();
}