package pl.agileit.ccm.web.rest;

import pl.agileit.ccm.CcmApp;
import pl.agileit.ccm.domain.CarMake;
import pl.agileit.ccm.repository.CarMakeRepository;
import pl.agileit.ccm.service.CarMakeService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the CarMakeResource REST controller.
 *
 * @see CarMakeResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CcmApp.class)
public class CarMakeResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private CarMakeRepository carMakeRepository;

    @Inject
    private CarMakeService carMakeService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCarMakeMockMvc;

    private CarMake carMake;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarMakeResource carMakeResource = new CarMakeResource();
        ReflectionTestUtils.setField(carMakeResource, "carMakeService", carMakeService);
        this.restCarMakeMockMvc = MockMvcBuilders.standaloneSetup(carMakeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarMake createEntity(EntityManager em) {
        CarMake carMake = new CarMake();
        carMake = new CarMake()
                .name(DEFAULT_NAME);
        return carMake;
    }

    @Before
    public void initTest() {
        carMake = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarMake() throws Exception {
        int databaseSizeBeforeCreate = carMakeRepository.findAll().size();

        // Create the CarMake

        restCarMakeMockMvc.perform(post("/api/car-makes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carMake)))
                .andExpect(status().isCreated());

        // Validate the CarMake in the database
        List<CarMake> carMakes = carMakeRepository.findAll();
        assertThat(carMakes).hasSize(databaseSizeBeforeCreate + 1);
        CarMake testCarMake = carMakes.get(carMakes.size() - 1);
        assertThat(testCarMake.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCarMakes() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        // Get all the carMakes
        restCarMakeMockMvc.perform(get("/api/car-makes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carMake.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarMake() throws Exception {
        // Initialize the database
        carMakeRepository.saveAndFlush(carMake);

        // Get the carMake
        restCarMakeMockMvc.perform(get("/api/car-makes/{id}", carMake.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carMake.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarMake() throws Exception {
        // Get the carMake
        restCarMakeMockMvc.perform(get("/api/car-makes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarMake() throws Exception {
        // Initialize the database
        carMakeService.save(carMake);

        int databaseSizeBeforeUpdate = carMakeRepository.findAll().size();

        // Update the carMake
        CarMake updatedCarMake = carMakeRepository.findOne(carMake.getId());
        updatedCarMake
                .name(UPDATED_NAME);

        restCarMakeMockMvc.perform(put("/api/car-makes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCarMake)))
                .andExpect(status().isOk());

        // Validate the CarMake in the database
        List<CarMake> carMakes = carMakeRepository.findAll();
        assertThat(carMakes).hasSize(databaseSizeBeforeUpdate);
        CarMake testCarMake = carMakes.get(carMakes.size() - 1);
        assertThat(testCarMake.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCarMake() throws Exception {
        // Initialize the database
        carMakeService.save(carMake);

        int databaseSizeBeforeDelete = carMakeRepository.findAll().size();

        // Get the carMake
        restCarMakeMockMvc.perform(delete("/api/car-makes/{id}", carMake.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CarMake> carMakes = carMakeRepository.findAll();
        assertThat(carMakes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
