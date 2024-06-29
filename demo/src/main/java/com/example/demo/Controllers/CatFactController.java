package com.example.demo.Controllers;

import com.example.demo.Entities.CatFact;
import com.example.demo.Repositories.CatFactRepository;
import com.example.demo.Services.CatFactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class CatFactController {
    @Autowired
    private CatFactRepository catFactRepository;
    @Autowired

    private final CatFactService catFactService;

    @Autowired
    public CatFactController(CatFactService catFactService) {
        this.catFactService = catFactService;
    }


    @GetMapping("/catfact")
    public String getCatFact() throws IOException {
        return catFactService.getCatFact();
    }

    @PostMapping("/post")
    public String postCatFact(@RequestBody CatFact catFact) throws IOException {
        return catFactService.postCatFact(catFact);
    }
    @PostMapping("/post-to-database")
    public String postCatFactToDatabase(@RequestBody CatFact catFact){
        catFactRepository.save(catFact);
        return "Successfully posted to the database!";
    }
    @GetMapping("catfact-2")
    public ResponseEntity<CatFact> getCatFact2(){
        return catFactService.getCatFactSecondMethod();
    }
}

