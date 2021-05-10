package com.example.restapi_car.repository;

import com.example.restapi_car.domain.Car;
import org.springframework.hateoas.CollectionModel;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CarRepository {


    List<Car> carList = new ArrayList<>();


    public void createCar(int id, String mark, String model, String color) {
        Car car = new Car(id, mark, model, color);
        boolean add = carList.add(car);
        if (add) {
            car.setFlagAdd(add);
        } else car.setFlagAdd(false);
    }

    @PostConstruct
    public void build() {
        createCar(1, "BMW", "320i", "white");
        createCar(2, "Honda", "accord 8", "black");
        createCar(3, "Honda", "CR-V", "grey");
    }

    public Car getCarById(int id) {
        return carList.get(id);
    }

    public List<Car> getAllList() {
        return carList;
    }

    public void deleteCar(Car car) {
         carList.remove(car);
    }


    @Override
    public String toString() {
        return "CarRepository{" +
                "carList=" + carList +
                '}';
    }

}
