package tobolich.qr.scanner.domain.scanner.validators

interface Validator {

    fun isValid(string: String): Boolean
}