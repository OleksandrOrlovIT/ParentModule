package orlov.oleksandr.programming.citycountryspringrest.controller.dto.mapper;

import org.springframework.stereotype.Component;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.request.CityDTO;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityCRUDResponse;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityFilteredResponse;
import orlov.oleksandr.programming.citycountryspringrest.controller.dto.response.CityResponse;
import orlov.oleksandr.programming.citycountryspringrest.model.City;
import orlov.oleksandr.programming.citycountryspringrest.model.Country;

import java.util.ArrayList;
import java.util.List;

/**
 * Mapper component for mapping between CityDTO and City entities.
 */
@Component
public class CityMapper {

    /**
     * Maps a CityDTO to a City entity.
     *
     * @param cityDTO The CityDTO object
     * @param country The Country object associated with the city
     * @return The mapped City entity
     */
    public City toCity(CityDTO cityDTO, Country country) {
        City city = new City();
        city.setCityName(cityDTO.getCityName());
        city.setCountry(country);
        city.setCityPopulation(cityDTO.getCityPopulation());
        city.setCityArea(cityDTO.getCityArea());
        city.setFoundedAt(cityDTO.getFoundedAt());
        city.setLanguages(cityDTO.getLanguagesList());

        return city;
    }

    /**
     * Maps City to CityFilteredResponse
     *
     * @param city
     * @return CityFilteredResponse
     */
    public CityFilteredResponse toCityFilteredResponse(City city) {
        CityFilteredResponse cityResponse = new CityFilteredResponse();
        cityResponse.setId(city.getId());
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountryId(city.getCountry().getId());
        cityResponse.setCityPopulation(city.getCityPopulation());
        cityResponse.setCityArea(city.getCityArea());
        cityResponse.setFoundedAt(city.getFoundedAt());
        cityResponse.setLanguages(convertListLanguages(city.getLanguages()));

        return cityResponse;
    }

    /**
     * Maps City to CityResponse
     *
     * @param city
     * @return CityResponse
     */
    public CityResponse toCityResponse(City city){
        CityResponse cityResponse = new CityResponse();
        cityResponse.setId(city.getId());
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountryId(city.getCountry().getId());
        cityResponse.setCityPopulation(city.getCityPopulation());
        cityResponse.setCityArea(city.getCityArea());
        cityResponse.setFoundedAt(city.getFoundedAt());
        cityResponse.setLanguages(convertListLanguages(city.getLanguages()));

        return cityResponse;
    }

    /**
     * Maps City to CityResponse in csv format
     *
     * @param city
     * @return CityResponse
     */
    public CityResponse toCityResponseCSVFormat(City city){
        CityResponse cityResponse = new CityResponse();
        cityResponse.setId(city.getId());
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountryId(city.getCountry().getId());
        cityResponse.setCityPopulation(city.getCityPopulation());
        cityResponse.setCityArea(city.getCityArea());
        cityResponse.setFoundedAt(city.getFoundedAt());
        cityResponse.setLanguages(convertListLanguagesForCSV(city.getLanguages()));

        return cityResponse;
    }

    /**
     * Maps City to CityCRUDResponse
     *
     * @param city
     * @return CityCRUDResponse
     */
    public CityCRUDResponse toCityCRUDResponse(City city){
        CityCRUDResponse cityResponse = new CityCRUDResponse();
        cityResponse.setId(city.getId());
        cityResponse.setCityName(city.getCityName());
        cityResponse.setCountry(city.getCountry());
        cityResponse.setCityPopulation(city.getCityPopulation());
        cityResponse.setCityArea(city.getCityArea());
        cityResponse.setFoundedAt(city.getFoundedAt());
        cityResponse.setLanguages(convertListLanguages(city.getLanguages()));

        return cityResponse;
    }

    /**
     * Maps List of City class to List of CityFilteredResponse
     *
     * @param cities
     * @return List<CityFilteredResponse>
     */
    public List<CityFilteredResponse> toCityFilteredResponseList(List<City> cities) {
        List<CityFilteredResponse> cityFilteredResponses = new ArrayList<>();

        for(City city : cities) {
            cityFilteredResponses.add(toCityFilteredResponse(city));
        }

        return cityFilteredResponses;
    }

    /**
     * Maps List of City class to List of CityResponse
     *
     * @param cities
     * @return List<CityResponse>
     */
    public List<CityResponse> toCityResponseList(List<City> cities) {
        List<CityResponse> cityResponses = new ArrayList<>();

        for(City city : cities) {
            cityResponses.add(toCityResponse(city));
        }

        return cityResponses;
    }

    /**
     * Maps List of City class to List of CityResponse in csv format
     *
     * @param cities
     * @return List<CityResponse>
     */
    public List<CityResponse> toCityResponseListFormatCSV(List<City> cities) {
        List<CityResponse> cityResponses = new ArrayList<>();

        for(City city : cities) {
            cityResponses.add(toCityResponseCSVFormat(city));
        }

        return cityResponses;
    }

    /**
     * Convert List<String> of languages into one String
     *
     * @param languages
     * @return String
     */
    public String convertListLanguages(List<String> languages){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < languages.size() - 1; i++){
            sb.append(languages.get(i)).append(", ");
        }
        sb.append(languages.get(languages.size() - 1));

        return sb.toString();
    }

    /**
     * Convert List<String> of languages into one String in csv format
     *
     * @param languages
     * @return String
     */
    public String convertListLanguagesForCSV(List<String> languages){
        StringBuilder sb = new StringBuilder();
        sb.append("\"");
        for(int i = 0; i < languages.size() - 1; i++){
            sb.append(languages.get(i)).append(", ");
        }
        sb.append(languages.get(languages.size() - 1));
        sb.append("\"");

        return sb.toString();
    }
}
