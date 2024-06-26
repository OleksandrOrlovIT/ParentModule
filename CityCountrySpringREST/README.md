<h1>OleksandrOrlovIT CityCountrySpringREST</h1>
<h2>Project Description</h2>
<h3>Recent Changes</h3>
<p>In order to add RabbitMQ implemenetation there was created an .env file. Change it to your real email to send this email in message via RabbitMQ.</p>
<h2>Preparation to start app</h2>
<p>To make application work with rabbitMQ you need to start another application available by next link <a href="https://github.com/OleksandrOrlovIT/CityCountryEmailSender">https://github.com/OleksandrOrlovIT/CityCountryEmailSender</a></p>
<br/>
<p>
  To configure this app please proceed to the next steps -> <br/> 
  1. Clone application 
  
  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/0b6c79d9-c598-4ed3-acc7-9afc423b5125)
  
  2. Input email address where you want to receive emails in root of the project in .env file.

     ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/eb008ca2-8f73-4866-a7bc-04bb27209555)
  3. Inside project root run <b>docker-compose up --build</b>
  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/7ea5ac8e-d000-4fa2-a8f0-a9a311286682)
  4. Wait for  Started CityCountrySpringRestApplication in 6.75 seconds message in the logs for full build. At <a href="http://localhost:8080/swagger-ui/index.html#/">http://localhost:8080/swagger-ui/index.html#/</a> you can use app, create country and create city (post methods) will send messages to message broker.
</p>
<h3>Summary</h3>
<p>This is a REST application that works with country and city entities. It provides CRUD methods and some additional methods for city to upload json file or to get Pages or CSV using HTTP.
It uses Docker Compose to start postgres db before starting application and Liquibase to create schema. In resources OneHundredCities.json provided to upload cities using POST. /api/entity1/upload<br/>
After running application at <a href="http://localhost:8080/swagger-ui/index.html#/">http://localhost:8080/swagger-ui/index.html#/</a> you can find swagger info about application
</p>
<h3>Tech Stack</h3>
<p>Java 17, lombok, jackson-core, jackson-dataformat-xml, junit-jupiter, mockito-core, liquibase, spring-boot-starter-web, postgresql, spring-boot-docker-compose, swagger</p>
<h3>Main endpoints</h3>
<h4>Country endpoints</h4>
<p>
  <h5>GET /api/country - returns all countries from db.<h5><br/>

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/dc405c92-66ed-45cf-ba9d-39f9cc008d85)

  <h5>POST /api/country (valid body for country) - creates new Country;</h5><br/>

![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/fe54cd9b-bc4e-4deb-8ee2-c67271f81e7e)

![Screenshot from 2024-05-02 22-59-33](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/936a28b4-8a49-4784-bd31-3f6e7331523b)

  <h5>PUT /api/country/{id} (valid body for country) - updates Country;</h5><br/>

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/8e1d3a7b-4ed7-4d1f-a96e-b00ef84419b9)
  
  <h5>DELETE /api/country/{id} - deletes Country;</h5><br/>
  
  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/e637166a-dd73-44f7-a65d-ba5ceacfe68d)
</p>
<h4>City endpoints</h4>
<p>
  <b>I will upload 100 jsons in order to test all other endpoints</b>
  <h5>POST /api/city/upload - uploads file of jsons and returns how many were errors and how many were saved objects.</h5><br/>
  
  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/cc48976a-3b5f-45aa-88b2-79df78d5e124)

  <h5>POST /api/city (valid body for city) - creates new City.</h5><br/>
  
![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/76b54a4d-9d13-4f7d-939f-56ae3a3e18c7)

  <h5>GET /api/city/{id} - Returns City by id.</h5><br/>
  
![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/6f03401f-c98c-4a70-928e-af4766af5618)

  
  <h5>PUT /api/entity1/{id} (valid body for city) - updates City.</h5><br/>
  
![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/f30665cf-9188-4a49-a0ff-79aff77751ce)

  <h5>DELETE /api/city/{id} - deletes City.</h5><br/>

![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/74f9df22-9676-4728-b1c6-60dda43650cc)
  
  <h5>POST /api/city/_list for example{“countryId”: 2,…, “page”: 1,“size”: 20} (page and size always has to be inside body)- returns page with all found by filtering objects and totalPages number. (Filters are passed inside body)</h5><br/>
  
![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/1ab98bba-ec3a-45d4-9e59-7c990f1c8e89)

![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/5c680ff2-b78c-4a07-ad35-12c1bc42633b)

  <h5>POST /api/city/_report {“entity2Id”: 2, …} - returns a csv file of all found cities matching filtering in body.</h5><br/>

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/25823da6-9b5c-47cd-b50d-f2c8b131f5f7)

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/0a2d55bc-e37f-4988-97c0-96d6b6082ba4)

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/95a6ac46-0125-49e9-adc4-897a58e82d05)

</p>
<h3>Main working directories</h3>
<h4>Directory Tree</h4>
<p>
  
  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/c708f0d3-c555-43f6-a9d4-4f0555987893)
  
</p>

<h4>model package</h4>
<p>
  In model package I have 2 entity classes, custom validators, and converters for JSON.
</p>
<h4>repository package</h4>
<p>
  In repository package I have 2 JPARepostories for both entity. CityRepository extends JpaSpecificationExecutor to add select statements by predicate.
</p>
<h4>service package</h4>
<p>
  In service package I have interfaces for services to delete loosely coupling. impl package - for Service interface implementations. specification package has a class to create specifications for City class depending on passes fields.
</p>
<h4>json package</h4>
<p>
  This package contains slightly changed files from ProfitSoftJavaCore to handle json serialization of json file
</p>
<h4>json package</h4>
<p>
  This package contains slightly changed files from ProfitSoftJavaCore to handle json serialization of json file
</p>
<h4>controller package</h4>
<p>
  This package contains controllers for both Country and City classes. It contains DTO classes and a mapper class.
</p>
<h4>exception package</h4>
<p>
  This package contains GlobalExceptionHandler.
</p>
<h4>exception package</h4>
<p>
  This package contains CSVGeneratorUtil to create a CSV file. 
</p>
<h3>Testing</h3>
<h4>Code tests</h4>
<p>
  For testing I wrote Integrational tests for CityController and CountryController. They both contain test cases to test bad and good possible behavior of my programm.

  ![image](https://github.com/OleksandrOrlovIT/CityCountrySpringREST/assets/86959421/622d43c5-4d10-4869-9f2e-54a25baac2b8)


</p>
<h4>How to Use</h4>
<p>In order to use my application simply clone from github this repository and start spring boot application. You have to install docker before, starting my programm.</p>
