package dk.eboks.app.presentation.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.TypedValue;

import dk.eboks.app.R;
import timber.log.Timber;

public class MaxHeightScrollView extends NestedScrollView {

    private int maxHeight;
    private final int defaultHeight = 300;
    private int topPaddingPixel;

    public MaxHeightScrollView(Context context) {
        super(context);
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    public MaxHeightScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (!isInEditMode()) {
            init(context, attrs);
        }
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            topPaddingPixel = (int) (context.getResources().getDisplayMetrics().density * 48.0f);
            TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightScrollView);
            //200 is a defualt value
            int defMaxHeight = context.getResources().getDisplayMetrics().heightPixels;
            // Calculate ActionBar height

            TypedValue tv = new TypedValue();
            int actionBarHeight = 0;
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
            {
                actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
            }
            else
            {
                actionBarHeight = (int) (getResources().getDisplayMetrics().density * 56.0f);
            }

            defMaxHeight = defMaxHeight - actionBarHeight - topPaddingPixel;
            maxHeight = styledAttrs.getDimensionPixelSize(R.styleable.MaxHeightScrollView_maxHeight, defMaxHeight);
            styledAttrs.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int mHeight = Math.min(MeasureSpec.getSize(heightMeasureSpec), maxHeight);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(mHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}