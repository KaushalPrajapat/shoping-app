package errorcheck.error.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import errorcheck.error.service.ErrorService;

@RestController
public class BaseController {
    @Autowired
    private ErrorService service;

    @GetMapping("/{prodId}")
    public ResponseEntity<?> getProduct(@PathVariable long prodId){
        return ResponseEntity.ok(service.getProductOrError(prodId));
    }
}
