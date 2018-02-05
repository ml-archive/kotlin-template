package dk.eboks.app.util;

import timber.log.Timber;

/**
 * Created by bison on 18-11-2016.
 */

public class MathUtil {
    public static final String TAG = MathUtil.class.getSimpleName();

    public static double reMapDouble(double oMin, double oMax, double nMin, double nMax, double x){
//range check
        if( oMin == oMax) {
            Timber.e("Warning: Zero input range");
            return -1;    }

        if( nMin == nMax){
            Timber.e(TAG, "Warning: Zero output range");
            return -1;        }

//check reversed input range
        boolean reverseInput = false;
        double oldMin = Math.min(oMin, oMax );
        double oldMax = Math.max(oMin, oMax );
        if (oldMin == oMin)
            reverseInput = true;

//check reversed output range
        boolean reverseOutput = false;
        double newMin = Math.min(nMin, nMax );
        double newMax = Math.max(nMin, nMax );
        if (newMin == nMin)
            reverseOutput = true;

        double portion = (x-oldMin)*(newMax-newMin)/(oldMax-oldMin);
        if (reverseInput)
            portion = (oldMax-x)*(newMax-newMin)/(oldMax-oldMin);

        double result = portion + newMin;
        if (reverseOutput)
            result = newMax - portion;

        return result;
    }

    /**
     * Would have been faster and a lot less lazier to just reimplement the above function with floats
     * @param oMin
     * @param oMax
     * @param nMin
     * @param nMax
     * @param x
     * @return
     */
    public static float reMapFloat(float oMin, float oMax, float nMin, float nMax, float x){
        return (float) reMapDouble((double) oMin, (double) oMax, (double) nMin, (double) nMax, (double) x);
    }

    public static float lerp(float a, float b, float f)
    {
        return (a * (1.0f - f)) + (b * f);
    }
}
