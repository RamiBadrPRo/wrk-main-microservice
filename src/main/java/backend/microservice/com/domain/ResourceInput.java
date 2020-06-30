package backend.microservice.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A ResourceInput.
 */
@Entity
@Table(name = "resource_input")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ResourceInput implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "ref_name")
    private String refName;

    @ManyToOne
    @JsonIgnoreProperties(value = "resourceInputs", allowSetters = true)
    private Resource resource;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public ResourceInput title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRefName() {
        return refName;
    }

    public ResourceInput refName(String refName) {
        this.refName = refName;
        return this;
    }

    public void setRefName(String refName) {
        this.refName = refName;
    }

    public Resource getResource() {
        return resource;
    }

    public ResourceInput resource(Resource resource) {
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
        if (!(o instanceof ResourceInput)) {
            return false;
        }
        return id != null && id.equals(((ResourceInput) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceInput{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", refName='" + getRefName() + "'" +
            "}";
    }
}
