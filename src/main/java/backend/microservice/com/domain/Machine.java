package backend.microservice.com.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Machine.
 */
@Entity
@Table(name = "machine")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Machine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "ip_addr")
    private String ipAddr;

    @Column(name = "o_s")
    private String oS;

    @Column(name = "access_username")
    private String accessUsername;

    @Column(name = "access_password")
    private String accessPassword;

    @OneToMany(mappedBy = "machine")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<MachineRule> machineRules = new HashSet<>();

    @OneToOne(mappedBy = "machine")
    @JsonIgnore
    private MachineRule machineRule;

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

    public Machine name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public Machine ipAddr(String ipAddr) {
        this.ipAddr = ipAddr;
        return this;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public String getoS() {
        return oS;
    }

    public Machine oS(String oS) {
        this.oS = oS;
        return this;
    }

    public void setoS(String oS) {
        this.oS = oS;
    }

    public String getAccessUsername() {
        return accessUsername;
    }

    public Machine accessUsername(String accessUsername) {
        this.accessUsername = accessUsername;
        return this;
    }

    public void setAccessUsername(String accessUsername) {
        this.accessUsername = accessUsername;
    }

    public String getAccessPassword() {
        return accessPassword;
    }

    public Machine accessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
        return this;
    }

    public void setAccessPassword(String accessPassword) {
        this.accessPassword = accessPassword;
    }

    public Set<MachineRule> getMachineRules() {
        return machineRules;
    }

    public Machine machineRules(Set<MachineRule> machineRules) {
        this.machineRules = machineRules;
        return this;
    }

    public Machine addMachineRule(MachineRule machineRule) {
        this.machineRules.add(machineRule);
        machineRule.setMachine(this);
        return this;
    }

    public Machine removeMachineRule(MachineRule machineRule) {
        this.machineRules.remove(machineRule);
        machineRule.setMachine(null);
        return this;
    }

    public void setMachineRules(Set<MachineRule> machineRules) {
        this.machineRules = machineRules;
    }

    public MachineRule getMachineRule() {
        return machineRule;
    }

    public Machine machineRule(MachineRule machineRule) {
        this.machineRule = machineRule;
        return this;
    }

    public void setMachineRule(MachineRule machineRule) {
        this.machineRule = machineRule;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Machine)) {
            return false;
        }
        return id != null && id.equals(((Machine) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Machine{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", ipAddr='" + getIpAddr() + "'" +
            ", oS='" + getoS() + "'" +
            ", accessUsername='" + getAccessUsername() + "'" +
            ", accessPassword='" + getAccessPassword() + "'" +
            "}";
    }
}
