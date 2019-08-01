package hanzhou.easyledger_demo.chart_personalization;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Utils;

import hanzhou.easyledger_demo.R;
import hanzhou.easyledger_demo.utility.Constant;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 * <p>
 * modified by Han Zhou
 */

public class MyMarkerView extends MarkerView {

    private final TextView tvContent;

    private String[] mCategories;


    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);

    }


    public MyMarkerView(Context context, int layoutResource, String[] categories) {
        super(context, layoutResource);

        tvContent = findViewById(R.id.tvContent);
        mCategories = categories;
    }

    // runs every time the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @SuppressLint("SetTextI18n")
    @Override
    public void refreshContent(Entry e, Highlight highlight) {


        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;

            if (mCategories != null) {
                int position = (int) e.getX();
                String temp = mCategories[position];
                tvContent.setText(
                        temp + Constant.TXT_NEW_LINE +
                                getResources().getString(R.string.dollor_sign) +
                                Utils.formatNumber(ce.getHigh(), 0, false));

            } else {
                tvContent.setText(getResources().getString(R.string.dollor_sign) + Utils.formatNumber(ce.getHigh(), 0, false));
            }


        } else {

            if (mCategories != null) {
                int position = (int) e.getX();
                String temp = mCategories[position];
                tvContent.setText(
                        temp + Constant.TXT_NEW_LINE +
                                getResources().getString(R.string.dollor_sign) +
                                Utils.formatNumber(e.getY(), 0, false));

            } else {
                tvContent.setText(getResources().getString(R.string.dollor_sign) + Utils.formatNumber(e.getY(), 0, false));

            }
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
