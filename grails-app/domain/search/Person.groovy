package search

class Person implements FuzzyDomain {

    String name

    Double fuzzyScore = 0

    static transients = ['fuzzyScore']

    // HibernateSearch
    static search = {
        name index: 'yes'
    }

    // ElasticSearch
    static searchable = {
        name boost:2.0
    }

    static constraints = {
    }

    static mapping = {
        version false
    }

    @Override
    List<String> getFuzzyAttributes() {
        return [name]
    }

    @Override
    void setFuzzyScore(Double score) {
        fuzzyScore = score
    }

    @Override
    Double getFuzzyScore() {
        return fuzzyScore
    }
}
