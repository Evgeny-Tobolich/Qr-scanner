package tobolich.qr.scanner.common.dialogs

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tobolich.qr.scanner.R

class ErrorDialog : DialogFragment() {

    private val message: String by lazy { "${arguments?.getString(ARG_MESSAGE)}" }

    companion object {
        val TAG = ErrorDialog::class.java.simpleName
        private const val ARG_MESSAGE = "ARG_MESSAGE"

        fun newInstance(throwable: Throwable): ErrorDialog {
            return newInstance(throwable.message.toString())
        }

        fun newInstance(message: String): ErrorDialog {
            return ErrorDialog().apply {
                arguments = Bundle().apply {
                    putString(ARG_MESSAGE, message)
                }
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.error_dialog_title))
            .setMessage(message)
            .setPositiveButton(getString(android.R.string.ok)) { _, _ -> }
            .create()
}


