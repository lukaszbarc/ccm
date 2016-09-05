package pl.agileit.ccm.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.agileit.ccm.domain.CarConcern;
import pl.agileit.ccm.service.CarConcernService;
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
 * REST controller for managing CarConcern.
 */
@RestController
@RequestMapping("/api")
public class CarConcernResource {

    private final Logger log = LoggerFactory.getLogger(CarConcernResource.class);
        
    @Inject
    private CarConcernService carConcernService;

    /**
     * POST  /car-concerns : Create a new carConcern.
     *
     * @param carConcern the carConcern to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carConcern, or with status 400 (Bad Request) if the carConcern has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-concerns",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarConcern> createCarConcern(@RequestBody CarConcern carConcern) throws URISyntaxException {
        log.debug("REST request to save CarConcern : {}", carConcern);
        if (carConcern.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carConcern", "idexists", "A new carConcern cannot already have an ID")).body(null);
        }
        CarConcern result = carConcernService.save(carConcern);
        return ResponseEntity.created(new URI("/api/car-concerns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carConcern", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-concerns : Updates an existing carConcern.
     *
     * @param carConcern the carConcern to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carConcern,
     * or with status 400 (Bad Request) if the carConcern is not valid,
     * or with status 500 (Internal Server Error) if the carConcern couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-concerns",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarConcern> updateCarConcern(@RequestBody CarConcern carConcern) throws URISyntaxException {
        log.debug("REST request to update CarConcern : {}", carConcern);
        if (carConcern.getId() == null) {
            return createCarConcern(carConcern);
        }
        CarConcern result = carConcernService.save(carConcern);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carConcern", carConcern.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-concerns : get all the carConcerns.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carConcerns in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/car-concerns",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CarConcern>> getAllCarConcerns(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CarConcerns");
        Page<CarConcern> page = carConcernService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car-concerns");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car-concerns/:id : get the "id" carConcern.
     *
     * @param id the id of the carConcern to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carConcern, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/car-concerns/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarConcern> getCarConcern(@PathVariable Long id) {
        log.debug("REST request to get CarConcern : {}", id);
        CarConcern carConcern = carConcernService.findOne(id);
        return Optional.ofNullable(carConcern)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car-concerns/:id : delete the "id" carConcern.
     *
     * @param id the id of the carConcern to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/car-concerns/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarConcern(@PathVariable Long id) {
        log.debug("REST request to delete CarConcern : {}", id);
        carConcernService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carConcern", id.toString())).build();
    }

}
