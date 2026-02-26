package com.projeto.javali;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/teste")
    public String index() {
        return "O sistema Javali está online e respondendo!";
    }
}