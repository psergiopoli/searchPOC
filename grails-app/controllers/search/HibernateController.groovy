package search


import grails.rest.*
import grails.converters.*
import grails.web.RequestParameter
import org.springframework.http.HttpStatus

class HibernateController extends BaseController{

    DistanceService distanceService

    def index(@RequestParameter('order') Boolean order,
              @RequestParameter('search') String search) {
        log.info(request.parameterMap.toMapString())

        List<Person> personList = Person.search().list {
            if (search && !search.isEmpty()) {
                fuzzy "name", search, [maxDistance: 2]
            }
        }

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

        Person.search().createIndexAndWait()

        render (status: HttpStatus.OK.value(), contentType: Message.CONTENT_TYPE, message as JSON)
    }
}
