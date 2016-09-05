package pl.agileit.ccm.web.rest;

import pl.agileit.ccm.CcmApp;
import pl.agileit.ccm.domain.Refueling;
import pl.agileit.ccm.repository.RefuelingRepository;
import pl.agileit.ccm.service.RefuelingService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the RefuelingResource REST controller.
 *
 * @see RefuelingResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CcmApp.class)
public class RefuelingResourceIntTest {
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final ZonedDateTime DEFAULT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_DATE_STR = dateTimeFormatter.format(DEFAULT_DATE);

    private static final BigDecimal DEFAULT_MILAGE = new BigDecimal(1);
    private static final BigDecimal UPDATED_MILAGE = new BigDecimal(2);

    private static final BigDecimal DEFAULT_TRIP = new BigDecimal(1);
    private static final BigDecimal UPDATED_TRIP = new BigDecimal(2);

    private static final BigDecimal DEFAULT_QUANTITY = new BigDecimal(1);
    private static final BigDecimal UPDATED_QUANTITY = new BigDecimal(2);

    private static final BigDecimal DEFAULT_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_COST = new BigDecimal(2);
    private static final String DEFAULT_COMMENT = "AAAAA";
    private static final String UPDATED_COMMENT = "BBBBB";

    @Inject
    private RefuelingRepository refuelingRepository;

    @Inject
    private RefuelingService refuelingService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restRefuelingMockMvc;

    private Refueling refueling;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RefuelingResource refuelingResource = new RefuelingResource();
        ReflectionTestUtils.setField(refuelingResource, "refuelingService", refuelingService);
        this.restRefuelingMockMvc = MockMvcBuilders.standaloneSetup(refuelingResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Refueling createEntity(EntityManager em) {
        Refueling refueling = new Refueling();
        refueling = new Refueling()
                .date(DEFAULT_DATE)
                .milage(DEFAULT_MILAGE)
                .trip(DEFAULT_TRIP)
                .quantity(DEFAULT_QUANTITY)
                .cost(DEFAULT_COST)
                .comment(DEFAULT_COMMENT);
        return refueling;
    }

    @Before
    public void initTest() {
        refueling = createEntity(em);
    }

    @Test
    @Transactional
    public void createRefueling() throws Exception {
        int databaseSizeBeforeCreate = refuelingRepository.findAll().size();

        // Create the Refueling

        restRefuelingMockMvc.perform(post("/api/refuelings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(refueling)))
                .andExpect(status().isCreated());

        // Validate the Refueling in the database
        List<Refueling> refuelings = refuelingRepository.findAll();
        assertThat(refuelings).hasSize(databaseSizeBeforeCreate + 1);
        Refueling testRefueling = refuelings.get(refuelings.size() - 1);
        assertThat(testRefueling.getDate()).isEqualTo(DEFAULT_DATE);
        assertThat(testRefueling.getMilage()).isEqualTo(DEFAULT_MILAGE);
        assertThat(testRefueling.getTrip()).isEqualTo(DEFAULT_TRIP);
        assertThat(testRefueling.getQuantity()).isEqualTo(DEFAULT_QUANTITY);
        assertThat(testRefueling.getCost()).isEqualTo(DEFAULT_COST);
        assertThat(testRefueling.getComment()).isEqualTo(DEFAULT_COMMENT);
    }

    @Test
    @Transactional
    public void getAllRefuelings() throws Exception {
        // Initialize the database
        refuelingRepository.saveAndFlush(refueling);

        // Get all the refuelings
        restRefuelingMockMvc.perform(get("/api/refuelings?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(refueling.getId().intValue())))
                .andExpect(jsonPath("$.[*].date").value(hasItem(DEFAULT_DATE_STR)))
                .andExpect(jsonPath("$.[*].milage").value(hasItem(DEFAULT_MILAGE.intValue())))
                .andExpect(jsonPath("$.[*].trip").value(hasItem(DEFAULT_TRIP.intValue())))
                .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY.intValue())))
                .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST.intValue())))
                .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())));
    }

    @Test
    @Transactional
    public void getRefueling() throws Exception {
        // Initialize the database
        refuelingRepository.saveAndFlush(refueling);

        // Get the refueling
        restRefuelingMockMvc.perform(get("/api/refuelings/{id}", refueling.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(refueling.getId().intValue()))
            .andExpect(jsonPath("$.date").value(DEFAULT_DATE_STR))
            .andExpect(jsonPath("$.milage").value(DEFAULT_MILAGE.intValue()))
            .andExpect(jsonPath("$.trip").value(DEFAULT_TRIP.intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY.intValue()))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST.intValue()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingRefueling() throws Exception {
        // Get the refueling
        restRefuelingMockMvc.perform(get("/api/refuelings/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRefueling() throws Exception {
        // Initialize the database
        refuelingService.save(refueling);

        int databaseSizeBeforeUpdate = refuelingRepository.findAll().size();

        // Update the refueling
        Refueling updatedRefueling = refuelingRepository.findOne(refueling.getId());
        updatedRefueling
                .date(UPDATED_DATE)
                .milage(UPDATED_MILAGE)
                .trip(UPDATED_TRIP)
                .quantity(UPDATED_QUANTITY)
                .cost(UPDATED_COST)
                .comment(UPDATED_COMMENT);

        restRefuelingMockMvc.perform(put("/api/refuelings")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedRefueling)))
                .andExpect(status().isOk());

        // Validate the Refueling in the database
        List<Refueling> refuelings = refuelingRepository.findAll();
        assertThat(refuelings).hasSize(databaseSizeBeforeUpdate);
        Refueling testRefueling = refuelings.get(refuelings.size() - 1);
        assertThat(testRefueling.getDate()).isEqualTo(UPDATED_DATE);
        assertThat(testRefueling.getMilage()).isEqualTo(UPDATED_MILAGE);
        assertThat(testRefueling.getTrip()).isEqualTo(UPDATED_TRIP);
        assertThat(testRefueling.getQuantity()).isEqualTo(UPDATED_QUANTITY);
        assertThat(testRefueling.getCost()).isEqualTo(UPDATED_COST);
        assertThat(testRefueling.getComment()).isEqualTo(UPDATED_COMMENT);
    }

    @Test
    @Transactional
    public void deleteRefueling() throws Exception {
        // Initialize the database
        refuelingService.save(refueling);

        int databaseSizeBeforeDelete = refuelingRepository.findAll().size();

        // Get the refueling
        restRefuelingMockMvc.perform(delete("/api/refuelings/{id}", refueling.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Refueling> refuelings = refuelingRepository.findAll();
        assertThat(refuelings).hasSize(databaseSizeBeforeDelete - 1);
    }
}
