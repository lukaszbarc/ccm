package pl.agileit.ccm.repository;

import pl.agileit.ccm.domain.CarMake;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CarMake entity.
 */
@SuppressWarnings("unused")
public interface CarMakeRepository extends JpaRepository<CarMake,Long> {

}
