package nerdery.jvm.challenge.starwars.service

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import groovy.transform.CompileStatic
import nerdery.jvm.challenge.starwars.client.StarWarsApiClient
import nerdery.jvm.challenge.starwars.domain.Person
import nerdery.jvm.challenge.starwars.domain.Planet
import nerdery.jvm.challenge.starwars.domain.ResultSet
import nerdery.jvm.challenge.starwars.domain.Starship
import retrofit.Call
import retrofit.JacksonConverterFactory
import retrofit.Response
import retrofit.Retrofit

/**
 * Created by dgoetsch on 10/27/15.
 */
@CompileStatic
class StarWarsService {

    static final String BASE_URL = "http://swapi.co"
    StarWarsApiClient client

    StarWarsService() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory.create(mapper))
                .build();

        client = retrofit.create(StarWarsApiClient)
    }

    Planet getPlanetByName(String name) {
        if(name == null || name.isEmpty()) return null
        Response<ResultSet<Planet>> planetResponse = client.getPlanets().execute()
        ResultSet<Planet> planetRS = planetResponse.body()
        while(planetRS != null) {
            for(Planet planet : planetRS.results) {
                if(name.equalsIgnoreCase(planet.name)) {
                    return planet
                }
            }
            String pageParamToken = "page="
            int nextPageNumIndex= planetRS.next.indexOf(pageParamToken) + pageParamToken.length()
            Integer nextPage = Integer.parseInt(planetRS.next.substring(nextPageNumIndex))
            planetRS = client.getPlanets(nextPage).execute().body()
        }
        return null
    }

    Person getPersonFromUri(String uri) {
        int beginIndex = uri.indexOf("people/") + "people/".length()
        int endIndex = uri.lastIndexOf("/") //- 1
        return getPersonById(Integer.parseInt(uri.substring(beginIndex, endIndex)))
    }
    Person getPersonById(int id) {
        return client.getPerson(id).execute().body()
    }

    Starship getStarshipFromUri(String uri) {
        int beginIndex = uri.indexOf("starships/") + "starships/".length()
        int endIndex = uri.lastIndexOf("/") //- 1
        return getStarshipById(Integer.parseInt(uri.substring(beginIndex, endIndex)))
    }
    Starship getStarshipById(int id) {
        return client.getStarship(id).execute().body()
    }

}
