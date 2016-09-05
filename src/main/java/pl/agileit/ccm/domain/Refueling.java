package pl.agileit.ccm.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A Refueling.
 */
@Entity
@Table(name = "refueling")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Refueling implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "date")
    private ZonedDateTime date;

    @Column(name = "milage", precision=10, scale=2)
    private BigDecimal milage;

    @Column(name = "trip", precision=10, scale=2)
    private BigDecimal trip;

    @Column(name = "quantity", precision=10, scale=2)
    private BigDecimal quantity;

    @Column(name = "cost", precision=10, scale=2)
    private BigDecimal cost;

    @Column(name = "comment")
    private String comment;

    @ManyToOne
    private User user;

    @ManyToOne
    private Car car;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public Refueling date(ZonedDateTime date) {
        this.date = date;
        return this;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public BigDecimal getMilage() {
        return milage;
    }

    public Refueling milage(BigDecimal milage) {
        this.milage = milage;
        return this;
    }

    public void setMilage(BigDecimal milage) {
        this.milage = milage;
    }

    public BigDecimal getTrip() {
        return trip;
    }

    public Refueling trip(BigDecimal trip) {
        this.trip = trip;
        return this;
    }

    public void setTrip(BigDecimal trip) {
        this.trip = trip;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Refueling quantity(BigDecimal quantity) {
        this.quantity = quantity;
        return this;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public Refueling cost(BigDecimal cost) {
        this.cost = cost;
        return this;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getComment() {
        return comment;
    }

    public Refueling comment(String comment) {
        this.comment = comment;
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public Refueling user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Car getCar() {
        return car;
    }

    public Refueling car(Car car) {
        this.car = car;
        return this;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Refueling refueling = (Refueling) o;
        if(refueling.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, refueling.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Refueling{" +
            "id=" + id +
            ", date='" + date + "'" +
            ", milage='" + milage + "'" +
            ", trip='" + trip + "'" +
            ", quantity='" + quantity + "'" +
            ", cost='" + cost + "'" +
            ", comment='" + comment + "'" +
            '}';
    }
}
