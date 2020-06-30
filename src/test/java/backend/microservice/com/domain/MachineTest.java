package backend.microservice.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import backend.microservice.com.web.rest.TestUtil;

public class MachineTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Machine.class);
        Machine machine1 = new Machine();
        machine1.setId(1L);
        Machine machine2 = new Machine();
        machine2.setId(machine1.getId());
        assertThat(machine1).isEqualTo(machine2);
        machine2.setId(2L);
        assertThat(machine1).isNotEqualTo(machine2);
        machine1.setId(null);
        assertThat(machine1).isNotEqualTo(machine2);
    }
}
