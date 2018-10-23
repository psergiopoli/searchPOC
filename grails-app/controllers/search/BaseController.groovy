package search

import grails.converters.JSON
import grails.validation.ValidationException
import org.springframework.context.MessageSource
import org.springframework.http.HttpStatus
import search.exceptions.InvalidParamException

class BaseController {

    MessageSource messageSource

    def handleInvalidParamException(InvalidParamException e) {
        log.error(e.getMessage(), e)
        Message message = new Message(
                message: messageSource.getMessage(e.getMessage(), null , null),
                code: e.getMessage()
        )
        render (status: e.status, contentType: Message.CONTENT_TYPE, message as JSON)
    }

    def handleValidationException(ValidationException e) {
        log.error(e.getMessage(), e)
        List<String> errors = ErrorUtil.instance.getErrorMessages(e, messageSource, false)
        List<Message> messages = new ArrayList<>()
        errors.each { String error ->
            messages.add(new Message(
                    message: error,
                    code: "search.error.validation"
            ))
        }
        render (status: HttpStatus.BAD_REQUEST.value(), contentType: Message.CONTENT_TYPE, messages as JSON)
    }

    def handleGenericException(Exception e) {
        log.error(e.getMessage(), e)
        Message message = new Message(
                message: messageSource.getMessage('search.error.internal', null , null),
                code: "search.error.internal"
        )
        render (status: HttpStatus.INTERNAL_SERVER_ERROR.value(), contentType: Message.CONTENT_TYPE, message as JSON)
    }
}

