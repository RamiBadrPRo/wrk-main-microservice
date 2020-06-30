package backend.microservice.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A MachineRule.
 */
@Entity
@Table(name = "machine_rule")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class MachineRule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    private String status;

    @Column(name = "resource_input")
    private String resourceInput;

    @Column(name = "rule_input")
    private String ruleInput;

    @OneToOne
    @JoinColumn(unique = true)
    private Machine machine;

    @OneToOne
    @JoinColumn(unique = true)
    private ResourceRule resourceRule;

    @ManyToOne
    @JsonIgnoreProperties(value = "machineRules", allowSetters = true)
    private Machine machine;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public MachineRule status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResourceInput() {
        return resourceInput;
    }

    public MachineRule resourceInput(String resourceInput) {
        this.resourceInput = resourceInput;
        return this;
    }

    public void setResourceInput(String resourceInput) {
        this.resourceInput = resourceInput;
    }

    public String getRuleInput() {
        return ruleInput;
    }

    public MachineRule ruleInput(String ruleInput) {
        this.ruleInput = ruleInput;
        return this;
    }

    public void setRuleInput(String ruleInput) {
        this.ruleInput = ruleInput;
    }

    public Machine getMachine() {
        return machine;
    }

    public MachineRule machine(Machine machine) {
        this.machine = machine;
        return this;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }

    public ResourceRule getResourceRule() {
        return resourceRule;
    }

    public MachineRule resourceRule(ResourceRule resourceRule) {
        this.resourceRule = resourceRule;
        return this;
    }

    public void setResourceRule(ResourceRule resourceRule) {
        this.resourceRule = resourceRule;
    }

    public Machine getMachine() {
        return machine;
    }

    public MachineRule machine(Machine machine) {
        this.machine = machine;
        return this;
    }

    public void setMachine(Machine machine) {
        this.machine = machine;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MachineRule)) {
            return false;
        }
        return id != null && id.equals(((MachineRule) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MachineRule{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", resourceInput='" + getResourceInput() + "'" +
            ", ruleInput='" + getRuleInput() + "'" +
            "}";
    }
}
