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

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * Created by dgoetsch on 10/27/15.
 */
class ChallengeApp {
    static final String BASE_URL = "http://swapi.co"

    static void main(String ... args) {
        Map<String, List<String>> starshipsByTatooineResident = new ConcurrentHashMap<>()
        StarWarsService service = new StarWarsService()
        Planet tatooine = service.getPlanetByName("Tatooine")
        ExecutorService executor = Executors.newCachedThreadPool()
        CountDownLatch tatooineLatch = new CountDownLatch(tatooine.residents.size())
        tatooine.residents.each({
            executor.submit(new Runnable() {
                @Override
                void run() {
                    Person person = service.getPersonFromUri(it)
                    starshipsByTatooineResident.put(person.name, [])
                    person.starships.each({
                        executor.submit(new Runnable() {
                            @Override
                            void run() {
                                Starship starship = service.getStarshipFromUri(it)
                                starshipsByTatooineResident.get(person.name).add(starship.name)
                            }
                        })
                    })
                    tatooineLatch.countDown()
                }
            })
        })
        tatooineLatch.await()
        executor.shutdown()
        executor.awaitTermination(100, TimeUnit.SECONDS)
        println starshipsByTatooineResident
    }
}
