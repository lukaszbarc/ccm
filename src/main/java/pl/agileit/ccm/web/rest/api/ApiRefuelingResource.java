package pl.agileit.ccm.web.rest.api;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.agileit.ccm.domain.Car;
import pl.agileit.ccm.domain.Refueling;
import pl.agileit.ccm.service.CarService;
import pl.agileit.ccm.service.RefuelingService;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/public/api")
public class ApiRefuelingResource {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiRefuelingResource.class);

    @Inject
    private RefuelingService refuelingService;

    @RequestMapping(value = "/refueling",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Refueling>> getAllRefueling()
        throws URISyntaxException {
        List<Refueling> refueling = refuelingService.findByUser();
        return new ResponseEntity<>(refueling, HttpStatus.OK);
    }
}
