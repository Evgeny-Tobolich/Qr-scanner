package tobolich.qr.scanner.domain.scanner.model

import java.io.Serializable

sealed class ScanResult {
    abstract val string: String

    data class Url(override val string: String) : ScanResult(), Serializable
    data class Phone(override val string: String) : ScanResult(), Serializable
    data class Text(override val string: String) : ScanResult(), Serializable
}


