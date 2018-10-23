package search

import org.ajbrown.namemachine.NameGenerator

class BootStrap {

    private static final MAX_PERSON = 4000

    def init = { servletContext ->

        NameGenerator generator = new NameGenerator();

        Person.withTransaction {
            if (Person.findAll().isEmpty()) {
                String oldName = 'Paulo'
                String last = 'Junior'

                for(int i=0; i < MAX_PERSON; i++) {
                    String newName = generator.generateName()

                    if (i.mod(1000) == 0){
                        last = generator.generateName()
                        println "i:${i}"
                    }

                    new Person(name: "${newName} ${oldName} ${last}")
                            .save(flush:true, failOnError: true)

                    oldName = newName
                }
            }
        }
    }
    def destroy = {
    }
}
