package tobolich.qr.scanner.Main.Package.ui

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import tobolich.qr.scanner.R

class DialogAboutCameraPermission : DialogFragment() {

    companion object {
        fun newInstance(): DialogAboutCameraPermission {
            return DialogAboutCameraPermission()
        }

        const val TAG = "CameraPermissionDialog"
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .apply {
                setTitle(getString(R.string.camera_permission_dialog_title))
                setMessage(getString(R.string.camera_permission_dialog_message))
                setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                    requireActivity().finish()
                }
            }.let(AlertDialog.Builder::create)
            .apply {
                setCancelable(false)
                setCanceledOnTouchOutside(false)
            }
    }
}

