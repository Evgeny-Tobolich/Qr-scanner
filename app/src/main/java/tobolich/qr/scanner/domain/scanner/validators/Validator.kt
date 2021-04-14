package tobolich.qr.scanner.domain.scanner.validators

fun interface Validator {

    fun isValid(string: String): Boolean
}