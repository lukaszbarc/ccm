package pl.agileit.ccm.repository;

import pl.agileit.ccm.domain.CarModelGeneration;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the CarModelGeneration entity.
 */
@SuppressWarnings("unused")
public interface CarModelGenerationRepository extends JpaRepository<CarModelGeneration,Long> {

}
