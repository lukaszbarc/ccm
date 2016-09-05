package pl.agileit.ccm.web.rest;

import com.codahale.metrics.annotation.Timed;
import pl.agileit.ccm.domain.Refueling;
import pl.agileit.ccm.service.RefuelingService;
import pl.agileit.ccm.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing Refueling.
 */
@RestController
@RequestMapping("/api")
public class RefuelingResource {

    private final Logger log = LoggerFactory.getLogger(RefuelingResource.class);
        
    @Inject
    private RefuelingService refuelingService;

    /**
     * POST  /refuelings : Create a new refueling.
     *
     * @param refueling the refueling to create
     * @return the ResponseEntity with status 201 (Created) and with body the new refueling, or with status 400 (Bad Request) if the refueling has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/refuelings",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Refueling> createRefueling(@RequestBody Refueling refueling) throws URISyntaxException {
        log.debug("REST request to save Refueling : {}", refueling);
        if (refueling.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("refueling", "idexists", "A new refueling cannot already have an ID")).body(null);
        }
        Refueling result = refuelingService.save(refueling);
        return ResponseEntity.created(new URI("/api/refuelings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("refueling", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /refuelings : Updates an existing refueling.
     *
     * @param refueling the refueling to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated refueling,
     * or with status 400 (Bad Request) if the refueling is not valid,
     * or with status 500 (Internal Server Error) if the refueling couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/refuelings",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Refueling> updateRefueling(@RequestBody Refueling refueling) throws URISyntaxException {
        log.debug("REST request to update Refueling : {}", refueling);
        if (refueling.getId() == null) {
            return createRefueling(refueling);
        }
        Refueling result = refuelingService.save(refueling);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("refueling", refueling.getId().toString()))
            .body(result);
    }

    /**
     * GET  /refuelings : get all the refuelings.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of refuelings in body
     */
    @RequestMapping(value = "/refuelings",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Refueling> getAllRefuelings() {
        log.debug("REST request to get all Refuelings");
        return refuelingService.findAll();
    }

    /**
     * GET  /refuelings/:id : get the "id" refueling.
     *
     * @param id the id of the refueling to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the refueling, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/refuelings/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Refueling> getRefueling(@PathVariable Long id) {
        log.debug("REST request to get Refueling : {}", id);
        Refueling refueling = refuelingService.findOne(id);
        return Optional.ofNullable(refueling)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /refuelings/:id : delete the "id" refueling.
     *
     * @param id the id of the refueling to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/refuelings/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRefueling(@PathVariable Long id) {
        log.debug("REST request to delete Refueling : {}", id);
        refuelingService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("refueling", id.toString())).build();
    }

}
