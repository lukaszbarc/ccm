package pl.agileit.ccm.web.rest;

import pl.agileit.ccm.CcmApp;
import pl.agileit.ccm.domain.CarModelGeneration;
import pl.agileit.ccm.repository.CarModelGenerationRepository;
import pl.agileit.ccm.service.CarModelGenerationService;

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
 * Test class for the CarModelGenerationResource REST controller.
 *
 * @see CarModelGenerationResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CcmApp.class)
public class CarModelGenerationResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private CarModelGenerationRepository carModelGenerationRepository;

    @Inject
    private CarModelGenerationService carModelGenerationService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCarModelGenerationMockMvc;

    private CarModelGeneration carModelGeneration;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarModelGenerationResource carModelGenerationResource = new CarModelGenerationResource();
        ReflectionTestUtils.setField(carModelGenerationResource, "carModelGenerationService", carModelGenerationService);
        this.restCarModelGenerationMockMvc = MockMvcBuilders.standaloneSetup(carModelGenerationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModelGeneration createEntity(EntityManager em) {
        CarModelGeneration carModelGeneration = new CarModelGeneration();
        carModelGeneration = new CarModelGeneration()
                .name(DEFAULT_NAME);
        return carModelGeneration;
    }

    @Before
    public void initTest() {
        carModelGeneration = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarModelGeneration() throws Exception {
        int databaseSizeBeforeCreate = carModelGenerationRepository.findAll().size();

        // Create the CarModelGeneration

        restCarModelGenerationMockMvc.perform(post("/api/car-model-generations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carModelGeneration)))
                .andExpect(status().isCreated());

        // Validate the CarModelGeneration in the database
        List<CarModelGeneration> carModelGenerations = carModelGenerationRepository.findAll();
        assertThat(carModelGenerations).hasSize(databaseSizeBeforeCreate + 1);
        CarModelGeneration testCarModelGeneration = carModelGenerations.get(carModelGenerations.size() - 1);
        assertThat(testCarModelGeneration.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCarModelGenerations() throws Exception {
        // Initialize the database
        carModelGenerationRepository.saveAndFlush(carModelGeneration);

        // Get all the carModelGenerations
        restCarModelGenerationMockMvc.perform(get("/api/car-model-generations?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carModelGeneration.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarModelGeneration() throws Exception {
        // Initialize the database
        carModelGenerationRepository.saveAndFlush(carModelGeneration);

        // Get the carModelGeneration
        restCarModelGenerationMockMvc.perform(get("/api/car-model-generations/{id}", carModelGeneration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carModelGeneration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarModelGeneration() throws Exception {
        // Get the carModelGeneration
        restCarModelGenerationMockMvc.perform(get("/api/car-model-generations/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarModelGeneration() throws Exception {
        // Initialize the database
        carModelGenerationService.save(carModelGeneration);

        int databaseSizeBeforeUpdate = carModelGenerationRepository.findAll().size();

        // Update the carModelGeneration
        CarModelGeneration updatedCarModelGeneration = carModelGenerationRepository.findOne(carModelGeneration.getId());
        updatedCarModelGeneration
                .name(UPDATED_NAME);

        restCarModelGenerationMockMvc.perform(put("/api/car-model-generations")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCarModelGeneration)))
                .andExpect(status().isOk());

        // Validate the CarModelGeneration in the database
        List<CarModelGeneration> carModelGenerations = carModelGenerationRepository.findAll();
        assertThat(carModelGenerations).hasSize(databaseSizeBeforeUpdate);
        CarModelGeneration testCarModelGeneration = carModelGenerations.get(carModelGenerations.size() - 1);
        assertThat(testCarModelGeneration.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCarModelGeneration() throws Exception {
        // Initialize the database
        carModelGenerationService.save(carModelGeneration);

        int databaseSizeBeforeDelete = carModelGenerationRepository.findAll().size();

        // Get the carModelGeneration
        restCarModelGenerationMockMvc.perform(delete("/api/car-model-generations/{id}", carModelGeneration.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CarModelGeneration> carModelGenerations = carModelGenerationRepository.findAll();
        assertThat(carModelGenerations).hasSize(databaseSizeBeforeDelete - 1);
    }
}
