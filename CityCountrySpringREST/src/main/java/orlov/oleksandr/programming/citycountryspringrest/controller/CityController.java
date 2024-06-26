package orlov.oleksandr.programming.citycountryspringrest.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CityDTO;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper.CityMapper;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityCRUDResponse;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityFilteredResponse;
import orlov.oleksandr.programming.citycountryspringrest.csv.CSVGeneratorUtil;
import orlov.oleksandr.programming.citycountryspringrest.json.JSONParser;
import orlov.oleksandr.programming.citycountryspringrest.model.City;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CityService;
import orlov.oleksandr.programming.citycountryspringrest.service.interfaces.CountryService;

import java.io.IOException;
import java.util.*;

/**
 * Controller class that used to work with cities
 */

@CrossOrigin("http://localhost:3000/")
@AllArgsConstructor
@RestController
@RequestMapping("/api/city")
public class CityController {

    private CityService cityService;
    private CountryService countryService;
    private CityMapper cityMapper;
    private JSONParser jsonParser;
    private CSVGeneratorUtil csvGeneratorUtil;

    /**
     * Takes cityDto and tries to save this city
     * @param cityDTO
     * @return ResponseEntity<CityCRUDResponse>
     */
    @PostMapping
    public ResponseEntity<CityCRUDResponse> createCity(@RequestBody @Validated CityDTO cityDTO) {
        Country country = countryService.findById(cityDTO.getCountryId());

        City city = cityMapper.toCity(cityDTO, country);

        city = cityService.create(city);

        CityCRUDResponse crudResponse = cityMapper.toCityCRUDResponse(city);

        return new ResponseEntity<>(crudResponse, HttpStatus.CREATED);
    }

    /**
     * Endpoint to get city by id
     * @param cityId
     * @return CityCRUDResponse
     */
    @GetMapping("/{cityId}")
    public CityCRUDResponse getCity(@PathVariable Long cityId) {
        City city = cityService.findById(cityId);

        return cityMapper.toCityCRUDResponse(city);
    }


    /**
     * Endpoint to update city
     * @param cityId
     * @param cityDTO
     * @return CityCRUDResponse
     */
    @PutMapping("/{cityId}")
    public CityCRUDResponse updateCity(@PathVariable Long cityId, @RequestBody @Validated CityDTO cityDTO) {
        Country country = countryService.findById(cityDTO.getCountryId());

        City city = cityMapper.toCity(cityDTO, country);
        city.setId(cityId);

        city = cityService.update(city);

        return cityMapper.toCityCRUDResponse(city);
    }

    /**
     * Endpoint to delete city by id
     * @param cityId
     */
    @DeleteMapping("/{cityId}")
    public void deleteCity(@PathVariable Long cityId) {
        cityService.deleteById(cityId);
    }

    /**
     * Endpoint to get list of city entities with desired page and size
     * @param input
     * @return MappingJacksonValue
     */
    @PostMapping("/_list")
    public MappingJacksonValue getList(@RequestBody Map<String, String> input) {
        int page = Integer.parseInt(input.get("page"));
        int size = Integer.parseInt(input.get("size"));

        input.remove("page");
        input.remove("size");

        Pageable pageable = PageRequest.of(page, size);

        Page<City> cityPage = cityService.findPageCitiesByFilters(input, pageable);

        List<CityFilteredResponse> responseList = cityMapper.toCityFilteredResponseList(cityPage.getContent());

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("list", responseList);
        response.put("totalPages", cityPage.getTotalPages());

        MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(response);
        FilterProvider filterProvider = getFilterProvider(input);
        mappingJacksonValue.setFilters(filterProvider);

        return mappingJacksonValue;
    }

    /**
     * Endpoint to retrieve a csv file of city entities
     * @param input
     * @return ResponseEntity<byte[]>
     */
    @PostMapping("/_report")
    public ResponseEntity<byte[]> generateCsvFile(@RequestBody Map<String, String> input) {
        List<City> cities = cityService.findCitiesByFilters(input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "report.csv");

        byte[] csvBytes = csvGeneratorUtil.generateCityCsv(cities).getBytes();

        return new ResponseEntity<>(csvBytes, headers, HttpStatus.OK);
    }

    /**
     * Endpoint to upload a json file with city entities
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException {
        int[] response = jsonParser.saveCitiesFromInputStream(file);

        Map<String, Integer> jsonResponseMap = new HashMap<>();
        jsonResponseMap.put("totalSaved", response[1]);
        jsonResponseMap.put("totalErrors", response[0] - response[1]);

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(jsonResponseMap);

        return ResponseEntity.ok().body(jsonResponse);
    }

    /**
     * Method to getFilters using input map
     * @param input
     * @return
     */
    private FilterProvider getFilterProvider(Map<String, String> input) {
        Set<String> fieldSet = new HashSet<>();
        fieldSet.add("id");
        fieldSet.add("cityName");
        fieldSet.addAll(input.keySet());

        SimpleBeanPropertyFilter filter = new SimpleBeanPropertyFilter.FilterExceptFilter(fieldSet);

        return new SimpleFilterProvider()
                .addFilter("cityFilter", filter);
    }
}
