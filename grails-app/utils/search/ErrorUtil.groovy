package search

import grails.validation.ValidationException
import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory
import org.springframework.context.MessageSource
import org.springframework.validation.ObjectError

@Singleton
class ErrorUtil {

    static final List<String> getErrorMessages(ValidationException e, MessageSource messageSource, Boolean log = false) {
        List<String> errorsList = new ArrayList<String>()
        Log logger = LogFactory.getLog(getClass())

        e.errors.allErrors.each { ObjectError error ->
            String errorMessage = messageSource.getMessage(error, null)
            if (log) {
                logger.error(errorMessage)
            }
            errorsList.add(errorMessage)
        }

        return errorsList
    }

}