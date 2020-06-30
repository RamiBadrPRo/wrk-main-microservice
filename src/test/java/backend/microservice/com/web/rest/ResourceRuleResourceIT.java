package backend.microservice.com.web.rest;

import backend.microservice.com.BackendApp;
import backend.microservice.com.domain.ResourceRule;
import backend.microservice.com.repository.ResourceRuleRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ResourceRuleResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResourceRuleResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INPUT = false;
    private static final Boolean UPDATED_INPUT = true;

    private static final String DEFAULT_REF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REF_NAME = "BBBBBBBBBB";

    @Autowired
    private ResourceRuleRepository resourceRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceRuleMockMvc;

    private ResourceRule resourceRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceRule createEntity(EntityManager em) {
        ResourceRule resourceRule = new ResourceRule()
            .description(DEFAULT_DESCRIPTION)
            .input(DEFAULT_INPUT)
            .refName(DEFAULT_REF_NAME);
        return resourceRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceRule createUpdatedEntity(EntityManager em) {
        ResourceRule resourceRule = new ResourceRule()
            .description(UPDATED_DESCRIPTION)
            .input(UPDATED_INPUT)
            .refName(UPDATED_REF_NAME);
        return resourceRule;
    }

    @BeforeEach
    public void initTest() {
        resourceRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceRule() throws Exception {
        int databaseSizeBeforeCreate = resourceRuleRepository.findAll().size();
        // Create the ResourceRule
        restResourceRuleMockMvc.perform(post("/api/resource-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceRule)))
            .andExpect(status().isCreated());

        // Validate the ResourceRule in the database
        List<ResourceRule> resourceRuleList = resourceRuleRepository.findAll();
        assertThat(resourceRuleList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceRule testResourceRule = resourceRuleList.get(resourceRuleList.size() - 1);
        assertThat(testResourceRule.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testResourceRule.isInput()).isEqualTo(DEFAULT_INPUT);
        assertThat(testResourceRule.getRefName()).isEqualTo(DEFAULT_REF_NAME);
    }

    @Test
    @Transactional
    public void createResourceRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceRuleRepository.findAll().size();

        // Create the ResourceRule with an existing ID
        resourceRule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceRuleMockMvc.perform(post("/api/resource-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceRule)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceRule in the database
        List<ResourceRule> resourceRuleList = resourceRuleRepository.findAll();
        assertThat(resourceRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResourceRules() throws Exception {
        // Initialize the database
        resourceRuleRepository.saveAndFlush(resourceRule);

        // Get all the resourceRuleList
        restResourceRuleMockMvc.perform(get("/api/resource-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].input").value(hasItem(DEFAULT_INPUT.booleanValue())))
            .andExpect(jsonPath("$.[*].refName").value(hasItem(DEFAULT_REF_NAME)));
    }
    
    @Test
    @Transactional
    public void getResourceRule() throws Exception {
        // Initialize the database
        resourceRuleRepository.saveAndFlush(resourceRule);

        // Get the resourceRule
        restResourceRuleMockMvc.perform(get("/api/resource-rules/{id}", resourceRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceRule.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.input").value(DEFAULT_INPUT.booleanValue()))
            .andExpect(jsonPath("$.refName").value(DEFAULT_REF_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingResourceRule() throws Exception {
        // Get the resourceRule
        restResourceRuleMockMvc.perform(get("/api/resource-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceRule() throws Exception {
        // Initialize the database
        resourceRuleRepository.saveAndFlush(resourceRule);

        int databaseSizeBeforeUpdate = resourceRuleRepository.findAll().size();

        // Update the resourceRule
        ResourceRule updatedResourceRule = resourceRuleRepository.findById(resourceRule.getId()).get();
        // Disconnect from session so that the updates on updatedResourceRule are not directly saved in db
        em.detach(updatedResourceRule);
        updatedResourceRule
            .description(UPDATED_DESCRIPTION)
            .input(UPDATED_INPUT)
            .refName(UPDATED_REF_NAME);

        restResourceRuleMockMvc.perform(put("/api/resource-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedResourceRule)))
            .andExpect(status().isOk());

        // Validate the ResourceRule in the database
        List<ResourceRule> resourceRuleList = resourceRuleRepository.findAll();
        assertThat(resourceRuleList).hasSize(databaseSizeBeforeUpdate);
        ResourceRule testResourceRule = resourceRuleList.get(resourceRuleList.size() - 1);
        assertThat(testResourceRule.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testResourceRule.isInput()).isEqualTo(UPDATED_INPUT);
        assertThat(testResourceRule.getRefName()).isEqualTo(UPDATED_REF_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceRule() throws Exception {
        int databaseSizeBeforeUpdate = resourceRuleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceRuleMockMvc.perform(put("/api/resource-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceRule)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceRule in the database
        List<ResourceRule> resourceRuleList = resourceRuleRepository.findAll();
        assertThat(resourceRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResourceRule() throws Exception {
        // Initialize the database
        resourceRuleRepository.saveAndFlush(resourceRule);

        int databaseSizeBeforeDelete = resourceRuleRepository.findAll().size();

        // Delete the resourceRule
        restResourceRuleMockMvc.perform(delete("/api/resource-rules/{id}", resourceRule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceRule> resourceRuleList = resourceRuleRepository.findAll();
        assertThat(resourceRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
