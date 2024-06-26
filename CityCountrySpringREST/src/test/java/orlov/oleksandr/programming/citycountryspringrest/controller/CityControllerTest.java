package orlov.oleksandr.programming.citycountryspringrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.testcontainers.containers.Network;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import orlov.oleksandr.programming.citycountryspringrest.configuration.EnvConfig;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CityDTO;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityCRUDResponse;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityFilteredResponse;
import orlov.oleksandr.programming.citycountryspringrest.model.City;
import orlov.oleksandr.programming.citycountryspringrest.repository.CityRepository;
import orlov.oleksandr.programming.citycountryspringrest.service.messages.impl.RabbitMQMessageSender;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Year;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test_city")
@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CityControllerTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16.0")
            .withNetwork(Network.SHARED);

    @Autowired
    TestRestTemplate restTemplate;

    @Autowired
    private CityRepository cityRepository;

    @MockBean
    RabbitMQMessageSender messageSender;

    @MockBean
    EnvConfig envConfig;

    @Test
    void shouldGetBadRequest_forCreatingWithAllInvalidFields() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("");
        cityDTO.setCountryId(-1L);
        cityDTO.setCityPopulation(-1);
        cityDTO.setCityArea(-1.0);
        cityDTO.setFoundedAt(Year.now().plusYears(1));
        cityDTO.setLanguages("");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forCreatingWithOneInvalidFields() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        //invalid population
        cityDTO.setCityPopulation(-1);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldCreateValidCountry() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        assertEquals(cityDTO.getCountryId(), Objects.requireNonNull(response.getBody()).getCountry().getId());
        assertEquals(cityDTO.getCityPopulation(), Objects.requireNonNull(response.getBody()).getCityPopulation());
        assertEquals(cityDTO.getCityArea(), Objects.requireNonNull(response.getBody()).getCityArea());
        assertEquals(cityDTO.getFoundedAt(), Objects.requireNonNull(response.getBody()).getFoundedAt());
        assertEquals(cityDTO.getLanguages(), Objects.requireNonNull(response.getBody()).getLanguages());
    }

    @Test
    void shouldGetBadRequest_forCreatingDuplicateCityName() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetNotFound_WithoutACity() {
        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city/" + 1, HttpMethod.GET, null, CityCRUDResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetCity() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response =
                restTemplate.exchange("/api/city/" + Objects.requireNonNull(response.getBody()).getId(),
                        HttpMethod.GET, null, CityCRUDResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        assertEquals(cityDTO.getCountryId(), Objects.requireNonNull(response.getBody()).getCountry().getId());
        assertEquals(cityDTO.getCityPopulation(), Objects.requireNonNull(response.getBody()).getCityPopulation());
        assertEquals(cityDTO.getCityArea(), Objects.requireNonNull(response.getBody()).getCityArea());
        assertEquals(cityDTO.getFoundedAt(), Objects.requireNonNull(response.getBody()).getFoundedAt());
        assertEquals(cityDTO.getLanguages(), Objects.requireNonNull(response.getBody()).getLanguages());
    }

    @Test
    void shouldGetNotFound_forUpdating_WithoutEntityInDB() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city/" + 1,
                        HttpMethod.PUT, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forUpdating_WithAllInvalidFields() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        cityDTO.setCityName("");
        cityDTO.setCountryId(-1L);
        cityDTO.setCityPopulation(-1);
        cityDTO.setCityArea(-1.0);
        cityDTO.setFoundedAt(Year.now().plusYears(1));
        cityDTO.setLanguages("");

        response = restTemplate.exchange("/api/city/" + savedId,
                        HttpMethod.PUT, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldGetBadRequest_forUpdating_WithOneInvalidField() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        cityDTO.setCityName("");

        response = restTemplate.exchange("/api/city/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldSuccessfullyUpdate() {
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        Long savedId = Objects.requireNonNull(response.getBody()).getId();
        cityDTO.setCityName(cityDTO.getCityName() + 1);
        cityDTO.setCountryId(2L);
        cityDTO.setCityPopulation(cityDTO.getCityPopulation() + 1);
        cityDTO.setCityArea(cityDTO.getCityArea() + 1);
        cityDTO.setFoundedAt(cityDTO.getFoundedAt().plusYears(1));
        cityDTO.setLanguages(cityDTO.getLanguages() + ", Ukrainian");

        response = restTemplate.exchange("/api/city/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(Objects.requireNonNull(response.getBody()).getId());
        assertEquals(cityDTO.getCountryId(), Objects.requireNonNull(response.getBody()).getCountry().getId());
        assertEquals(cityDTO.getCityPopulation(), Objects.requireNonNull(response.getBody()).getCityPopulation());
        assertEquals(cityDTO.getCityArea(), Objects.requireNonNull(response.getBody()).getCityArea());
        assertEquals(cityDTO.getFoundedAt(), Objects.requireNonNull(response.getBody()).getFoundedAt());
        assertEquals(cityDTO.getLanguages(), Objects.requireNonNull(response.getBody()).getLanguages());

    }

    @Test
    void shouldReturnBadRequest_ForDuplicates() {
        CityDTO cityDTO1 = new CityDTO();
        cityDTO1.setCityName("CityName");
        cityDTO1.setCountryId(1L);
        cityDTO1.setCityPopulation(100);
        cityDTO1.setCityArea(10.0);
        cityDTO1.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO1.setLanguages("English, Spanish");

        CityDTO cityDTO2 = new CityDTO();
        cityDTO2.setCityName("CityName2");
        cityDTO2.setCountryId(2L);
        cityDTO2.setCityPopulation(200);
        cityDTO2.setCityArea(20.0);
        cityDTO2.setFoundedAt(Year.of(Year.MIN_VALUE).plusYears(2));
        cityDTO2.setLanguages("English");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO1), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Long savedId = Objects.requireNonNull(response.getBody()).getId();

        response = restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO2), CityCRUDResponse.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        response = restTemplate.exchange("/api/city/" + savedId,
                HttpMethod.PUT, new HttpEntity<>(cityDTO2), CityCRUDResponse.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void deleteNothing_ReturnsOK(){
        ResponseEntity<Void> response =
                restTemplate.exchange("/api/city/" + 1, HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteValidId_ReturnsOK(){
        CityDTO cityDTO = new CityDTO();
        cityDTO.setCityName("CityName");
        cityDTO.setCountryId(1L);
        cityDTO.setCityPopulation(100);
        cityDTO.setCityArea(10.0);
        cityDTO.setFoundedAt(Year.of(Year.MIN_VALUE));
        cityDTO.setLanguages("English, Spanish");

        ResponseEntity<CityCRUDResponse> response =
                restTemplate.exchange("/api/city", HttpMethod.POST, new HttpEntity<>(cityDTO), CityCRUDResponse.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ResponseEntity<Void> responseVoid =
                restTemplate.exchange("/api/city/" + Objects.requireNonNull(response.getBody()).getId(),
                        HttpMethod.DELETE, null, Void.class);

        assertEquals(HttpStatus.OK, responseVoid.getStatusCode());

        response = restTemplate.exchange("/api/city/" + response.getBody().getId(),
                        HttpMethod.GET, null, CityCRUDResponse.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void uploadEmptyJson_ReturnsOK() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/Empty.json"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);

        var request = getAsMultiValue(mockMultipartFile);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/city/upload", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> map = reader.readValue(response.getBody());

        assertEquals(0, map.get("totalSaved"));
        assertEquals(0, map.get("totalErrors"));
    }

    @Test
    void upload5Json_ReturnsOK() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/USA5Cities.json"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);

        var request = getAsMultiValue(mockMultipartFile);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/city/upload", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> map = reader.readValue(response.getBody());

        assertEquals(5, map.get("totalSaved"));
        assertEquals(0, map.get("totalErrors"));
    }

    @Test
    void uploadBrokenJson_ReturnsOK() throws IOException {
        byte[] content = Files.readAllBytes(
                Paths.get("src/test/resources/json/FileWithBrokenLinesAndSomeValidFields.json")
        );

        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);

        var request = getAsMultiValue(mockMultipartFile);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/city/upload", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> map = reader.readValue(response.getBody());

        assertEquals(3, map.get("totalSaved"));
        assertEquals(7, map.get("totalErrors"));
    }

    @Test
    void upload100Json_ReturnsOK() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));

        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);

        var request = getAsMultiValue(mockMultipartFile);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/city/upload", request, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> map = reader.readValue(response.getBody());

        assertEquals(100, map.get("totalSaved"));
        assertEquals(0, map.get("totalErrors"));
    }

    //TODO getList: withoutAdditionalReturnsAll, returnsByCountryId
    @Test
    void getList_BadRequest_WithoutPageAndSize(){
        Map<String, String> input = new HashMap<>();

        ResponseEntity<byte[]> response = restTemplate.exchange("/api/city/_list",
                HttpMethod.POST, new HttpEntity<>(input), byte[].class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void getList_ReturnsAll_WithoutFilters() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);
        var request = getAsMultiValue(mockMultipartFile);
        ResponseEntity<String> responseUploading = restTemplate.postForEntity("/api/city/upload", request, String.class);
        assertEquals(HttpStatus.OK, responseUploading.getStatusCode());

        Map<String, String> input = new HashMap<>();
        input.put("page", "0");
        input.put("size", "100");

        ResponseEntity<String> response
                = restTemplate.postForEntity("/api/city/_list", input, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> responseMap = reader.readValue(jsonResponse);

        List<CityFilteredResponse> cityList = (List<CityFilteredResponse>) responseMap.get("list");
        assertNotNull(cityList);
        assertEquals(100, cityList.size());

        int totalPages = (int) responseMap.get("totalPages");
        assertEquals(1, totalPages);
    }

    @Test
    void getList_ReturnsAll_WithCountryIdFilter() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);
        var request = getAsMultiValue(mockMultipartFile);
        ResponseEntity<String> responseUploading = restTemplate.postForEntity("/api/city/upload", request, String.class);
        assertEquals(HttpStatus.OK, responseUploading.getStatusCode());

        Map<String, String> input = new HashMap<>();
        input.put("page", "0");
        input.put("size", "5");
        input.put("countryId", "1");

        ResponseEntity<String> response
                = restTemplate.postForEntity("/api/city/_list", input, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        String jsonResponse = response.getBody();

        ObjectReader reader = new ObjectMapper().readerFor(Map.class);

        Map<String, Object> responseMap = reader.readValue(jsonResponse);

        List<CityFilteredResponse> cityList = (List<CityFilteredResponse>) responseMap.get("list");
        assertNotNull(cityList);
        assertEquals(5, cityList.size());

        int totalPages = (int) responseMap.get("totalPages");
        assertEquals(5, totalPages);
    }

    //TODO generateCsvFile: withoutFiltersReturnsAll, withFilterByCountryId, withWrongFilterReturnsAll
    @Test
    void generateCsvFile_ReturnsCsvFile() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);
        var request = getAsMultiValue(mockMultipartFile);
        ResponseEntity<String> responseUploading = restTemplate.postForEntity("/api/city/upload", request, String.class);
        assertEquals(HttpStatus.OK, responseUploading.getStatusCode());

        Map<String, String> input = new HashMap<>();
        ResponseEntity<byte[]> response = restTemplate.postForEntity("/api/city/_report", input, byte[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"report.csv\"", headers.getContentDisposition().toString());

        byte[] csvBytes = response.getBody();
        assertNotNull(csvBytes);

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8))) {
            int rowCount = 0;
            while (reader.readNext() != null) {
                rowCount++;
            }
            assertEquals(101, rowCount);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateCsvFile_WithFilter_Returns25() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);
        var request = getAsMultiValue(mockMultipartFile);
        ResponseEntity<String> responseUploading = restTemplate.postForEntity("/api/city/upload", request, String.class);
        assertEquals(HttpStatus.OK, responseUploading.getStatusCode());

        Map<String, String> input = new HashMap<>();
        input.put("countryId", "1");
        ResponseEntity<byte[]> response = restTemplate.postForEntity("/api/city/_report", input, byte[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"report.csv\"", headers.getContentDisposition().toString());

        byte[] csvBytes = response.getBody();
        assertNotNull(csvBytes);

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8))) {
            int rowCount = 0;
            while (reader.readNext() != null) {
                rowCount++;
            }
            assertEquals(26, rowCount);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    @Test
    void generateCsvFile_WithWrongFilter_ReturnsEverything() throws IOException {
        byte[] content = Files.readAllBytes(Paths.get("src/test/resources/json/OneHundredCitiesTest.json"));
        MockMultipartFile mockMultipartFile = new MockMultipartFile("fileName", content);
        var request = getAsMultiValue(mockMultipartFile);
        ResponseEntity<String> responseUploading = restTemplate.postForEntity("/api/city/upload", request, String.class);
        assertEquals(HttpStatus.OK, responseUploading.getStatusCode());

        Map<String, String> input = new HashMap<>();
        input.put("Somefilter", "filter");
        input.put("oneMore", "filter1");
        ResponseEntity<byte[]> response = restTemplate.postForEntity("/api/city/_report", input, byte[].class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        HttpHeaders headers = response.getHeaders();
        assertNotNull(headers);
        assertEquals(MediaType.APPLICATION_OCTET_STREAM, headers.getContentType());
        assertEquals("form-data; name=\"attachment\"; filename=\"report.csv\"", headers.getContentDisposition().toString());

        byte[] csvBytes = response.getBody();
        assertNotNull(csvBytes);

        try (CSVReader reader = new CSVReader(new InputStreamReader(new ByteArrayInputStream(csvBytes), StandardCharsets.UTF_8))) {
            int rowCount = 0;
            while (reader.readNext() != null) {
                rowCount++;
            }
            assertEquals(101, rowCount);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> getAsMultiValue(MultipartFile multipartFile){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file_name", "testFile");
        body.add("file", multipartFile.getResource());

        return new HttpEntity<>(body, headers);
    }

    @AfterEach
    public void cleanUp() {
        List<City> cities = cityRepository.findAll();
        if(!cities.isEmpty()) {
            cityRepository.deleteAll(cities);
        }
    }
}