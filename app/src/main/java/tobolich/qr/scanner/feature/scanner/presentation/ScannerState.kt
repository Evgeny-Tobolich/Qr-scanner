package tobolich.qr.scanner.feature.scanner.presentation

import tobolich.qr.scanner.domain.scanner.model.ScanResult

sealed class ScannerState {

    object Idle : ScannerState()

    data class Scanned(val scanResult: ScanResult) : ScannerState()
}

