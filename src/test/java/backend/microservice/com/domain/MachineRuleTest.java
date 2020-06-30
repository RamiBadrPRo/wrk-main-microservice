package backend.microservice.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import backend.microservice.com.web.rest.TestUtil;

public class MachineRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(MachineRule.class);
        MachineRule machineRule1 = new MachineRule();
        machineRule1.setId(1L);
        MachineRule machineRule2 = new MachineRule();
        machineRule2.setId(machineRule1.getId());
        assertThat(machineRule1).isEqualTo(machineRule2);
        machineRule2.setId(2L);
        assertThat(machineRule1).isNotEqualTo(machineRule2);
        machineRule1.setId(null);
        assertThat(machineRule1).isNotEqualTo(machineRule2);
    }
}
