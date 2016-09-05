package pl.agileit.ccm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CarMake.
 */
@Entity
@Table(name = "car_make")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class CarMake implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    private CarConcern carConcern;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public CarMake name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CarConcern getCarConcern() {
        return carConcern;
    }

    public CarMake carConcern(CarConcern carConcern) {
        this.carConcern = carConcern;
        return this;
    }

    public void setCarConcern(CarConcern carConcern) {
        this.carConcern = carConcern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CarMake carMake = (CarMake) o;
        if(carMake.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, carMake.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CarMake{" +
            "id=" + id +
            ", name='" + name + "'" +
            '}';
    }
}
