package pl.agileit.ccm.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.agileit.ccm.domain.CarMake;
import pl.agileit.ccm.service.CarMakeService;
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
 * REST controller for managing CarMake.
 */
@RestController
@RequestMapping("/api")
public class CarMakeResource {

    private final Logger log = LoggerFactory.getLogger(CarMakeResource.class);
        
    @Inject
    private CarMakeService carMakeService;

    /**
     * POST  /car-makes : Create a new carMake.
     *
     * @param carMake the carMake to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carMake, or with status 400 (Bad Request) if the carMake has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-makes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarMake> createCarMake(@RequestBody CarMake carMake) throws URISyntaxException {
        log.debug("REST request to save CarMake : {}", carMake);
        if (carMake.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carMake", "idexists", "A new carMake cannot already have an ID")).body(null);
        }
        CarMake result = carMakeService.save(carMake);
        return ResponseEntity.created(new URI("/api/car-makes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carMake", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-makes : Updates an existing carMake.
     *
     * @param carMake the carMake to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carMake,
     * or with status 400 (Bad Request) if the carMake is not valid,
     * or with status 500 (Internal Server Error) if the carMake couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-makes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarMake> updateCarMake(@RequestBody CarMake carMake) throws URISyntaxException {
        log.debug("REST request to update CarMake : {}", carMake);
        if (carMake.getId() == null) {
            return createCarMake(carMake);
        }
        CarMake result = carMakeService.save(carMake);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carMake", carMake.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-makes : get all the carMakes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carMakes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/car-makes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CarMake>> getAllCarMakes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CarMakes");
        Page<CarMake> page = carMakeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car-makes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car-makes/:id : get the "id" carMake.
     *
     * @param id the id of the carMake to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carMake, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/car-makes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarMake> getCarMake(@PathVariable Long id) {
        log.debug("REST request to get CarMake : {}", id);
        CarMake carMake = carMakeService.findOne(id);
        return Optional.ofNullable(carMake)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car-makes/:id : delete the "id" carMake.
     *
     * @param id the id of the carMake to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/car-makes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarMake(@PathVariable Long id) {
        log.debug("REST request to delete CarMake : {}", id);
        carMakeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carMake", id.toString())).build();
    }

}
