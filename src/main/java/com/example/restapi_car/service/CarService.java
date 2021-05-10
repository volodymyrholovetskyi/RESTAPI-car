package com.example.restapi_car.service;

import com.example.restapi_car.controller.CarController;
import com.example.restapi_car.domain.Car;
import com.example.restapi_car.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Service
public class CarService {

    private CarRepository carRepository;

    @Autowired
    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public ResponseEntity<CollectionModel<Car>> getAllCar() {
        List<Car> allList = carRepository.getAllList();
        allList.forEach(car -> car.add(linkTo(CarController.class).slash(car.getId()).withSelfRel()));
        Link link = linkTo(CarController.class).withSelfRel();
        CollectionModel<Car> collectionModel = new CollectionModel<Car>(allList, link);
        return new ResponseEntity<>( collectionModel, HttpStatus.OK);
    }

    public ResponseEntity<EntityModel<Car>> getCarById(int id) {
        Link link = linkTo(CarController.class).withSelfRel();
        List<Car> allCar = carRepository.getAllList();
        Optional<Car> first = allCar.stream().filter(car1 -> car1.getId() == id).findFirst();
        if (first.isPresent()) {
            EntityModel<Car> carEntityModel =
                    new EntityModel<Car>(first.get(), link);
            return new ResponseEntity<>(carEntityModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<EntityModel<Car>> getCarByColor(String color) {
        List<Car> allCar = carRepository.getAllList();
        Optional<Car> first = allCar.stream().filter(car -> car.getColor().equalsIgnoreCase(color)).findFirst();
        if (first.isPresent()) {
            allCar.forEach(car -> car.add(linkTo(CarController.class).withRel("allColors")));
            Link link = linkTo(CarController.class).withSelfRel();
            EntityModel<Car> entityModel = new EntityModel<>(first.get(), link);
            return new ResponseEntity<>(entityModel, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity createCar(Car car) {
        carRepository.createCar(car.getId(), car.getMark(), car.getModel(), car.getColor());
        if (car.getFlagAdd()) {
            return new ResponseEntity(HttpStatus.CREATED);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity putCar(Car newCar) {
        List<Car> allList = carRepository.getAllList();
        Optional<Car> first = allList.stream().filter(car -> car.getId() == newCar.getId()).findFirst();
        if (first.isPresent()) {
            carRepository.deleteCar(first.get());
            createCar(newCar);
            return new ResponseEntity(HttpStatus.OK);
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity deleteCarById(int id) {
        List<Car> allList = carRepository.getAllList();
        Optional<Car> first =
                allList.stream().filter(car -> car.getId() == id).findFirst();
        if (first.isPresent()) {
            carRepository.deleteCar(first.get());
            return new ResponseEntity(first.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity("Nie znaleziono podanego identyfikatora", HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity patchCar(Car patchCar) {
        List<Car> allList = carRepository.getAllList();

        Optional<Car> first = allList.stream()
                .filter(car -> car.getId() == patchCar.getId())
                .findFirst();

        if (first.isPresent()) {
            for (Car car : allList) {
                if (!car.getMark().equalsIgnoreCase(patchCar.getMark())
                        || !car.getModel().equalsIgnoreCase(patchCar.getModel())
                        || !car.getColor().equalsIgnoreCase(patchCar.getColor())) {
                    carRepository.deleteCar(first.get());
                    createCar(patchCar);
                    return new ResponseEntity(HttpStatus.OK);
                }
            }
        }
        return new ResponseEntity(HttpStatus.NOT_FOUND);
    }
}
