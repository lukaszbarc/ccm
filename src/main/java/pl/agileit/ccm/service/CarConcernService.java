package pl.agileit.ccm.service;

import pl.agileit.ccm.domain.CarConcern;
import pl.agileit.ccm.repository.CarConcernRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CarConcern.
 */
@Service
@Transactional
public class CarConcernService {

    private final Logger log = LoggerFactory.getLogger(CarConcernService.class);
    
    @Inject
    private CarConcernRepository carConcernRepository;

    /**
     * Save a carConcern.
     *
     * @param carConcern the entity to save
     * @return the persisted entity
     */
    public CarConcern save(CarConcern carConcern) {
        log.debug("Request to save CarConcern : {}", carConcern);
        CarConcern result = carConcernRepository.save(carConcern);
        return result;
    }

    /**
     *  Get all the carConcerns.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CarConcern> findAll(Pageable pageable) {
        log.debug("Request to get all CarConcerns");
        Page<CarConcern> result = carConcernRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one carConcern by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CarConcern findOne(Long id) {
        log.debug("Request to get CarConcern : {}", id);
        CarConcern carConcern = carConcernRepository.findOne(id);
        return carConcern;
    }

    /**
     *  Delete the  carConcern by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CarConcern : {}", id);
        carConcernRepository.delete(id);
    }
}
