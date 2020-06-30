package backend.microservice.com.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import backend.microservice.com.web.rest.TestUtil;

public class ResourceRuleTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceRule.class);
        ResourceRule resourceRule1 = new ResourceRule();
        resourceRule1.setId(1L);
        ResourceRule resourceRule2 = new ResourceRule();
        resourceRule2.setId(resourceRule1.getId());
        assertThat(resourceRule1).isEqualTo(resourceRule2);
        resourceRule2.setId(2L);
        assertThat(resourceRule1).isNotEqualTo(resourceRule2);
        resourceRule1.setId(null);
        assertThat(resourceRule1).isNotEqualTo(resourceRule2);
    }
}
