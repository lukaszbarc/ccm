package pl.agileit.ccm.web.rest.api;

import com.codahale.metrics.annotation.Timed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.agileit.ccm.domain.Car;
import pl.agileit.ccm.domain.User;
import pl.agileit.ccm.service.CarService;
import pl.agileit.ccm.web.rest.CarResource;
import pl.agileit.ccm.web.rest.util.HeaderUtil;
import pl.agileit.ccm.web.rest.util.PaginationUtil;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/public/api")
public class ApiCarResource {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiCarResource.class);

    @Inject
    private CarService carService;

    @RequestMapping(value = "/cars",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Car> createCar(@RequestBody Car car, User user) throws URISyntaxException {

        if (car.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("car", "idexists", "A new car cannot already have an ID")).body(null);
        }
        car.setOwner(user);
        Car result = carService.save(car);
        return ResponseEntity.created(new URI("/public/api/cars/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("car", result.getId().toString()))
            .body(result);
    }


    @RequestMapping(value = "/cars",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Car>> getAllCars()
        throws URISyntaxException {
        List<Car> cars = carService.findByUser();
        return new ResponseEntity<>(cars, HttpStatus.OK);
    }
}
