package com.example.demo.Repositories;

import com.example.demo.Entities.CatFact;
import org.springframework.data.repository.CrudRepository;

public interface CatFactRepository extends CrudRepository<CatFact, Long> {
}

