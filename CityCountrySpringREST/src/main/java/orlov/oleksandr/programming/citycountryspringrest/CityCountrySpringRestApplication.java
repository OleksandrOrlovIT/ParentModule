package orlov.oleksandr.programming.citycountryspringrest;

import lombok.AllArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry of the program
 */
@AllArgsConstructor
@SpringBootApplication
public class CityCountrySpringRestApplication{

    public static void main(String[] args) {
        SpringApplication.run(CityCountrySpringRestApplication.class, args);
    }

}
