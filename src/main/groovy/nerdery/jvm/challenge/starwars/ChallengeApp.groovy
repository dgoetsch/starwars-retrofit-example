package nerdery.jvm.challenge.starwars

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import nerdery.jvm.challenge.starwars.client.StarWarsApiClient
import nerdery.jvm.challenge.starwars.domain.Person
import nerdery.jvm.challenge.starwars.domain.Planet
import nerdery.jvm.challenge.starwars.domain.Starship
import nerdery.jvm.challenge.starwars.service.StarWarsService
import retrofit.JacksonConverterFactory
import retrofit.Retrofit

/**
 * Created by dgoetsch on 10/27/15.
 */
class ChallengeApp {
    static final String BASE_URL = "http://swapi.co"

    static void main(String ... args) {
        Map<String, List<String>> starshipsByTatooineResident = [:]
        StarWarsService service = new StarWarsService()
        Planet tatooine = service.getPlanetByName("Tatooine")
        tatooine.residents.each({
            Person person = service.getPersonFromUri(it)
            starshipsByTatooineResident.put(person.name, [])
            person.starships.each({
                Starship starship = service.getStarshipFromUri(it)
                starshipsByTatooineResident.get(person.name).add(starship.name)

            })
        })
        println starshipsByTatooineResident
    }
}
