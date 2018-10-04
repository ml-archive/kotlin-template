package dk.eboks.app.presentation.ui.login.components

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.domain.config.Config
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.ui.login.screens.PopupLoginActivity
import kotlinx.android.synthetic.main.fragment_activation_code_component.*
import kotlinx.android.synthetic.main.fragment_device_activation_component.*
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class DeviceActivationComponentFragment : BaseFragment(), DeviceActivationComponentContract.View {
    @Inject
    lateinit var presenter: DeviceActivationComponentContract.Presenter

    var mHandler = Handler()

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_device_activation_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        mHandler.removeCallbacksAndMessages(null)
        super.onPause()
    }

    override fun showProgress(show: Boolean) {
        activateDevicebuttonGroupLl.visibility = if(show) View.INVISIBLE else View.VISIBLE
        activateDeviceProgressFl.visibility = if(!show) View.GONE else View.VISIBLE
    }

    override fun setupButtons() {
        activateBtn.setOnClickListener {
                presenter.requestNemidLogin()
        }

        skipBtn.setOnClickListener {
                closeDrawer()
        }
    }

    override fun requestNemidLogin() {
        var intent = Intent(context, PopupLoginActivity::class.java)
        intent.putExtra("selectedLoginProviderId", Config.getVerificationProviderId())
        startActivityForResult(intent,770 )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       when (requestCode){
           770 -> {
               if ( resultCode == Activity.RESULT_OK){
                   presenter.activateDevice()
               }
           }
       }
    }

    override fun closeDrawer() {
        getBaseActivity()?.onBackPressed()
    }
}