package dk.eboks.app.presentation.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;

import dk.eboks.app.R;

/**
 * Created by bison on 23/11/15.
 */
public class DottedViewPager extends android.support.v4.view.ViewPager {
    private static final String TAG = DottedViewPager.class.getSimpleName();
    Drawable InactiveDot;
    Drawable activeDot;
    int circleSize;
    int noCircles;
    int curCircle = 0;
    int margin;
    int marginBottom;
    float density;
    int screenWidth;


    public DottedViewPager(Context context) {
        super(context);
        init();
    }

    public DottedViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init()
    {
        density = getResources().getDisplayMetrics().density;
        screenWidth = getResources().getDisplayMetrics().widthPixels;
        activeDot = getResources().getDrawable(R.drawable.circle_filled);
        InactiveDot = getResources().getDrawable(R.drawable.circle_outline);
        circleSize = (int) (density * 6.0f);
        margin = (int) (density * 5.0f);
        marginBottom = (int) (density * 7f);
    }

    public void setMarginBottom(int dp)
    {
        marginBottom = (int) (density * (float) dp);
    }

    public Drawable getInactiveDot() {
        return InactiveDot;
    }

    public void setInactiveDot(Drawable inactiveDot) {
        InactiveDot = inactiveDot;
    }

    public Drawable getActiveDot() {
        return activeDot;
    }

    public void setActiveDot(Drawable activeDot) {
        this.activeDot = activeDot;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(noCircles == 1){
            return;
        }

        int horiz_off = this.computeHorizontalScrollOffset();
        curCircle = getCurrentItem();

        //NLog.e(TAG, "dispatchDraw curCircle = " + curCircle + " noCircles = " + noCircles);
        int span_w = noCircles * (circleSize + margin);
        int startx = screenWidth/2 - (span_w/2);
        int offx = 0;
        //int offy = getHeight() - marginBottom - circleSize;
        int offy = getHeight() + (int) (density * 8f);


        for(int i=0; i < noCircles; i++)
        {
            offx = startx + (i * (circleSize + margin));
            if(i != curCircle) {
                InactiveDot.setBounds(horiz_off + offx, offy, horiz_off + offx + circleSize, offy + circleSize);
                InactiveDot.draw(canvas);
            }
            else
            {
                activeDot.setBounds(horiz_off + offx, offy, horiz_off + offx + circleSize, offy + circleSize);
                activeDot.draw(canvas);
            }
        }

    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        super.setAdapter(adapter);
        noCircles = adapter.getCount();
    }
}
