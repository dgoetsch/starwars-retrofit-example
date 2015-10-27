package nerdery.jvm.challenge.starwars.domain

class ResultSet<T> {
    Integer count
    String next
    String previous
    List<T> results
}
