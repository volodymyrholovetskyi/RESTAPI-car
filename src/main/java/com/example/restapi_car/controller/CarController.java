package com.example.restapi_car.controller;


import com.example.restapi_car.domain.Car;
import com.example.restapi_car.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/cars")
public class CarController {

    private CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()//produces = {
//        MediaType.APPLICATION_XML_VALUE,
//        MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<CollectionModel<Car>> getAllCar() {
        return carService.getAllCar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Car>> getCarById(@PathVariable int id) {
        return carService.getCarById(id);
    }

    @GetMapping("colors/{color}")
    public ResponseEntity<EntityModel<Car>> getCarByColor(@PathVariable String color) {
        return carService.getCarByColor(color);
    }

    @PutMapping()
    public ResponseEntity putCar(@RequestBody Car car) {
        return carService.putCar(car);
    }

    @PatchMapping
    public ResponseEntity patchCar(@RequestBody Car car) {
        return carService.patchCar(car);
    }

    @DeleteMapping("remove/{id}")
    public ResponseEntity removeCar(@PathVariable int id) {
        return carService.deleteCarById(id);
    }
}

