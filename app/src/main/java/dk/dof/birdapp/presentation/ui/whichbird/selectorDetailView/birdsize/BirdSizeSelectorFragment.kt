package dk.dof.birdapp.presentation.ui.whichbird.selectorDetailView.birdsize

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import dk.dof.birdapp.R
import dk.dof.birdapp.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_selector_birdsize.*
import javax.inject.Inject

class BirdSizeSelectorFragment : BaseFragment(), SeekBar.OnSeekBarChangeListener, BirdSizeSelectorContract.View {

    @Inject
    lateinit var presenter : BirdSizeSelectorContract.Presenter

    var selectedStep = 3
    val sizes = arrayOf(
            "_på størrelse med en blåmejse eller mindre",
            "_Størrelse mellem blåmejse og solsort",
            "_På størrelse med en solsort",
            "_Størrelse mellem solsort og krage",
            "_På størrelse med en krage",
            "_størrelse mellem krage og gås",
            "_På størrelse med gås eller større")


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_selector_birdsize, container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)



        setupSeekbar()
    }

    private fun setupSeekbar() {
        val steps = 6
        seekBar.max = steps
        seekBar1.max = steps
        seekBar.progress = selectedStep
        seekBar1.progress = selectedStep
        handleBirdColor(selectedStep)

        seekBar1.setOnTouchListener( { v, event ->
            true
        })

        seekBar.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        handleBirdColor(progress)
        seekBar1.setProgress(progress)
    }

    private fun handleBirdColor(step: Int) {

        context?.let {
            val blackColor = ContextCompat.getColor(it, R.color.color_dof_birdguide_black)
            val greyColor = ContextCompat.getColor(it, R.color.color_dof_birdguide_grey)
            val darkerGreyColor = ContextCompat.getColor(it,R.color.color_dof_birdguide_darkergrey)

            // silhouttes
            imvBird1.setColorFilter(if (step <= 1) blackColor else greyColor)
            imvBird2.setColorFilter(if (step >= 1 && step <= 3) blackColor else greyColor)
            imvBird3.setColorFilter(if (step >= 3 && step <= 5) blackColor else greyColor)
            imvBird4.setColorFilter(if (step >= 5) blackColor else greyColor)

            //Bird size labels
            tvFindBirdSmallSizeLabel.setTextColor(if (step <= 1) blackColor else darkerGreyColor)
            tvFindBirdMediumSizeLabel.setTextColor(if (step >= 2 && step <= 4) blackColor else darkerGreyColor)
            tvFindBirdBigSizeLabel.setTextColor(if (step >= 5) blackColor else darkerGreyColor)

            tvSelectedSizeDescription.text = sizes[step]
            selectedStep = step
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}