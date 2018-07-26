package dk.eboks.app.presentation.ui.uploads.components.uploadfile

import android.os.Bundle
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.models.Translation
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.sheet_file_upload.*
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class UploadFileComponentFragment : BaseFragment(), UploadFileComponentContract.View {

    @Inject
    lateinit var presenter: UploadFileComponentContract.Presenter

    var fileNameValid = false
    var handler = Handler()

    //todo *mock* should be changed to the correct appstate
    var userVerified = true

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.sheet_file_upload, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        setupValidation()
        setupChoseFolder()
        setupButtons()
    }

    private fun setupButtons() {
        saveBtn.setOnClickListener {
            //todo saveBtn clicked
            var temp = "_saveBtn Clicked!"
            Timber.d(temp)
        }
        cancelBtn.setOnClickListener {
            //todo cancelBtn clicked
            var temp = "_cancelBtn Clicked!"
            Timber.d(temp)
        }
        chooseFolderLl.setOnClickListener {
            //todo chose folder clicked
            var temp = "_chose folder Clicked!"
            Timber.d(temp)
        }
    }

    private fun setupChoseFolder() {
        chooseFolderLl.isClickable = userVerified
        if (userVerified) {
            chooseFolderLl.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.rounded_white_background))
            chooseFolderLl.isClickable = true
            setTvTextColors(R.color.darkGreyBlue)
        } else {
            chooseFolderLl.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.rounded_darkgrey_background))
            chooseFolderLl.isClickable = false
            setTvTextColors(R.color.silver)
        }
    }

    private fun setTvTextColors(color: Int) {
        folderTv.setTextColor(resources.getColor(color))
        destinationTv.setTextColor(resources.getColor(color))
    }

    private fun setupValidation() {

        fileNameEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                fileNameTil.error = null
                //todo could optimize this validator for a more accurate check if the file name is valid
                fileNameValid = !(fileNameEt.text.toString().trim().isNullOrBlank())
                saveBtn.isEnabled = fileNameValid
                if(!fileNameValid){
                    //todo in the design the hint changes color. Not sure if they want that or the more android way
                    fileNameTil.error = Translation.uploads.incorrectFileName
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

}