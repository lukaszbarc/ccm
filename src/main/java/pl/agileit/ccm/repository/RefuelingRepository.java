package pl.agileit.ccm.repository;

import pl.agileit.ccm.domain.Refueling;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Refueling entity.
 */
@SuppressWarnings("unused")
public interface RefuelingRepository extends JpaRepository<Refueling,Long> {

    @Query("select refueling from Refueling refueling where refueling.user.login = ?#{principal.username}")
    List<Refueling> findByUserIsCurrentUser();

}
