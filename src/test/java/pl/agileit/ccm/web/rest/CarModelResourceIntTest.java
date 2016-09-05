package pl.agileit.ccm.web.rest;

import pl.agileit.ccm.CcmApp;
import pl.agileit.ccm.domain.CarModel;
import pl.agileit.ccm.repository.CarModelRepository;
import pl.agileit.ccm.service.CarModelService;

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
 * Test class for the CarModelResource REST controller.
 *
 * @see CarModelResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CcmApp.class)
public class CarModelResourceIntTest {
    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private CarModelRepository carModelRepository;

    @Inject
    private CarModelService carModelService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restCarModelMockMvc;

    private CarModel carModel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        CarModelResource carModelResource = new CarModelResource();
        ReflectionTestUtils.setField(carModelResource, "carModelService", carModelService);
        this.restCarModelMockMvc = MockMvcBuilders.standaloneSetup(carModelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CarModel createEntity(EntityManager em) {
        CarModel carModel = new CarModel();
        carModel = new CarModel()
                .name(DEFAULT_NAME);
        return carModel;
    }

    @Before
    public void initTest() {
        carModel = createEntity(em);
    }

    @Test
    @Transactional
    public void createCarModel() throws Exception {
        int databaseSizeBeforeCreate = carModelRepository.findAll().size();

        // Create the CarModel

        restCarModelMockMvc.perform(post("/api/car-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(carModel)))
                .andExpect(status().isCreated());

        // Validate the CarModel in the database
        List<CarModel> carModels = carModelRepository.findAll();
        assertThat(carModels).hasSize(databaseSizeBeforeCreate + 1);
        CarModel testCarModel = carModels.get(carModels.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllCarModels() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get all the carModels
        restCarModelMockMvc.perform(get("/api/car-models?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(carModel.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getCarModel() throws Exception {
        // Initialize the database
        carModelRepository.saveAndFlush(carModel);

        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", carModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(carModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCarModel() throws Exception {
        // Get the carModel
        restCarModelMockMvc.perform(get("/api/car-models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCarModel() throws Exception {
        // Initialize the database
        carModelService.save(carModel);

        int databaseSizeBeforeUpdate = carModelRepository.findAll().size();

        // Update the carModel
        CarModel updatedCarModel = carModelRepository.findOne(carModel.getId());
        updatedCarModel
                .name(UPDATED_NAME);

        restCarModelMockMvc.perform(put("/api/car-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedCarModel)))
                .andExpect(status().isOk());

        // Validate the CarModel in the database
        List<CarModel> carModels = carModelRepository.findAll();
        assertThat(carModels).hasSize(databaseSizeBeforeUpdate);
        CarModel testCarModel = carModels.get(carModels.size() - 1);
        assertThat(testCarModel.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteCarModel() throws Exception {
        // Initialize the database
        carModelService.save(carModel);

        int databaseSizeBeforeDelete = carModelRepository.findAll().size();

        // Get the carModel
        restCarModelMockMvc.perform(delete("/api/car-models/{id}", carModel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<CarModel> carModels = carModelRepository.findAll();
        assertThat(carModels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
