package search

interface FuzzyDomain {

    List<String> getFuzzyAttributes();
    void setFuzzyScore(Double score);
    Double getFuzzyScore();


}
