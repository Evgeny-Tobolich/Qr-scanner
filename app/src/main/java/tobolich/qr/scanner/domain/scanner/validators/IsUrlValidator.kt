package tobolich.qr.scanner.domain.scanner.validators

import android.util.Patterns

class IsUrlValidator : Validator {
    override fun isValid(string: String): Boolean {
        return Patterns.WEB_URL.matcher(string).matches()
    }
}