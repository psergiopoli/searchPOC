package search

import grails.web.RequestParameter

class SearchController extends BaseController{

    DistanceService distanceService
	
    def index(@RequestParameter('order') Boolean order,
              @RequestParameter('search') String search) {

        log.info(request.parameterMap.toMapString())

        List<Person> personList = Person.findAll()
        if (order) {
            distanceService.setFuzzyScore(personList, search)
            distanceService.order(personList)
        }

        respond personList
    }
}
