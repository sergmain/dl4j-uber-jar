package com.xxx;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SimpleRestController {

    private final ModelService modelService;

    public SimpleRestController(ModelService modelService) {
        this.modelService = modelService;
    }

    private static final String csv = "2778.0,0.0,0.0,994.0,0.0,2899.0,1429.0,1863.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
    private static final String csv1 = "875.0,0.0,994.0,0.0,153.0,0.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";
    private static final String csv2 = "469.0,0.0,1561.0,834.0,2506.0,89.0,1113.0,0.0,0.0,2288.0,0.0,0.0,0.0,0.0,2499.0,1476.0,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,";


    @GetMapping("/api")
    public String rest() {
        modelService.predict(csv2);
        return "Hello! ";
    }
}
