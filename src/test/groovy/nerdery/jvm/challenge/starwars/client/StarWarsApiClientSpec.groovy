package nerdery.jvm.challenge.starwars.client

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import nerdery.jvm.challenge.starwars.domain.Person
import nerdery.jvm.challenge.starwars.domain.Planet
import nerdery.jvm.challenge.starwars.domain.Starship
import retrofit.JacksonConverterFactory
import retrofit.Retrofit
import spock.lang.Specification

class StarWarsApiClientSpec extends Specification {
    static final String BASE_URL = "http://swapi.co"
    StarWarsApiClient client

    void setup() {
        ObjectMapper mapper = new ObjectMapper()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).addConverterFactory(JacksonConverterFactory.create(mapper))
                .build()

        client = retrofit.create(StarWarsApiClient)
    }

    void "test get a person"() {
        given:
        int id = 1

        when:
        Person person = client.getPerson(id).execute().body()

        then:
        person.name == "Luke Skywalker"
        person.starships.size() ==2
        person.starships.contains('http://swapi.co/api/starships/12/')
        person.starships.contains('http://swapi.co/api/starships/22/')
    }
    void "test get a planet"() {
        given:
        int id = 1

        when:
        Planet planet = client.getPlanet(id).execute().body()

        then:
        planet.name == "Tatooine"
        planet.residents.size() == 10
    }

    void "test get a starship"() {
        given:
        int id = 2

        when:
        Starship starship = client.getStarship(id).execute().body()

        then:
        starship.name == 'CR90 corvette'
    }
}
