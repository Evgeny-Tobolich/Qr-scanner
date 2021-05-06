package tobolich.qr.scanner.feature.scanner.presentation

sealed class ScannerState

class Idle(string: String) : ScannerState()

class Scaned(string: String) : ScannerState()

fun checkScannerState(scannerState: ScannerState) {}

