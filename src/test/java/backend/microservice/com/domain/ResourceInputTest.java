package backend.microservice.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import backend.microservice.com.web.rest.TestUtil;

public class ResourceInputTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceInput.class);
        ResourceInput resourceInput1 = new ResourceInput();
        resourceInput1.setId(1L);
        ResourceInput resourceInput2 = new ResourceInput();
        resourceInput2.setId(resourceInput1.getId());
        assertThat(resourceInput1).isEqualTo(resourceInput2);
        resourceInput2.setId(2L);
        assertThat(resourceInput1).isNotEqualTo(resourceInput2);
        resourceInput1.setId(null);
        assertThat(resourceInput1).isNotEqualTo(resourceInput2);
    }
}
