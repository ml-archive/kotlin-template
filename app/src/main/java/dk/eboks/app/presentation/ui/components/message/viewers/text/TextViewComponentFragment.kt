package dk.eboks.app.presentation.ui.components.message.viewers.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_textview_component.*
import timber.log.Timber
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class TextViewComponentFragment : BaseFragment(), TextViewComponentContract.View {

    @Inject
    lateinit var presenter : TextViewComponentContract.Presenter

    internal var data: ByteArray? = null
    internal var name: String = ""
    internal var format: String = ""

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val rootView = inflater?.inflate(R.layout.fragment_pasta_component, container, false)
        return rootView
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)

        val args = arguments
        if (args == null) {
            Timber.e("textviewer received no arguments, not much to do")
            return
        }
        name = args.getString("name")
        format = args.getString("format")
        data = args.getByteArray("data")
        show()
    }

    override fun setupTranslations() {

    }

    private fun show()
    {
        try {

            var decoded = String(data!!, Charset.forName("ISO-8859-1"))
            if (utf8Test(decoded)) {
                Timber.i("text was not encoded with iso-8859-1, using utf-8 as fallback")
                decoded = String(data!!, Charset.forName("utf-8"))
            }
            contentTv.text = decoded

        } catch (e: UnsupportedEncodingException) {
            contentTv.visibility = View.GONE

        }

    }


    @Throws(UnsupportedEncodingException::class)
    private fun utf8Test(latin1String: String): Boolean
    {
        val charset = Charset.forName("ISO-8859-1")
        val test1 = String("æ".toByteArray(), charset)     // shown as Ã¦
        val test2 = String("Æ".toByteArray(), charset)     // shown as Ã
        val test3 = String("ø".toByteArray(), charset)     // shown as Ã¸
        val test4 = String("Ø".toByteArray(), charset)     // shown as Ã
        val test5 = String("å".toByteArray(), charset)     // shown as Ã¥
        val test6 = String("Å".toByteArray(), charset)     // shown as Ã
        val test7 = String("ä".toByteArray(), charset)     // shown as Ã¤
        val test8 = String("Ä".toByteArray(), charset)     // shown as Ã
        val test9 = String("ö".toByteArray(), charset)     // shown as Ã¶
        val test10 = String("Ö".toByteArray(), charset)    // shown as Ã

        if (latin1String.contains(test1) || latin1String.contains(test2) ||
                latin1String.contains(test3) || latin1String.contains(test4) ||
                latin1String.contains(test5) || latin1String.contains(test6) ||
                latin1String.contains(test7) || latin1String.contains(test8) ||
                latin1String.contains(test9) || latin1String.contains(test10)) {
            return true
        }
        else
            return false
    }

}