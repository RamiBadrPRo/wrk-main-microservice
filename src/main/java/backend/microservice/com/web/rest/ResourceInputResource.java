package backend.microservice.com.web.rest;

import backend.microservice.com.domain.ResourceInput;
import backend.microservice.com.repository.ResourceInputRepository;
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
 * REST controller for managing {@link backend.microservice.com.domain.ResourceInput}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ResourceInputResource {

    private final Logger log = LoggerFactory.getLogger(ResourceInputResource.class);

    private static final String ENTITY_NAME = "backendResourceInput";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ResourceInputRepository resourceInputRepository;

    public ResourceInputResource(ResourceInputRepository resourceInputRepository) {
        this.resourceInputRepository = resourceInputRepository;
    }

    /**
     * {@code POST  /resource-inputs} : Create a new resourceInput.
     *
     * @param resourceInput the resourceInput to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new resourceInput, or with status {@code 400 (Bad Request)} if the resourceInput has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/resource-inputs")
    public ResponseEntity<ResourceInput> createResourceInput(@RequestBody ResourceInput resourceInput) throws URISyntaxException {
        log.debug("REST request to save ResourceInput : {}", resourceInput);
        if (resourceInput.getId() != null) {
            throw new BadRequestAlertException("A new resourceInput cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ResourceInput result = resourceInputRepository.save(resourceInput);
        return ResponseEntity.created(new URI("/api/resource-inputs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /resource-inputs} : Updates an existing resourceInput.
     *
     * @param resourceInput the resourceInput to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated resourceInput,
     * or with status {@code 400 (Bad Request)} if the resourceInput is not valid,
     * or with status {@code 500 (Internal Server Error)} if the resourceInput couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/resource-inputs")
    public ResponseEntity<ResourceInput> updateResourceInput(@RequestBody ResourceInput resourceInput) throws URISyntaxException {
        log.debug("REST request to update ResourceInput : {}", resourceInput);
        if (resourceInput.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ResourceInput result = resourceInputRepository.save(resourceInput);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, resourceInput.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /resource-inputs} : get all the resourceInputs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of resourceInputs in body.
     */
    @GetMapping("/resource-inputs")
    public List<ResourceInput> getAllResourceInputs() {
        log.debug("REST request to get all ResourceInputs");
        return resourceInputRepository.findAll();
    }

    /**
     * {@code GET  /resource-inputs/:id} : get the "id" resourceInput.
     *
     * @param id the id of the resourceInput to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the resourceInput, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/resource-inputs/{id}")
    public ResponseEntity<ResourceInput> getResourceInput(@PathVariable Long id) {
        log.debug("REST request to get ResourceInput : {}", id);
        Optional<ResourceInput> resourceInput = resourceInputRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(resourceInput);
    }

    /**
     * {@code DELETE  /resource-inputs/:id} : delete the "id" resourceInput.
     *
     * @param id the id of the resourceInput to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/resource-inputs/{id}")
    public ResponseEntity<Void> deleteResourceInput(@PathVariable Long id) {
        log.debug("REST request to delete ResourceInput : {}", id);
        resourceInputRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
