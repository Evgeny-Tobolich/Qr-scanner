package tobolich.qr.scanner

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class DialogFragment : DialogFragment() {

    companion object {
        const val TAG = "PurchaseConfirmationDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
            AlertDialog.Builder(requireContext())
                    .setMessage(getString(R.string.dialog_message))
                    .setPositiveButton(getString(R.string.ok)) {_,_ -> }
                    .create()

}
