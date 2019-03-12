package dk.eboks.app.presentation.ui.message.components.viewers.text

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import dk.eboks.app.R
import dk.eboks.app.presentation.base.BaseFragment
import dk.eboks.app.presentation.base.ViewerFragment
import dk.eboks.app.mail.presentation.ui.message.components.viewers.base.EmbeddedViewer
import dk.eboks.app.mail.presentation.ui.message.components.viewers.text.TextViewComponentContract
import dk.eboks.app.util.visible
import dk.nodes.filepicker.uriHelper.FilePickerUriHelper
import kotlinx.android.synthetic.main.fragment_textview_component.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.Charset
import javax.inject.Inject

/**
 * Created by bison on 09-02-2018.
 */
class TextViewComponentFragment : BaseFragment(), TextViewComponentContract.View, EmbeddedViewer,
        ViewerFragment {

    @Inject
    lateinit var presenter: TextViewComponentContract.Presenter

    internal var data: ByteArray? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_textview_component, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        component.inject(this)
        presenter.onViewCreated(this, lifecycle)
        presenter.setup(arguments?.getString("URI"))
    }

    override fun showText(filename: String) {
        GlobalScope.launch(Dispatchers.Default) {
            data = convertFileToByteArray(File(filename))
            data?.let {
                // show()
                var decoded = String(data!!, Charset.forName("ISO-8859-1"))
                if (utf8Test(decoded)) {
                    Timber.i("text was not encoded with iso-8859-1, using utf-8 as fallback")
                    decoded = String(data!!, Charset.forName("utf-8"))
                }

                launch(Dispatchers.Main) {
                    Timber.e("Calling show")
                    show(decoded)
                }
            }
        }
    }

    override fun showTextURI(uri: String) {
        Timber.e("Attempting to open URI $uri")
        val file = FilePickerUriHelper.getFile(activity ?: return, uri)
        showText(file.path)
    }

    private fun convertFileToByteArray(f: File): ByteArray? {
        var byteArray: ByteArray? = null
        try {
            val inputStream = FileInputStream(f)
            val bos = ByteArrayOutputStream()
            val b = ByteArray(1024 * 8)
            var bytesRead = 0

            while (true) {
                bytesRead = inputStream.read(b)
                if (bytesRead == -1)
                    break
                bos.write(b, 0, bytesRead)
            }

            byteArray = bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return byteArray
    }

    private fun show(decoded: String) {
        progresspb.visible = (false)
        contentTv.visible = (true)
        try {
            Timber.e("Setting textview")
            contentTv.text = decoded
            Timber.e("Done setting textview")
        } catch (e: UnsupportedEncodingException) {
            contentTv.visibility = View.GONE
        }
    }

    @Throws(UnsupportedEncodingException::class)
    private fun utf8Test(latin1String: String): Boolean {
        val charset = Charset.forName("ISO-8859-1")
        val test1 = String("æ".toByteArray(), charset) // shown as Ã¦
        val test2 = String("Æ".toByteArray(), charset) // shown as Ã
        val test3 = String("ø".toByteArray(), charset) // shown as Ã¸
        val test4 = String("Ø".toByteArray(), charset) // shown as Ã
        val test5 = String("å".toByteArray(), charset) // shown as Ã¥
        val test6 = String("Å".toByteArray(), charset) // shown as Ã
        val test7 = String("ä".toByteArray(), charset) // shown as Ã¤
        val test8 = String("Ä".toByteArray(), charset) // shown as Ã
        val test9 = String("ö".toByteArray(), charset) // shown as Ã¶
        val test10 = String("Ö".toByteArray(), charset) // shown as Ã

        return latin1String.contains(test1) || latin1String.contains(test2) ||
            latin1String.contains(test3) || latin1String.contains(test4) ||
            latin1String.contains(test5) || latin1String.contains(test6) ||
            latin1String.contains(test7) || latin1String.contains(test8) ||
            latin1String.contains(test9) || latin1String.contains(test10)
    }

    override fun print() {
        Timber.e("Print called")
    }
}