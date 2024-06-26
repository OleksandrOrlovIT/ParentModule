package orlov.oleksandr.programming.citycountryemailsender;

import config.TestElasticsearchConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@ContextConfiguration(classes = {CityCountryEmailSenderApplication.class, TestElasticsearchConfiguration.class})
class CityCountryEmailSenderApplicationTests {

    @Test
    void contextLoads() {
    }

}
