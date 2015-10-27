package nerdery.jvm.challenge.starwars.client

import groovy.transform.CompileStatic
import nerdery.jvm.challenge.starwars.domain.Person
import nerdery.jvm.challenge.starwars.domain.Planet
import nerdery.jvm.challenge.starwars.domain.ResultSet
import nerdery.jvm.challenge.starwars.domain.Starship
import retrofit.Call
import retrofit.http.GET
import retrofit.http.Path
import retrofit.http.Query

@CompileStatic
interface StarWarsApiClient {
    @GET("/api/planets/{id}/")
    Call<Planet> getPlanet(@Path("id") int id);

    @GET("/api/planets/")
    Call<ResultSet<Planet>> getPlanets();

    @GET("/api/planets/")
    Call<ResultSet<Planet>> getPlanets(@Query("page") int page);

    @GET("/api/people/{id}/")
    Call<Person> getPerson(@Path("id") int id);

    @GET("/api/starships/{id}/")
    Call<Starship> getStarship(@Path("id") int id);

}