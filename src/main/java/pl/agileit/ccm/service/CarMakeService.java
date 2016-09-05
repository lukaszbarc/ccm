package pl.agileit.ccm.service;

import pl.agileit.ccm.domain.CarMake;
import pl.agileit.ccm.repository.CarMakeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CarMake.
 */
@Service
@Transactional
public class CarMakeService {

    private final Logger log = LoggerFactory.getLogger(CarMakeService.class);
    
    @Inject
    private CarMakeRepository carMakeRepository;

    /**
     * Save a carMake.
     *
     * @param carMake the entity to save
     * @return the persisted entity
     */
    public CarMake save(CarMake carMake) {
        log.debug("Request to save CarMake : {}", carMake);
        CarMake result = carMakeRepository.save(carMake);
        return result;
    }

    /**
     *  Get all the carMakes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CarMake> findAll(Pageable pageable) {
        log.debug("Request to get all CarMakes");
        Page<CarMake> result = carMakeRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one carMake by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CarMake findOne(Long id) {
        log.debug("Request to get CarMake : {}", id);
        CarMake carMake = carMakeRepository.findOne(id);
        return carMake;
    }

    /**
     *  Delete the  carMake by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CarMake : {}", id);
        carMakeRepository.delete(id);
    }
}
