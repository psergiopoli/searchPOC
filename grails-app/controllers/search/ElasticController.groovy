package search


import grails.converters.JSON
import grails.plugins.elasticsearch.ElasticSearchResult
import grails.plugins.elasticsearch.ElasticSearchService
import grails.web.RequestParameter
import org.apache.lucene.queryparser.flexible.core.builders.QueryBuilder
import org.elasticsearch.common.unit.Fuzziness
import org.elasticsearch.index.query.FuzzyQueryBuilder
import org.elasticsearch.index.query.QueryBuilders
import org.springframework.http.HttpStatus

class ElasticController extends BaseController {

    DistanceService distanceService
    ElasticSearchService elasticSearchService

    def index(@RequestParameter('order') Boolean order,
              @RequestParameter('search') String search) {
        log.info(request.parameterMap.toMapString())

        FuzzyQueryBuilder query = QueryBuilders.fuzzyQuery('name', search).fuzziness(Fuzziness.TWO)

        ElasticSearchResult result = elasticSearchService.search(query, null as Closure, [indices: Person, types: Person])

        List<Person> personList = result.searchResults

        if (order) {
            distanceService.setFuzzyScore(personList, search)
            distanceService.order(personList)
        }

        respond personList
    }

    def reIndex(){
        Message message = new Message(
                message: 'ok',
                code: 'reindex.success.hibernate'
        )

        elasticSearchService.index()
        //elasticSearchService.index(Person)
        //elasticSearchService.unindex()

        render (status: HttpStatus.OK.value(), contentType: Message.CONTENT_TYPE, message as JSON)
    }
}
