package tobolich.qr.scanner.feature.scanner.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.budiyev.android.codescanner.DecodeCallback

class ScannerViewModel : ViewModel() {

    val liveData: MutableLiveData<DecodeCallback> by lazy {
        MutableLiveData<DecodeCallback>()
    }

    fun scanResult() {
        TODO()
    }
}

