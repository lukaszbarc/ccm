package pl.agileit.ccm.web.rest;

import pl.agileit.ccm.CcmApp;
import pl.agileit.ccm.domain.CarConcern;
import pl.agileit.ccm.repository.CarConcernRepository;
import pl.agileit.ccm.service.CarConcernService;

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
 * Test class for the CarConcernResource REST controller.
 *
 * @see CarConcernResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CcmApp.class)
public class CarConcernResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private CarConcernRepository carConcernRepository;

    @Inject
    private CarConcernService carConcernService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCarConcernMockMvc;

    private CarConcern carConcern;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarConcernResource carConcernResource = new CarConcernResource();
        ReflectionTestUtils.setField(carConcernResource, "carConcernService", carConcernService);
        this.restCarConcernMockMvc = MockMvcBuilders.standaloneSetup(carConcernResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarConcern createEntity(EntityManager em) {
        CarConcern carConcern = new CarConcern();
        carConcern = new CarConcern()
                .name(DEFAULT_NAME);
        return carConcern;
    }

    @Before
    public void initTest() {
        carConcern = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarConcern() throws Exception {
        int databaseSizeBeforeCreate = carConcernRepository.findAll().size();

        // Create the CarConcern

        restCarConcernMockMvc.perform(post("/api/car-concerns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carConcern)))
                .andExpect(status().isCreated());

        // Validate the CarConcern in the database
        List<CarConcern> carConcerns = carConcernRepository.findAll();
        assertThat(carConcerns).hasSize(databaseSizeBeforeCreate + 1);
        CarConcern testCarConcern = carConcerns.get(carConcerns.size() - 1);
        assertThat(testCarConcern.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCarConcerns() throws Exception {
        // Initialize the database
        carConcernRepository.saveAndFlush(carConcern);

        // Get all the carConcerns
        restCarConcernMockMvc.perform(get("/api/car-concerns?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carConcern.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarConcern() throws Exception {
        // Initialize the database
        carConcernRepository.saveAndFlush(carConcern);

        // Get the carConcern
        restCarConcernMockMvc.perform(get("/api/car-concerns/{id}", carConcern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carConcern.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarConcern() throws Exception {
        // Get the carConcern
        restCarConcernMockMvc.perform(get("/api/car-concerns/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarConcern() throws Exception {
        // Initialize the database
        carConcernService.save(carConcern);

        int databaseSizeBeforeUpdate = carConcernRepository.findAll().size();

        // Update the carConcern
        CarConcern updatedCarConcern = carConcernRepository.findOne(carConcern.getId());
        updatedCarConcern
                .name(UPDATED_NAME);

        restCarConcernMockMvc.perform(put("/api/car-concerns")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCarConcern)))
                .andExpect(status().isOk());

        // Validate the CarConcern in the database
        List<CarConcern> carConcerns = carConcernRepository.findAll();
        assertThat(carConcerns).hasSize(databaseSizeBeforeUpdate);
        CarConcern testCarConcern = carConcerns.get(carConcerns.size() - 1);
        assertThat(testCarConcern.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCarConcern() throws Exception {
        // Initialize the database
        carConcernService.save(carConcern);

        int databaseSizeBeforeDelete = carConcernRepository.findAll().size();

        // Get the carConcern
        restCarConcernMockMvc.perform(delete("/api/car-concerns/{id}", carConcern.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CarConcern> carConcerns = carConcernRepository.findAll();
        assertThat(carConcerns).hasSize(databaseSizeBeforeDelete - 1);
    }
}
