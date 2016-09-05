package pl.agileit.ccm.service;

import pl.agileit.ccm.domain.CarModel;
import pl.agileit.ccm.repository.CarModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;

/**
 * Service Implementation for managing CarModel.
 */
@Service
@Transactional
public class CarModelService {

    private final Logger log = LoggerFactory.getLogger(CarModelService.class);
    
    @Inject
    private CarModelRepository carModelRepository;

    /**
     * Save a carModel.
     *
     * @param carModel the entity to save
     * @return the persisted entity
     */
    public CarModel save(CarModel carModel) {
        log.debug("Request to save CarModel : {}", carModel);
        CarModel result = carModelRepository.save(carModel);
        return result;
    }

    /**
     *  Get all the carModels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<CarModel> findAll(Pageable pageable) {
        log.debug("Request to get all CarModels");
        Page<CarModel> result = carModelRepository.findAll(pageable);
        return result;
    }

    /**
     *  Get one carModel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public CarModel findOne(Long id) {
        log.debug("Request to get CarModel : {}", id);
        CarModel carModel = carModelRepository.findOne(id);
        return carModel;
    }

    /**
     *  Delete the  carModel by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete CarModel : {}", id);
        carModelRepository.delete(id);
    }
}
