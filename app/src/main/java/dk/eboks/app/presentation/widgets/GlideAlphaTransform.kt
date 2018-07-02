package dk.eboks.app.presentation.widgets

import android.graphics.*
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import timber.log.Timber
import java.security.MessageDigest

/**
 * Creates a "sandwich", consisting of
 * 1) A background color,
 * 2) The image, with optional translucency
 * 3) A gray, skewed gradient in OVERLAY porter-duff mode
 * @param color which color to use for the background
 * @param alpha the translucency of the image. The lower, the more of the background color will shine through.
 * Default is 20%
 *
 * @author   Christian
 * @since    5/14/2018.
 */
class GlideAlphaTransform(val color: Int, val alpha: Float = 0.2f) : BitmapTransformation() {

    val ID = "AlphaTransform"

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + color).toByteArray(CHARSET))
    }

    override fun transform(pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int): Bitmap {
        val config = toTransform.config ?: Bitmap.Config.ARGB_8888
        val bitmap = pool.get(toTransform.width, toTransform.height, config)

        // Background
        val canvas = Canvas(bitmap)
        Timber.v("Color = $color")
        val solid = color.or(0xff000000.toInt())
        Timber.v("Solid Color = $solid")
        canvas.drawColor( solid )


        // Image
        val alphaPaint = Paint()
        alphaPaint.isAntiAlias = true
        alphaPaint.alpha = (alpha * 255).toInt()
        canvas.drawBitmap(toTransform, 0.0f, 0.0f, alphaPaint)

        // Foreground
        val shaderG = LinearGradient(0.0f, 0.0f, bitmap.width.toFloat(), bitmap.height.toFloat() * 0.5f, 0xff999999.toInt(), 0xff666666.toInt(), Shader.TileMode.CLAMP)
        val gradientPaint = Paint()
        gradientPaint.xfermode = PorterDuffXfermode(PorterDuff.Mode.OVERLAY)
        gradientPaint.shader = shaderG
        canvas.drawPaint(gradientPaint)

        return bitmap
    }

    override fun hashCode(): Int {
        return ID.hashCode() + color * 10
    }

    override fun equals(other: Any?): Boolean {
        return other is GlideAlphaTransform && other.color == color
    }
}