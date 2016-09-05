package pl.agileit.ccm.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.agileit.ccm.domain.CarModelGeneration;
import pl.agileit.ccm.service.CarModelGenerationService;
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
 * REST controller for managing CarModelGeneration.
 */
@RestController
@RequestMapping("/api")
public class CarModelGenerationResource {

    private final Logger log = LoggerFactory.getLogger(CarModelGenerationResource.class);
        
    @Inject
    private CarModelGenerationService carModelGenerationService;

    /**
     * POST  /car-model-generations : Create a new carModelGeneration.
     *
     * @param carModelGeneration the carModelGeneration to create
     * @return the ResponseEntity with status 201 (Created) and with body the new carModelGeneration, or with status 400 (Bad Request) if the carModelGeneration has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-model-generations",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModelGeneration> createCarModelGeneration(@RequestBody CarModelGeneration carModelGeneration) throws URISyntaxException {
        log.debug("REST request to save CarModelGeneration : {}", carModelGeneration);
        if (carModelGeneration.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("carModelGeneration", "idexists", "A new carModelGeneration cannot already have an ID")).body(null);
        }
        CarModelGeneration result = carModelGenerationService.save(carModelGeneration);
        return ResponseEntity.created(new URI("/api/car-model-generations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("carModelGeneration", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /car-model-generations : Updates an existing carModelGeneration.
     *
     * @param carModelGeneration the carModelGeneration to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated carModelGeneration,
     * or with status 400 (Bad Request) if the carModelGeneration is not valid,
     * or with status 500 (Internal Server Error) if the carModelGeneration couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/car-model-generations",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModelGeneration> updateCarModelGeneration(@RequestBody CarModelGeneration carModelGeneration) throws URISyntaxException {
        log.debug("REST request to update CarModelGeneration : {}", carModelGeneration);
        if (carModelGeneration.getId() == null) {
            return createCarModelGeneration(carModelGeneration);
        }
        CarModelGeneration result = carModelGenerationService.save(carModelGeneration);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("carModelGeneration", carModelGeneration.getId().toString()))
            .body(result);
    }

    /**
     * GET  /car-model-generations : get all the carModelGenerations.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of carModelGenerations in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/car-model-generations",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<CarModelGeneration>> getAllCarModelGenerations(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of CarModelGenerations");
        Page<CarModelGeneration> page = carModelGenerationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/car-model-generations");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /car-model-generations/:id : get the "id" carModelGeneration.
     *
     * @param id the id of the carModelGeneration to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the carModelGeneration, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/car-model-generations/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<CarModelGeneration> getCarModelGeneration(@PathVariable Long id) {
        log.debug("REST request to get CarModelGeneration : {}", id);
        CarModelGeneration carModelGeneration = carModelGenerationService.findOne(id);
        return Optional.ofNullable(carModelGeneration)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /car-model-generations/:id : delete the "id" carModelGeneration.
     *
     * @param id the id of the carModelGeneration to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/car-model-generations/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteCarModelGeneration(@PathVariable Long id) {
        log.debug("REST request to delete CarModelGeneration : {}", id);
        carModelGenerationService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("carModelGeneration", id.toString())).build();
    }

}
