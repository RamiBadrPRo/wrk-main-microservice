package backend.microservice.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ResourceRule.
 */
@Entity
@Table(name = "resource_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResourceRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "input")
    private Boolean input;

    @Column(name = "ref_name")
    private String refName;

    @OneToOne(mappedBy = "resourceRule")
    @JsonIgnore
    private MachineRule machineRule;

    @ManyToOne
    @JsonIgnoreProperties(value = "resourceRules", allowSetters = true)
    private Resource resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public ResourceRule description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean isInput() {
        return input;
    }

    public ResourceRule input(Boolean input) {
        this.input = input;
        return this;
    }

    public void setInput(Boolean input) {
        this.input = input;
    }

    public String getRefName() {
        return refName;
    }

    public ResourceRule refName(String refName) {
        this.refName = refName;
        return this;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public MachineRule getMachineRule() {
        return machineRule;
    }

    public ResourceRule machineRule(MachineRule machineRule) {
        this.machineRule = machineRule;
        return this;
    }

    public void setMachineRule(MachineRule machineRule) {
        this.machineRule = machineRule;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceRule resource(Resource resource) {
        this.resource = resource;
        return this;
    }

    public void setResource(Resource resource) {
        this.resource = resource;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceRule)) {
            return false;
        }
        return id != null && id.equals(((ResourceRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceRule{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", input='" + isInput() + "'" +
            ", refName='" + getRefName() + "'" +
            "}";
    }
}
