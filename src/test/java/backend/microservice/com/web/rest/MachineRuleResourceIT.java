package backend.microservice.com.web.rest;

import backend.microservice.com.BackendApp;
import backend.microservice.com.domain.MachineRule;
import backend.microservice.com.repository.MachineRuleRepository;

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
 * Integration tests for the {@link MachineRuleResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MachineRuleResourceIT {

    private static final String DEFAULT_STATUS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS = "BBBBBBBBBB";

    private static final String DEFAULT_RESOURCE_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_RESOURCE_INPUT = "BBBBBBBBBB";

    private static final String DEFAULT_RULE_INPUT = "AAAAAAAAAA";
    private static final String UPDATED_RULE_INPUT = "BBBBBBBBBB";

    @Autowired
    private MachineRuleRepository machineRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineRuleMockMvc;

    private MachineRule machineRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineRule createEntity(EntityManager em) {
        MachineRule machineRule = new MachineRule()
            .status(DEFAULT_STATUS)
            .resourceInput(DEFAULT_RESOURCE_INPUT)
            .ruleInput(DEFAULT_RULE_INPUT);
        return machineRule;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MachineRule createUpdatedEntity(EntityManager em) {
        MachineRule machineRule = new MachineRule()
            .status(UPDATED_STATUS)
            .resourceInput(UPDATED_RESOURCE_INPUT)
            .ruleInput(UPDATED_RULE_INPUT);
        return machineRule;
    }

    @BeforeEach
    public void initTest() {
        machineRule = createEntity(em);
    }

    @Test
    @Transactional
    public void createMachineRule() throws Exception {
        int databaseSizeBeforeCreate = machineRuleRepository.findAll().size();
        // Create the MachineRule
        restMachineRuleMockMvc.perform(post("/api/machine-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machineRule)))
            .andExpect(status().isCreated());

        // Validate the MachineRule in the database
        List<MachineRule> machineRuleList = machineRuleRepository.findAll();
        assertThat(machineRuleList).hasSize(databaseSizeBeforeCreate + 1);
        MachineRule testMachineRule = machineRuleList.get(machineRuleList.size() - 1);
        assertThat(testMachineRule.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testMachineRule.getResourceInput()).isEqualTo(DEFAULT_RESOURCE_INPUT);
        assertThat(testMachineRule.getRuleInput()).isEqualTo(DEFAULT_RULE_INPUT);
    }

    @Test
    @Transactional
    public void createMachineRuleWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = machineRuleRepository.findAll().size();

        // Create the MachineRule with an existing ID
        machineRule.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineRuleMockMvc.perform(post("/api/machine-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machineRule)))
            .andExpect(status().isBadRequest());

        // Validate the MachineRule in the database
        List<MachineRule> machineRuleList = machineRuleRepository.findAll();
        assertThat(machineRuleList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMachineRules() throws Exception {
        // Initialize the database
        machineRuleRepository.saveAndFlush(machineRule);

        // Get all the machineRuleList
        restMachineRuleMockMvc.perform(get("/api/machine-rules?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machineRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS)))
            .andExpect(jsonPath("$.[*].resourceInput").value(hasItem(DEFAULT_RESOURCE_INPUT)))
            .andExpect(jsonPath("$.[*].ruleInput").value(hasItem(DEFAULT_RULE_INPUT)));
    }
    
    @Test
    @Transactional
    public void getMachineRule() throws Exception {
        // Initialize the database
        machineRuleRepository.saveAndFlush(machineRule);

        // Get the machineRule
        restMachineRuleMockMvc.perform(get("/api/machine-rules/{id}", machineRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machineRule.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS))
            .andExpect(jsonPath("$.resourceInput").value(DEFAULT_RESOURCE_INPUT))
            .andExpect(jsonPath("$.ruleInput").value(DEFAULT_RULE_INPUT));
    }
    @Test
    @Transactional
    public void getNonExistingMachineRule() throws Exception {
        // Get the machineRule
        restMachineRuleMockMvc.perform(get("/api/machine-rules/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMachineRule() throws Exception {
        // Initialize the database
        machineRuleRepository.saveAndFlush(machineRule);

        int databaseSizeBeforeUpdate = machineRuleRepository.findAll().size();

        // Update the machineRule
        MachineRule updatedMachineRule = machineRuleRepository.findById(machineRule.getId()).get();
        // Disconnect from session so that the updates on updatedMachineRule are not directly saved in db
        em.detach(updatedMachineRule);
        updatedMachineRule
            .status(UPDATED_STATUS)
            .resourceInput(UPDATED_RESOURCE_INPUT)
            .ruleInput(UPDATED_RULE_INPUT);

        restMachineRuleMockMvc.perform(put("/api/machine-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMachineRule)))
            .andExpect(status().isOk());

        // Validate the MachineRule in the database
        List<MachineRule> machineRuleList = machineRuleRepository.findAll();
        assertThat(machineRuleList).hasSize(databaseSizeBeforeUpdate);
        MachineRule testMachineRule = machineRuleList.get(machineRuleList.size() - 1);
        assertThat(testMachineRule.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testMachineRule.getResourceInput()).isEqualTo(UPDATED_RESOURCE_INPUT);
        assertThat(testMachineRule.getRuleInput()).isEqualTo(UPDATED_RULE_INPUT);
    }

    @Test
    @Transactional
    public void updateNonExistingMachineRule() throws Exception {
        int databaseSizeBeforeUpdate = machineRuleRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineRuleMockMvc.perform(put("/api/machine-rules")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machineRule)))
            .andExpect(status().isBadRequest());

        // Validate the MachineRule in the database
        List<MachineRule> machineRuleList = machineRuleRepository.findAll();
        assertThat(machineRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMachineRule() throws Exception {
        // Initialize the database
        machineRuleRepository.saveAndFlush(machineRule);

        int databaseSizeBeforeDelete = machineRuleRepository.findAll().size();

        // Delete the machineRule
        restMachineRuleMockMvc.perform(delete("/api/machine-rules/{id}", machineRule.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<MachineRule> machineRuleList = machineRuleRepository.findAll();
        assertThat(machineRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
