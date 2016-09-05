package pl.agileit.ccm.service;

import pl.agileit.ccm.domain.CarModelGeneration;
import pl.agileit.ccm.repository.CarModelGenerationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CarModelGeneration.
 */
@Service
@Transactional
public class CarModelGenerationService {

    private final Logger log = LoggerFactory.getLogger(CarModelGenerationService.class);
    
    @Inject
    private CarModelGenerationRepository carModelGenerationRepository;

    /**
     * Save a carModelGeneration.
     *
     * @param carModelGeneration the entity to save
     * @return the persisted entity
     */
    public CarModelGeneration save(CarModelGeneration carModelGeneration) {
        log.debug("Request to save CarModelGeneration : {}", carModelGeneration);
        CarModelGeneration result = carModelGenerationRepository.save(carModelGeneration);
        return result;
    }

    /**
     *  Get all the carModelGenerations.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CarModelGeneration> findAll(Pageable pageable) {
        log.debug("Request to get all CarModelGenerations");
        Page<CarModelGeneration> result = carModelGenerationRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one carModelGeneration by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CarModelGeneration findOne(Long id) {
        log.debug("Request to get CarModelGeneration : {}", id);
        CarModelGeneration carModelGeneration = carModelGenerationRepository.findOne(id);
        return carModelGeneration;
    }

    /**
     *  Delete the  carModelGeneration by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CarModelGeneration : {}", id);
        carModelGenerationRepository.delete(id);
    }
}
