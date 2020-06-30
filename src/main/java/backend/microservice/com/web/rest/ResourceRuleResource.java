package backend.microservice.com.web.rest;

import backend.microservice.com.domain.ResourceRule;
import backend.microservice.com.repository.ResourceRuleRepository;
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
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link backend.microservice.com.domain.ResourceRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResourceRuleResource {

    private final Logger log = LoggerFactory.getLogger(ResourceRuleResource.class);

    private static final String ENTITY_NAME = "backendResourceRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceRuleRepository resourceRuleRepository;

    public ResourceRuleResource(ResourceRuleRepository resourceRuleRepository) {
        this.resourceRuleRepository = resourceRuleRepository;
    }

    /**
     * {@code POST  /resource-rules} : Create a new resourceRule.
     *
     * @param resourceRule the resourceRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceRule, or with status {@code 400 (Bad Request)} if the resourceRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-rules")
    public ResponseEntity<ResourceRule> createResourceRule(@RequestBody ResourceRule resourceRule) throws URISyntaxException {
        log.debug("REST request to save ResourceRule : {}", resourceRule);
        if (resourceRule.getId() != null) {
            throw new BadRequestAlertException("A new resourceRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceRule result = resourceRuleRepository.save(resourceRule);
        return ResponseEntity.created(new URI("/api/resource-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-rules} : Updates an existing resourceRule.
     *
     * @param resourceRule the resourceRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceRule,
     * or with status {@code 400 (Bad Request)} if the resourceRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-rules")
    public ResponseEntity<ResourceRule> updateResourceRule(@RequestBody ResourceRule resourceRule) throws URISyntaxException {
        log.debug("REST request to update ResourceRule : {}", resourceRule);
        if (resourceRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceRule result = resourceRuleRepository.save(resourceRule);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resourceRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-rules} : get all the resourceRules.
     *
     * @param filter the filter of the request.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceRules in body.
     */
    @GetMapping("/resource-rules")
    public List<ResourceRule> getAllResourceRules(@RequestParam(required = false) String filter) {
        if ("machinerule-is-null".equals(filter)) {
            log.debug("REST request to get all ResourceRules where machineRule is null");
            return StreamSupport
                .stream(resourceRuleRepository.findAll().spliterator(), false)
                .filter(resourceRule -> resourceRule.getMachineRule() == null)
                .collect(Collectors.toList());
        }
        log.debug("REST request to get all ResourceRules");
        return resourceRuleRepository.findAll();
    }

    /**
     * {@code GET  /resource-rules/:id} : get the "id" resourceRule.
     *
     * @param id the id of the resourceRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-rules/{id}")
    public ResponseEntity<ResourceRule> getResourceRule(@PathVariable Long id) {
        log.debug("REST request to get ResourceRule : {}", id);
        Optional<ResourceRule> resourceRule = resourceRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resourceRule);
    }

    /**
     * {@code DELETE  /resource-rules/:id} : delete the "id" resourceRule.
     *
     * @param id the id of the resourceRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-rules/{id}")
    public ResponseEntity<Void> deleteResourceRule(@PathVariable Long id) {
        log.debug("REST request to delete ResourceRule : {}", id);
        resourceRuleRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
