package pl.agileit.ccm.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.agileit.ccm.domain.CarModel;
import pl.agileit.ccm.service.CarModelService;
import pl.agileit.ccm.web.rest.util.HeaderUtil;
import pl.agileit.ccm.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing CarModel.
 */
@RestController
@RequestMapping("/api")
public class CarModelResource {

    private final Logger log = LoggerFactory.getLogger(CarModelResource.class);
        
    @Inject
    private CarModelService carModelService;

    /**
     * POST  /car-models : Create a new carModel.
     *
     * @param carModel the carModel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carModel, or with status 400 (Bad Request) if the carModel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModel> createCarModel(@RequestBody CarModel carModel) throws URISyntaxException {
        log.debug("REST request to save CarModel : {}", carModel);
        if (carModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carModel", "idexists", "A new carModel cannot already have an ID")).body(null);
        }
        CarModel result = carModelService.save(carModel);
        return ResponseEntity.created(new URI("/api/car-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-models : Updates an existing carModel.
     *
     * @param carModel the carModel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carModel,
     * or with status 400 (Bad Request) if the carModel is not valid,
     * or with status 500 (Internal Server Error) if the carModel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-models",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModel> updateCarModel(@RequestBody CarModel carModel) throws URISyntaxException {
        log.debug("REST request to update CarModel : {}", carModel);
        if (carModel.getId() == null) {
            return createCarModel(carModel);
        }
        CarModel result = carModelService.save(carModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carModel", carModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-models : get all the carModels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carModels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/car-models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CarModel>> getAllCarModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CarModels");
        Page<CarModel> page = carModelService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car-models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car-models/:id : get the "id" carModel.
     *
     * @param id the id of the carModel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carModel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/car-models/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModel> getCarModel(@PathVariable Long id) {
        log.debug("REST request to get CarModel : {}", id);
        CarModel carModel = carModelService.findOne(id);
        return Optional.ofNullable(carModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car-models/:id : delete the "id" carModel.
     *
     * @param id the id of the carModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/car-models/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarModel(@PathVariable Long id) {
        log.debug("REST request to delete CarModel : {}", id);
        carModelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carModel", id.toString())).build();
    }

}
