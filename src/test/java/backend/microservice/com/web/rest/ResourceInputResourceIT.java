package backend.microservice.com.web.rest;

import backend.microservice.com.BackendApp;
import backend.microservice.com.domain.ResourceInput;
import backend.microservice.com.repository.ResourceInputRepository;

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
 * Integration tests for the {@link ResourceInputResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ResourceInputResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_REF_NAME = "AAAAAAAAAA";
    private static final String UPDATED_REF_NAME = "BBBBBBBBBB";

    @Autowired
    private ResourceInputRepository resourceInputRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restResourceInputMockMvc;

    private ResourceInput resourceInput;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceInput createEntity(EntityManager em) {
        ResourceInput resourceInput = new ResourceInput()
            .title(DEFAULT_TITLE)
            .refName(DEFAULT_REF_NAME);
        return resourceInput;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ResourceInput createUpdatedEntity(EntityManager em) {
        ResourceInput resourceInput = new ResourceInput()
            .title(UPDATED_TITLE)
            .refName(UPDATED_REF_NAME);
        return resourceInput;
    }

    @BeforeEach
    public void initTest() {
        resourceInput = createEntity(em);
    }

    @Test
    @Transactional
    public void createResourceInput() throws Exception {
        int databaseSizeBeforeCreate = resourceInputRepository.findAll().size();
        // Create the ResourceInput
        restResourceInputMockMvc.perform(post("/api/resource-inputs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceInput)))
            .andExpect(status().isCreated());

        // Validate the ResourceInput in the database
        List<ResourceInput> resourceInputList = resourceInputRepository.findAll();
        assertThat(resourceInputList).hasSize(databaseSizeBeforeCreate + 1);
        ResourceInput testResourceInput = resourceInputList.get(resourceInputList.size() - 1);
        assertThat(testResourceInput.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testResourceInput.getRefName()).isEqualTo(DEFAULT_REF_NAME);
    }

    @Test
    @Transactional
    public void createResourceInputWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = resourceInputRepository.findAll().size();

        // Create the ResourceInput with an existing ID
        resourceInput.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restResourceInputMockMvc.perform(post("/api/resource-inputs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceInput)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceInput in the database
        List<ResourceInput> resourceInputList = resourceInputRepository.findAll();
        assertThat(resourceInputList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllResourceInputs() throws Exception {
        // Initialize the database
        resourceInputRepository.saveAndFlush(resourceInput);

        // Get all the resourceInputList
        restResourceInputMockMvc.perform(get("/api/resource-inputs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(resourceInput.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].refName").value(hasItem(DEFAULT_REF_NAME)));
    }
    
    @Test
    @Transactional
    public void getResourceInput() throws Exception {
        // Initialize the database
        resourceInputRepository.saveAndFlush(resourceInput);

        // Get the resourceInput
        restResourceInputMockMvc.perform(get("/api/resource-inputs/{id}", resourceInput.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(resourceInput.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.refName").value(DEFAULT_REF_NAME));
    }
    @Test
    @Transactional
    public void getNonExistingResourceInput() throws Exception {
        // Get the resourceInput
        restResourceInputMockMvc.perform(get("/api/resource-inputs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateResourceInput() throws Exception {
        // Initialize the database
        resourceInputRepository.saveAndFlush(resourceInput);

        int databaseSizeBeforeUpdate = resourceInputRepository.findAll().size();

        // Update the resourceInput
        ResourceInput updatedResourceInput = resourceInputRepository.findById(resourceInput.getId()).get();
        // Disconnect from session so that the updates on updatedResourceInput are not directly saved in db
        em.detach(updatedResourceInput);
        updatedResourceInput
            .title(UPDATED_TITLE)
            .refName(UPDATED_REF_NAME);

        restResourceInputMockMvc.perform(put("/api/resource-inputs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedResourceInput)))
            .andExpect(status().isOk());

        // Validate the ResourceInput in the database
        List<ResourceInput> resourceInputList = resourceInputRepository.findAll();
        assertThat(resourceInputList).hasSize(databaseSizeBeforeUpdate);
        ResourceInput testResourceInput = resourceInputList.get(resourceInputList.size() - 1);
        assertThat(testResourceInput.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testResourceInput.getRefName()).isEqualTo(UPDATED_REF_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingResourceInput() throws Exception {
        int databaseSizeBeforeUpdate = resourceInputRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restResourceInputMockMvc.perform(put("/api/resource-inputs")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(resourceInput)))
            .andExpect(status().isBadRequest());

        // Validate the ResourceInput in the database
        List<ResourceInput> resourceInputList = resourceInputRepository.findAll();
        assertThat(resourceInputList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteResourceInput() throws Exception {
        // Initialize the database
        resourceInputRepository.saveAndFlush(resourceInput);

        int databaseSizeBeforeDelete = resourceInputRepository.findAll().size();

        // Delete the resourceInput
        restResourceInputMockMvc.perform(delete("/api/resource-inputs/{id}", resourceInput.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ResourceInput> resourceInputList = resourceInputRepository.findAll();
        assertThat(resourceInputList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
