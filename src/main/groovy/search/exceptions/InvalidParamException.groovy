package search.exceptions

class InvalidParamException extends RuntimeException {

    Integer status

    InvalidParamException(String message, Integer status) {
        super(message)
        this.status = status
    }
}
