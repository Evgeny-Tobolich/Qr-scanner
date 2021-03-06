package tobolich.qr.scanner.domain.scanner.validators

import android.telephony.PhoneNumberUtils
import android.util.Patterns

class IsPhoneValidator : Validator {
    override fun isValid(string: String): Boolean {
        val number = PhoneNumberUtils.stripSeparators(string)
        return Patterns.PHONE.matcher(number).matches()
    }
}