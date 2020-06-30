package backend.microservice.com.web.rest;

import backend.microservice.com.BackendApp;
import backend.microservice.com.domain.Machine;
import backend.microservice.com.repository.MachineRepository;

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
 * Integration tests for the {@link MachineResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class MachineResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_IP_ADDR = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDR = "BBBBBBBBBB";

    private static final String DEFAULT_O_S = "AAAAAAAAAA";
    private static final String UPDATED_O_S = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_USERNAME = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_USERNAME = "BBBBBBBBBB";

    private static final String DEFAULT_ACCESS_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_ACCESS_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private MachineRepository machineRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMachineMockMvc;

    private Machine machine;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createEntity(EntityManager em) {
        Machine machine = new Machine()
            .name(DEFAULT_NAME)
            .ipAddr(DEFAULT_IP_ADDR)
            .oS(DEFAULT_O_S)
            .accessUsername(DEFAULT_ACCESS_USERNAME)
            .accessPassword(DEFAULT_ACCESS_PASSWORD);
        return machine;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Machine createUpdatedEntity(EntityManager em) {
        Machine machine = new Machine()
            .name(UPDATED_NAME)
            .ipAddr(UPDATED_IP_ADDR)
            .oS(UPDATED_O_S)
            .accessUsername(UPDATED_ACCESS_USERNAME)
            .accessPassword(UPDATED_ACCESS_PASSWORD);
        return machine;
    }

    @BeforeEach
    public void initTest() {
        machine = createEntity(em);
    }

    @Test
    @Transactional
    public void createMachine() throws Exception {
        int databaseSizeBeforeCreate = machineRepository.findAll().size();
        // Create the Machine
        restMachineMockMvc.perform(post("/api/machines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isCreated());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate + 1);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMachine.getIpAddr()).isEqualTo(DEFAULT_IP_ADDR);
        assertThat(testMachine.getoS()).isEqualTo(DEFAULT_O_S);
        assertThat(testMachine.getAccessUsername()).isEqualTo(DEFAULT_ACCESS_USERNAME);
        assertThat(testMachine.getAccessPassword()).isEqualTo(DEFAULT_ACCESS_PASSWORD);
    }

    @Test
    @Transactional
    public void createMachineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = machineRepository.findAll().size();

        // Create the Machine with an existing ID
        machine.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMachineMockMvc.perform(post("/api/machines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllMachines() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get all the machineList
        restMachineMockMvc.perform(get("/api/machines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(machine.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].ipAddr").value(hasItem(DEFAULT_IP_ADDR)))
            .andExpect(jsonPath("$.[*].oS").value(hasItem(DEFAULT_O_S)))
            .andExpect(jsonPath("$.[*].accessUsername").value(hasItem(DEFAULT_ACCESS_USERNAME)))
            .andExpect(jsonPath("$.[*].accessPassword").value(hasItem(DEFAULT_ACCESS_PASSWORD)));
    }
    
    @Test
    @Transactional
    public void getMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        // Get the machine
        restMachineMockMvc.perform(get("/api/machines/{id}", machine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(machine.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.ipAddr").value(DEFAULT_IP_ADDR))
            .andExpect(jsonPath("$.oS").value(DEFAULT_O_S))
            .andExpect(jsonPath("$.accessUsername").value(DEFAULT_ACCESS_USERNAME))
            .andExpect(jsonPath("$.accessPassword").value(DEFAULT_ACCESS_PASSWORD));
    }
    @Test
    @Transactional
    public void getNonExistingMachine() throws Exception {
        // Get the machine
        restMachineMockMvc.perform(get("/api/machines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // Update the machine
        Machine updatedMachine = machineRepository.findById(machine.getId()).get();
        // Disconnect from session so that the updates on updatedMachine are not directly saved in db
        em.detach(updatedMachine);
        updatedMachine
            .name(UPDATED_NAME)
            .ipAddr(UPDATED_IP_ADDR)
            .oS(UPDATED_O_S)
            .accessUsername(UPDATED_ACCESS_USERNAME)
            .accessPassword(UPDATED_ACCESS_PASSWORD);

        restMachineMockMvc.perform(put("/api/machines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMachine)))
            .andExpect(status().isOk());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
        Machine testMachine = machineList.get(machineList.size() - 1);
        assertThat(testMachine.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMachine.getIpAddr()).isEqualTo(UPDATED_IP_ADDR);
        assertThat(testMachine.getoS()).isEqualTo(UPDATED_O_S);
        assertThat(testMachine.getAccessUsername()).isEqualTo(UPDATED_ACCESS_USERNAME);
        assertThat(testMachine.getAccessPassword()).isEqualTo(UPDATED_ACCESS_PASSWORD);
    }

    @Test
    @Transactional
    public void updateNonExistingMachine() throws Exception {
        int databaseSizeBeforeUpdate = machineRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMachineMockMvc.perform(put("/api/machines")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(machine)))
            .andExpect(status().isBadRequest());

        // Validate the Machine in the database
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMachine() throws Exception {
        // Initialize the database
        machineRepository.saveAndFlush(machine);

        int databaseSizeBeforeDelete = machineRepository.findAll().size();

        // Delete the machine
        restMachineMockMvc.perform(delete("/api/machines/{id}", machine.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Machine> machineList = machineRepository.findAll();
        assertThat(machineList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
