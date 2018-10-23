package search

import grails.gorm.transactions.Transactional
import me.xdrop.fuzzywuzzy.FuzzySearch

@Transactional
class DistanceService {

    void order(List<FuzzyDomain> domainList) {
        Collections.sort(domainList, new Comparator<FuzzyDomain>() {
            @Override
            int compare(FuzzyDomain domain1, FuzzyDomain domain2) {
                return domain2.fuzzyScore <=> domain1.fuzzyScore
            }
        })
    }

    void setFuzzyScore(List<FuzzyDomain> domainList, String keyword) {
        domainList.each { FuzzyDomain fuzzyDomain ->
            fuzzyDomain.fuzzyScore = FuzzySearch.weightedRatio(keyword, fuzzyDomain.getFuzzyAttributes().get(0))
        }
    }
}
