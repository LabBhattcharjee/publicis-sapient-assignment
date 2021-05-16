package com.sapient.publicis.controller;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:testApp.yml")
class WeatherServiceControllerOverriddenCOnfigTest extends BaseTestTemplate {

}
