package backend.microservice.com.web.rest;

import backend.microservice.com.domain.MachineRule;
import backend.microservice.com.repository.MachineRuleRepository;
import backend.microservice.com.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link backend.microservice.com.domain.MachineRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class MachineRuleResource {

    private final Logger log = LoggerFactory.getLogger(MachineRuleResource.class);

    private static final String ENTITY_NAME = "backendMachineRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MachineRuleRepository machineRuleRepository;

    public MachineRuleResource(MachineRuleRepository machineRuleRepository) {
        this.machineRuleRepository = machineRuleRepository;
    }

    /**
     * {@code POST  /machine-rules} : Create a new machineRule.
     *
     * @param machineRule the machineRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new machineRule, or with status {@code 400 (Bad Request)} if the machineRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/machine-rules")
    public ResponseEntity<MachineRule> createMachineRule(@RequestBody MachineRule machineRule) throws URISyntaxException {
        log.debug("REST request to save MachineRule : {}", machineRule);
        if (machineRule.getId() != null) {
            throw new BadRequestAlertException("A new machineRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MachineRule result = machineRuleRepository.save(machineRule);
        return ResponseEntity.created(new URI("/api/machine-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /machine-rules} : Updates an existing machineRule.
     *
     * @param machineRule the machineRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated machineRule,
     * or with status {@code 400 (Bad Request)} if the machineRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the machineRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/machine-rules")
    public ResponseEntity<MachineRule> updateMachineRule(@RequestBody MachineRule machineRule) throws URISyntaxException {
        log.debug("REST request to update MachineRule : {}", machineRule);
        if (machineRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MachineRule result = machineRuleRepository.save(machineRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, machineRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /machine-rules} : get all the machineRules.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of machineRules in body.
     */
    @GetMapping("/machine-rules")
    public List<MachineRule> getAllMachineRules() {
        log.debug("REST request to get all MachineRules");
        return machineRuleRepository.findAll();
    }

    /**
     * {@code GET  /machine-rules/:id} : get the "id" machineRule.
     *
     * @param id the id of the machineRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the machineRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/machine-rules/{id}")
    public ResponseEntity<MachineRule> getMachineRule(@PathVariable Long id) {
        log.debug("REST request to get MachineRule : {}", id);
        Optional<MachineRule> machineRule = machineRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(machineRule);
    }

    /**
     * {@code DELETE  /machine-rules/:id} : delete the "id" machineRule.
     *
     * @param id the id of the machineRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/machine-rules/{id}")
    public ResponseEntity<Void> deleteMachineRule(@PathVariable Long id) {
        log.debug("REST request to delete MachineRule : {}", id);
        machineRuleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
