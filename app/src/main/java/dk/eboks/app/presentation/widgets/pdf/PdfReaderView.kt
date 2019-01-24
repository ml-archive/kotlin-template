package dk.eboks.app.presentation.widgets.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.MotionEvent.INVALID_POINTER_ID
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Scroller
import timber.log.Timber


/**
 * Created by bison on 12-02-2018.
 */
class PdfReaderView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attributes: AttributeSet?) : super(context, attributes) {
        init()
    }

    constructor(context: Context?, attributes: AttributeSet?, defStyle: Int) : super(context, attributes, defStyle) {
        init()
    }

    private lateinit var renderer: AsyncPdfRenderer

    private var posX: Float = 0f
    private var posY: Float = 0f

    private var dragStartY: Float = 0f

    private var lastTouchX: Float = 0f
    private var lastTouchY: Float = 0f
    private var activePointerId = INVALID_POINTER_ID

    private var scaleDetector: ScaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    private var scaleFactor = 1f
    private val scroller = Scroller(context)
    private var scrollOffset = 0f

    var isInitialized = false

    var currentPageNo = 0
    var bitmap: Bitmap? = null

    var filename: String = ""
    lateinit var paint: Paint

    private fun init() {
        Timber.e("Init")
        isInitialized = true
        paint = Paint()
        paint.strokeWidth = context.resources.displayMetrics.density * 4
        paint.flags = Paint.ANTI_ALIAS_FLAG
        //paint.setStrokeCap();
        paint.color = Color.BLACK
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        Timber.e("onAttachedToWindow")
        renderer = AsyncPdfRenderer(context)
        renderer.listener = object : AsyncPdfRenderer.PdfRendererListener {
            override fun onPageLoaded(bitmap: Bitmap, pageNumber: Int) {
                Timber.d("Page loaded: $pageNumber")
                this@PdfReaderView.bitmap = bitmap
                this@PdfReaderView.invalidate()
            }
        }

    }

    fun showFile(filename: String) {
        Timber.d("show file $filename")
        renderer.start(filename)
        renderer.requestPage(0)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Timber.e("onDetachedFromWindow")
        renderer.finish()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        Timber.d("Draw")
        //Timber.e("onDraw ${canvas?.width} x ${canvas?.height} scale = $scaleFactor pan = $posX,$posY")
        //Timber.e("scrollOffset $scrollOffset")
        canvas?.let { c ->
            if (bitmap != null) {
                Timber.d("Draw bitmap")
                c.save()
                c.translate(posX, scrollOffset)
                c.scale(scaleFactor, scaleFactor)
                c.drawBitmap(bitmap, 0f, 0f, null)
                c.restore()
            }

        }

        /*
        pdfRenderer?.let { renderer ->
            currentPage = renderer.openPage(currentPageNo)
            currentPage?.let { page ->
                Timber.e("Rendering pdf ${page.width}X${page.height}")
                val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
                // Here, we render the page onto the Bitmap.
                // To render a portion of the page, use the second and third parameter. Pass nulls to get
                // the default result.
                // Pass either RENDER_MODE_FOR_DISPLAY or RENDER_MODE_FOR_PRINT for the last parameter.
                page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                canvas?.drawBitmap(bitmap, 0f, 0f, null)
            }
        }
        */
        if (!scroller.isFinished) {
            scroller.computeScrollOffset()
            scrollOffset = scroller.currY.toFloat()
            Timber.e("scrollOffset $scrollOffset")
            postInvalidate()
        }
        //else
        //    scrollOffset = scroller.currY.toFloat()

    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        // Let the ScaleGestureDetector inspect all events.
        scaleDetector.onTouchEvent(ev)

        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Timber.e("ACTION_DOWN")
                //if(!scroller.isFinished)
                //    scroller.forceFinished(true)
                val x = ev.x
                val y = ev.y
                dragStartY = y
                lastTouchX = x
                lastTouchY = y
                activePointerId = ev.getPointerId(0)
            }

            MotionEvent.ACTION_MOVE -> {
                val pointerIndex = ev.findPointerIndex(activePointerId)
                val x = ev.getX(pointerIndex)
                val y = ev.getY(pointerIndex)
                val dx = x - lastTouchX
                val dy = y - lastTouchY

                // Only move if the ScaleGestureDetector isn't processing a gesture.
                if (scaleDetector.isInProgress) {
                    posX += dx
                    posY += dy

                    invalidate()
                } else {
                    Timber.e("Starting scroll")
                    scrollOffset += dy
                    scroller.startScroll(0, (scrollOffset).toInt(), 0, (dy * 5f).toInt())
                    /*
                    val dy = y - lastTouchY
                    scrollOffset += dy
                    */
                    invalidate()
                }

                lastTouchX = x
                lastTouchY = y
            }

            MotionEvent.ACTION_UP -> {
                Timber.e("ACTION_UP")
                val pointerIndex = ev.findPointerIndex(activePointerId)
                val y = ev.getY(pointerIndex)
                activePointerId = INVALID_POINTER_ID
                if (!scaleDetector.isInProgress) {

                }
            }

            MotionEvent.ACTION_CANCEL -> {
                Timber.e("ACTION_CANCEL")
                activePointerId = INVALID_POINTER_ID
            }

            MotionEvent.ACTION_POINTER_UP -> {
                Timber.e("ACTION_POINTER_UP")
                val pointerIndex = ev.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
                val pointerId = ev.getPointerId(pointerIndex)
                if (pointerId == activePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    val newPointerIndex = if (pointerIndex == 0) 1 else 0
                    lastTouchX = ev.getX(newPointerIndex)
                    lastTouchY = ev.getY(newPointerIndex)
                    activePointerId = ev.getPointerId(newPointerIndex)
                }
            }
        }
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            scroller.forceFinished(true)
            scaleFactor *= detector.scaleFactor
            Timber.d("onScael")
            // Don't let the object get too small or too large.
            scaleFactor = Math.max(0.50f, Math.min(scaleFactor, 2.0f))

            invalidate()
            return true
        }
    }
}