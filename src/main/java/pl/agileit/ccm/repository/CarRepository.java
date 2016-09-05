package pl.agileit.ccm.repository;

import pl.agileit.ccm.domain.Car;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Car entity.
 */
@SuppressWarnings("unused")
public interface CarRepository extends JpaRepository<Car,Long> {

    @Query("select car from Car car where car.owner.login = ?#{principal.username}")
    List<Car> findByOwnerIsCurrentUser();

}
