package tobolich.qr.scanner

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import kotlin.system.exitProcess

class DialogAboutCameraPermission : DialogFragment() {

    companion object {
        const val CAMERA_DIALOG_TAG = "PurchaseConfirmationDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.dialog_message))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ -> exitProcess(0) }
            .create()
    }
}

