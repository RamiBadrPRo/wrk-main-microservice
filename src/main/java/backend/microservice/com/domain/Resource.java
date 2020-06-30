package backend.microservice.com.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Resource.
 */
@Entity
@Table(name = "resource")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Resource implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "inspec_code")
    private String inspecCode;

    @OneToMany(mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ResourceRule> resourceRules = new HashSet<>();

    @OneToMany(mappedBy = "resource")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<ResourceInput> resourceInputs = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Resource name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public Resource description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getInspecCode() {
        return inspecCode;
    }

    public Resource inspecCode(String inspecCode) {
        this.inspecCode = inspecCode;
        return this;
    }

    public void setInspecCode(String inspecCode) {
        this.inspecCode = inspecCode;
    }

    public Set<ResourceRule> getResourceRules() {
        return resourceRules;
    }

    public Resource resourceRules(Set<ResourceRule> resourceRules) {
        this.resourceRules = resourceRules;
        return this;
    }

    public Resource addResourceRule(ResourceRule resourceRule) {
        this.resourceRules.add(resourceRule);
        resourceRule.setResource(this);
        return this;
    }

    public Resource removeResourceRule(ResourceRule resourceRule) {
        this.resourceRules.remove(resourceRule);
        resourceRule.setResource(null);
        return this;
    }

    public void setResourceRules(Set<ResourceRule> resourceRules) {
        this.resourceRules = resourceRules;
    }

    public Set<ResourceInput> getResourceInputs() {
        return resourceInputs;
    }

    public Resource resourceInputs(Set<ResourceInput> resourceInputs) {
        this.resourceInputs = resourceInputs;
        return this;
    }

    public Resource addResourceInput(ResourceInput resourceInput) {
        this.resourceInputs.add(resourceInput);
        resourceInput.setResource(this);
        return this;
    }

    public Resource removeResourceInput(ResourceInput resourceInput) {
        this.resourceInputs.remove(resourceInput);
        resourceInput.setResource(null);
        return this;
    }

    public void setResourceInputs(Set<ResourceInput> resourceInputs) {
        this.resourceInputs = resourceInputs;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Resource)) {
            return false;
        }
        return id != null && id.equals(((Resource) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Resource{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", inspecCode='" + getInspecCode() + "'" +
            "}";
    }
}
