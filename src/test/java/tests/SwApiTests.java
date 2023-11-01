package tests;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import pojos.Film;
import pojos.Person;
import pojos.Planet;
import utils.Utils;

public class SwApiTests {

  String C3POSecondMovieURL;
  String firstPlanetURL;

  @BeforeClass
  public void beforeClass() {
    RestAssured.baseURI = "https://swapi.dev/api";
  }

  @Test()
  public void testC3PO() {
    Response response;

    response = RestAssured.given().get("/people/2");

    Person person = response.as(Person.class);

    C3POSecondMovieURL = person.getFilms()[1];

    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(person.getSkin_color(), "gold");
    Assert.assertEquals(person.getFilms().length, 6);
  }

  @Test(dependsOnMethods = "testC3PO")
  public void testSecondC3POFilm() {
    Response response;

    response = RestAssured.given().get(C3POSecondMovieURL);

    Film film = response.as(Film.class);

    firstPlanetURL = film.getPlanets()[0];

    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertTrue(Utils.isValidDate(film.getRelease_date()));
    Assert.assertTrue(film.getCharacters().length > 1);
    Assert.assertTrue(film.getPlanets().length > 1);
    Assert.assertTrue(film.getStarships().length > 1);
    Assert.assertTrue(film.getVehicles().length > 1);
    Assert.assertTrue(film.getSpecies().length > 1);

  }

  @Test(dependsOnMethods = "testSecondC3POFilm")
  public void testLastFilmFirstPlanet() {

    Response response;

    response = RestAssured.given().get(firstPlanetURL);

    Planet planet = response.as(Planet.class);

    String expectedGravity = "1.1 standard";
    String expectedTerrain = "tundra, ice caves, mountain ranges";

    Assert.assertEquals(response.getStatusCode(), 200);
    Assert.assertEquals(planet.getGravity(), expectedGravity);
    Assert.assertEquals(planet.getTerrain(), expectedTerrain);

    Response responsePlanetUrl = RestAssured.given().get(planet.getUrl());

    Assert.assertEquals(response.getStatusCode(), responsePlanetUrl.getStatusCode());
    Assert.assertEquals(response.getBody().asString(), responsePlanetUrl.getBody().asString());
  }

  @Test
  public void testSeventhFilmError() {
    Response response;

    response = RestAssured.given().get("/films/7");

    Assert.assertEquals(response.getStatusCode(), 404);
  }


}
