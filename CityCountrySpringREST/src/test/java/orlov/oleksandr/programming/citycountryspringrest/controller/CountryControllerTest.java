package orlov.oleksandr.programming.citycountryspringrest.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import orlov.oleksandr.programming.citycountryspringrest.configuration.EnvConfig;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CountryDTO;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;
import orlov.oleksandr.programming.citycountryspringrest.repository.CountryRepository;
import orlov.oleksandr.programming.citycountryspringrest.service.messages.impl.RabbitMQMessageSender;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CountryControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withNetwork(Network.SHARED);

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private CountryRepository countryRepository;

    @MockBean
    RabbitMQMessageSender rabbitMQMessageSender;

    @MockBean
    EnvConfig envConfig;

    @Test
    void shouldFindZeroCountries() {
        Country[] countries = restTemplate.getForObject("/api/country", Country[].class);
        assertEquals(0, countries.length);
    }

    @Test
    void shouldFindOneCountry() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Country[] countries = restTemplate.getForObject("/api/country", Country[].class);
        assertEquals(1, countries.length);
    }

    @Test
    void shouldFindTenCountries() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        for (int i = 0; i < 10; i++) {
            countryDTO.setCountryName(countryDTO.getCountryName() + i);
            ResponseEntity<Country> response =
                    restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }

        Country[] countries = restTemplate.getForObject("/api/country", Country[].class);
        assertEquals(10, countries.length);
    }

    @Test
    void shouldGetBadRequest_forCreatingWithAllInvalidFields() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("")
                .countryArea(-1.0)
                .currency("")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forCreatingWithOneInvalidFields() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateValidCountry() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        assertEquals(countryDTO.getCountryName(), Objects.requireNonNull(response.getBody()).getCountryName());
        assertEquals(countryDTO.getCountryArea(), Objects.requireNonNull(response.getBody()).getCountryArea());
        assertEquals(countryDTO.getCurrency(), Objects.requireNonNull(response.getBody()).getCurrency());
    }

    @Test
    void shouldGetBadRequest_forCreatingDuplicateName() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetNotFound_forUpdating_WithoutEntityInDB() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country/" + 1,
                        HttpMethod.PUT, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forUpdating_WithAllInvalidFields() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        countryDTO.setCountryName("");
        countryDTO.setCountryArea(-1.0);
        countryDTO.setCurrency("");

        response = restTemplate.exchange("/api/country/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forUpdating_WithOneInvalidField() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        countryDTO.setCountryName("");

        response = restTemplate.exchange("/api/country/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldSuccessfullyUpdate() {
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        countryDTO.setCountryName(countryDTO.getCountryName() + 1);
        countryDTO.setCountryArea(countryDTO.getCountryArea() + 1);
        countryDTO.setCurrency(countryDTO.getCurrency() + 1);

        response = restTemplate.exchange("/api/country/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(countryDTO), Country.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(countryDTO.getCountryName(), Objects.requireNonNull(response.getBody()).getCountryName());
        assertEquals(countryDTO.getCountryArea(), Objects.requireNonNull(response.getBody()).getCountryArea());
        assertEquals(countryDTO.getCurrency(), Objects.requireNonNull(response.getBody()).getCurrency());
    }

    @Test
    void shouldReturnBadRequest_ForDuplicates() {
        CountryDTO countryDTO1 = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        CountryDTO countryDTO2 = CountryDTO.builder()
                .countryName("SomeState2")
                .countryArea(2.0)
                .currency("Currency2")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO1), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Long savedId = Objects.requireNonNull(response.getBody()).getId();

        response = restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO2), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restTemplate.exchange("/api/country/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(countryDTO2), Country.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteNothing_ReturnsOK(){
        ResponseEntity<Void> response =
            restTemplate.exchange("/api/country/1", HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteValidId_ReturnsOK(){
        CountryDTO countryDTO = CountryDTO.builder()
                .countryName("SomeState")
                .countryArea(1.0)
                .currency("Currency")
                .build();

        ResponseEntity<Country> response =
                restTemplate.exchange("/api/country", HttpMethod.POST, new HttpEntity<>(countryDTO), Country.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<Void> responseVoid =
                restTemplate.exchange("/api/country/" + Objects.requireNonNull(response.getBody()).getId(),
                        HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, responseVoid.getStatusCode());

        Country[] countries = restTemplate.getForObject("/api/country", Country[].class);
        assertEquals(0, countries.length);
    }

    @AfterEach
    public void cleanUp() {
        List<Country> countries = countryRepository.findAll();
        if(!countries.isEmpty()) {
            countryRepository.deleteAll(countries);
        }
    }
}