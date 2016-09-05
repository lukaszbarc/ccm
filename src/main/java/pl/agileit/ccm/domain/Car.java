package pl.agileit.ccm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Car.
 */
@Entity
@Table(name = "car")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Car implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private CarConcern carConcern;

    @ManyToOne
    private CarMake carMake;

    @ManyToOne
    private CarModel carModel;

    @ManyToOne
    private CarModelGeneration carModelGeneration;

    @ManyToOne
    private User owner;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Car name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarConcern getCarConcern() {
        return carConcern;
    }

    public Car carConcern(CarConcern carConcern) {
        this.carConcern = carConcern;
        return this;
    }

    public void setCarConcern(CarConcern carConcern) {
        this.carConcern = carConcern;
    }

    public CarMake getCarMake() {
        return carMake;
    }

    public Car carMake(CarMake carMake) {
        this.carMake = carMake;
        return this;
    }

    public void setCarMake(CarMake carMake) {
        this.carMake = carMake;
    }

    public CarModel getCarModel() {
        return carModel;
    }

    public Car carModel(CarModel carModel) {
        this.carModel = carModel;
        return this;
    }

    public void setCarModel(CarModel carModel) {
        this.carModel = carModel;
    }

    public CarModelGeneration getCarModelGeneration() {
        return carModelGeneration;
    }

    public Car carModelGeneration(CarModelGeneration carModelGeneration) {
        this.carModelGeneration = carModelGeneration;
        return this;
    }

    public void setCarModelGeneration(CarModelGeneration carModelGeneration) {
        this.carModelGeneration = carModelGeneration;
    }

    public User getOwner() {
        return owner;
    }

    public Car owner(User user) {
        this.owner = user;
        return this;
    }

    public void setOwner(User user) {
        this.owner = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Car car = (Car) o;
        if(car.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, car.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Car{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
