package pl.agileit.ccm.service;

import pl.agileit.ccm.domain.Refueling;
import pl.agileit.ccm.repository.RefuelingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing Refueling.
 */
@Service
@Transactional
public class RefuelingService {

    private final Logger log = LoggerFactory.getLogger(RefuelingService.class);

    @Inject
    private RefuelingRepository refuelingRepository;

    /**
     * Save a refueling.
     *
     * @param refueling the entity to save
     * @return the persisted entity
     */
    public Refueling save(Refueling refueling) {
        log.debug("Request to save Refueling : {}", refueling);
        Refueling result = refuelingRepository.save(refueling);
        return result;
    }

    /**
     *  Get all the refuelings.
     *
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public List<Refueling> findAll() {
        log.debug("Request to get all Refuelings");
        List<Refueling> result = refuelingRepository.findAll();

        return result;
    }

    /**
     *  Get one refueling by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true)
    public Refueling findOne(Long id) {
        log.debug("Request to get Refueling : {}", id);
        Refueling refueling = refuelingRepository.findOne(id);
        return refueling;
    }

    /**
     *  Delete the  refueling by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Refueling : {}", id);
        refuelingRepository.delete(id);
    }

    public List<Refueling> findByUser() {
        return refuelingRepository.findByUserIsCurrentUser();
    }
}
