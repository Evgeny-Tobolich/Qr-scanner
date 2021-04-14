package tobolich.qr.scanner.domain.scanner.validators

import android.util.Patterns

class IsPhoneValidator : Validator {
    override fun isValid(string: String): Boolean {
        return  Patterns.PHONE.matcher(string).matches()
    }
}