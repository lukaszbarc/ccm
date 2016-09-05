package pl.agileit.ccm.repository;

import pl.agileit.ccm.domain.CarConcern;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CarConcern entity.
 */
@SuppressWarnings("unused")
public interface CarConcernRepository extends JpaRepository<CarConcern,Long> {

}
