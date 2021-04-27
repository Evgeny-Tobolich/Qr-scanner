package tobolich.qr.scanner.domain.scanner

import tobolich.qr.scanner.domain.scanner.model.ScanResult
import tobolich.qr.scanner.domain.scanner.model.ScanResult.*
import tobolich.qr.scanner.domain.scanner.validators.IsPhoneValidator
import tobolich.qr.scanner.domain.scanner.validators.IsUrlValidator

class ProcessScanResultInteractor(
    private val isPhoneValidator: IsPhoneValidator,
    private val isUrlValidator: IsUrlValidator
) {

    fun execute(string: String): ScanResult = when {
        isPhoneValidator.isValid(string) -> Phone(string)
        isUrlValidator.isValid(string) -> Url(string)
        else -> Text(string)
    }
}

