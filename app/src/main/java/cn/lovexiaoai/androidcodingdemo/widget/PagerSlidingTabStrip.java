
package cn.lovexiaoai.androidcodingdemo.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import cn.lovexiaoai.androidcodingdemo.R;
import cn.lovexiaoai.androidcodingdemo.utils.ScreenUtil;

/**
 * Created by xiaoai on 2018/8/14.
 * 可滑动标签
 */
public class PagerSlidingTabStrip extends HorizontalScrollView {

    public interface ViewTabProvider {
        View getTabView(int position);
    }

    public interface TagTabProvider {
        boolean hasRedPoint(int position);

        boolean hasNewTag(int position);
    }

    public interface IconTabProvider {
        int getPageIconResId(int position);
    }

    public interface TabOnClickListener {
        void OnTabOnClick(int position);
    }

    public interface ShowArrowListener {
        void showArrow();
    }

    // @formatter:off
    private static final int[] ATTRS = new int[]{
            android.R.attr.textSize,
            android.R.attr.textColor
    };
    // @formatter:on

    private ShowArrowListener showArrowListener;

    private static final int TITLE_OFFSET_DIPS = 24;

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    public OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition = 0;
    private float currentPositionOffset = 0f;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = 0xFF666666;
    private int underlineColor = 0x1A000000;
    private int dividerColor = 0x1A000000;

    private boolean shouldExpand = false;
    private boolean textAllCaps = true;
    private boolean selectBoldStyle = true;

    private int scrollOffset = 52;
    private int indicatorHeight = 8;
    private int underlineHeight = 2;
    private int dividerPadding = 12;
    private int tabPadding = 24;
    private int marginLeftRight = 0;
    private int indicatorpadding = 0;
    //private int margins = 0;

    private int dividerWidth = 1;

    private int tabTextSize = 14;
    private int tabTextColor = 0x181818;
    private Typeface tabTypeface = null;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int selectTextColor = 0;

    private int lastScrollX = 0;

    private int mWidth;

    private int mTitleOffset;

    private int tabBackgroundResId = R.drawable.xa_background_tab;

    private TabOnClickListener mTabOnClickListener;

    private Locale locale;

    private List<ImageButton> icons = new ArrayList<ImageButton>();

    private List<TextView> textViews = new ArrayList<TextView>();

    private DisplayMetrics dm;

    private int widthNum = -1;//新增 一屏宽显示几个tab
    private boolean hasHalfTab = false;//是否显示半个Tab

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);

        dm = getResources().getDisplayMetrics();

        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
        mWidth = dm.widthPixels;
        mTitleOffset = (int) (TITLE_OFFSET_DIPS * dm.density);
        // get system attrs (android:textSize and android:textColor)

        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);

        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColor(1, tabTextColor);

        a.recycle();

        // get custom attrs

        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);

        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsIndicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsUnderlineColor, underlineColor);
        dividerColor = a.getColor(R.styleable.PagerSlidingTabStrip_pstsDividerColor, dividerColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsIndicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsUnderlineHeight, underlineHeight);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsDividerPadding, dividerPadding);
        tabPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsTabPaddingLeftRight, tabPadding);
        tabBackgroundResId = a.getResourceId(R.styleable.PagerSlidingTabStrip_pstsTabBackground, tabBackgroundResId);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsShouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_pstsScrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_pstsTextAllCaps, textAllCaps);

        a.recycle();

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);

        if (locale == null) {
            locale = getResources().getConfiguration().locale;
        }
    }

    public void setTabOnClcikListener(TabOnClickListener tabOnClickListener) {
        this.mTabOnClickListener = tabOnClickListener;
    }

    public void setShowArrowListener(ShowArrowListener showArrowListener) {
        this.showArrowListener = showArrowListener;
    }

    public void setViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.addOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        icons.clear();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof IconTabProvider) {
                addIconTab(i, pager.getAdapter().getPageTitle(i).toString(), ((IconTabProvider) pager.getAdapter()).getPageIconResId(i));
            } else if (pager.getAdapter() instanceof TagTabProvider) {
                // TODO: by xiaoai at 2018/8/14
                TagTabProvider provider = (TagTabProvider) pager.getAdapter();
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            } else if (pager.getAdapter() instanceof ViewTabProvider) {
                ViewTabProvider provider = (ViewTabProvider) pager.getAdapter();
                addViewTab(i, provider.getTabView(i));
            } else {
                addTextTab(i, pager.getAdapter().getPageTitle(i).toString());
            }
        }

        updateTabStyles();

        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @SuppressLint("NewApi")
            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if (getWidth() > mWidth - ScreenUtil.dip2px(getContext(), 38)) {
                    if (showArrowListener != null) {
                        showArrowListener.showArrow();
                    }
                }
                currentPosition = pager.getCurrentItem();
                scrollToChild(currentPosition, 0);
            }
        });

    }

    protected void addTextTab(final int position, String title) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        //tab.setIncludeFontPadding(false);
        addTab(position, tab);
        textViews.add(position, tab);
    }

    protected void addIconTab(final int position, String title, int resId) {
        TextView tab = new TextView(getContext());
        tab.setText(title);
        tab.setGravity(Gravity.CENTER);
        tab.setSingleLine();
        if (resId != 0) {
            Drawable drawable = getContext().getResources().getDrawable(resId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            tab.setCompoundDrawables(null, null, drawable, null);
            int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, dm);
            tab.setCompoundDrawablePadding(padding);
        }
        addTab(position, tab);
        textViews.add(position, tab);
    }

    private void addViewTab(final int position, View tab) {
        addTab(position, tab);
    }

    private void addTab(final int position, View tab) {
        RelativeLayout rl = new RelativeLayout(getContext());
        MarginLayoutParams mp;
        if (widthNum != -1) {
            float mIndicatorWidth = (float) ScreenUtil.getScreenWidth(getContext()) / (widthNum + (hasHalfTab ? 0.5f : 0));
            mp = new MarginLayoutParams((int) mIndicatorWidth,
                    LayoutParams.MATCH_PARENT);
        } else {
            mp = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        }

        if (tab != null) {
            tab.setFocusable(true);
            tab.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTabOnClickListener != null && pager.getCurrentItem() == position) {
                        mTabOnClickListener.OnTabOnClick(position);
                    }
                    pager.setCurrentItem(position);
                }
            });
            tab.setPadding(shouldExpand ? 0 : tabPadding, 0, shouldExpand ? 0 : tabPadding, 0);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(mp);
            lp.addRule(RelativeLayout.CENTER_IN_PARENT);
            if (tab.getParent() != null) {
                ((ViewGroup) tab.getParent()).removeView(tab);
            }
            rl.addView(tab, lp);
        }

        rl.setBackgroundColor(getResources().getColor(android.R.color.white));
        tabsContainer.addView(rl, position, shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
    }

    private void updateTabStyles() {

        for (int i = 0; i < tabCount; i++) {

            View v = tabsContainer.getChildAt(i);

            v.setBackgroundResource(tabBackgroundResId);

        }

        if (textViews.size() == 0) {
            return;
        }
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextSize(TypedValue.COMPLEX_UNIT_DIP, tabTextSize);
            textViews.get(i).setTypeface(tabTypeface, tabTypefaceStyle);
            textViews.get(i).setTextColor(tabTextColor);
            textViews.get(i).getPaint().setFakeBoldText(false);
            if (textAllCaps) {
                textViews.get(i).setAllCaps(true);
            }
        }
        textViews.get(currentPosition).setTextColor(selectTextColor);
        if (selectBoldStyle) {
            textViews.get(currentPosition).getPaint().setFakeBoldText(true);
        }
    }


    private void scrollToChild(int position, int offset) {

        if (tabCount == 0) {
            return;
        }

        if (position < 0 || position >= tabCount) {
            return;
        }

        View selectedChild = tabsContainer.getChildAt(position);
        if (selectedChild != null) {
            /*获取当前 item 的偏移量*/
            int targetScrollX = selectedChild.getLeft() + offset;
            /*item 的宽度*/
            int width = selectedChild.getWidth();
            /*item 距离正中间的偏移量*/
            mTitleOffset = (int) ((mWidth - width) / 2.0f);

            if (position > 0 || offset > 0) {
                /*计算出正在的偏移量*/
                targetScrollX -= mTitleOffset;
            }

            /*这个时候偏移的量就是屏幕的正中间*/
            scrollTo(targetScrollX, 0);

        }

        //selectTextColor
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || tabCount == 0) {
            return;
        }

        final int height = getHeight();

        // draw indicator line

        rectPaint.setColor(indicatorColor);

        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();

        View childView = ((ViewGroup) currentTab).getChildAt(0);
        float childWidth = childView.getMeasuredWidth();
        float offset = ((lineRight - lineLeft) - childWidth) / 2;


        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {

            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = (currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft);
            lineRight = (currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight);
        }

        canvas.drawRect(lineLeft + (shouldExpand ? offset : tabPadding), height - indicatorHeight - indicatorpadding, lineRight - (shouldExpand ? offset : tabPadding), height - indicatorpadding, rectPaint);

        // draw underline

        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);

        // draw divider

        dividerPaint.setColor(dividerColor);
        for (int i = 0; i < tabCount - 1; i++) {
            View tab = tabsContainer.getChildAt(i);
            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
        selectColorChange();
    }

    private void selectColorChange() {
        if (textViews.size() == 0 || selectTextColor == 0) {
            return;
        }
        for (int i = 0; i < textViews.size(); i++) {
            textViews.get(i).setTextColor(tabTextColor);
            textViews.get(i).getPaint().setFakeBoldText(false);
        }
        textViews.get(currentPosition).setTextColor(selectTextColor);
        if (selectBoldStyle) {
            textViews.get(currentPosition).getPaint().setFakeBoldText(true);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (tabsContainer == null || tabsContainer.getChildCount() == 0) {
                return;
            }
            currentPosition = position;
            currentPositionOffset = positionOffset;

            scrollToChild(position, (int) (positionOffset * tabsContainer.getChildAt(position).getWidth()));

            invalidate();

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                scrollToChild(pager.getCurrentItem(), 0);
            }

            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
        }

    }

    public void setSelectBoldStyle(boolean boldStyle) {
        this.selectBoldStyle = boldStyle;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorColorResource(int resId) {
        this.indicatorColor = getResources().getColor(resId);
        invalidate();
    }

    public int getIndicatorColor() {
        return this.indicatorColor;
    }

    public void setIndicatorHeight(int indicatorLineHeightPx) {
        this.indicatorHeight = indicatorLineHeightPx;
        invalidate();
    }

    public int getIndicatorHeight() {
        return indicatorHeight;
    }

    public void setUnderlineColor(int underlineColor) {
        this.underlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineColorResource(int resId) {
        this.underlineColor = getResources().getColor(resId);
        invalidate();
    }

    public int getUnderlineColor() {
        return underlineColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
        invalidate();
    }

    public void setDividerColorResource(int resId) {
        this.dividerColor = getResources().getColor(resId);
        invalidate();
    }

    public int getDividerColor() {
        return dividerColor;
    }

    public void setUnderlineHeight(int underlineHeightPx) {
        this.underlineHeight = underlineHeightPx;
        invalidate();
    }

    public int getUnderlineHeight() {
        return underlineHeight;
    }

    public void setDividerPadding(int dividerPaddingPx) {
        this.dividerPadding = dividerPaddingPx;
        invalidate();
    }

    public void setMarginLeftRight(int marginLeftRight) {
        this.marginLeftRight = marginLeftRight;
    }

    public int getDividerPadding() {
        return dividerPadding;
    }

    public void setScrollOffset(int scrollOffsetPx) {
        this.scrollOffset = scrollOffsetPx;
        invalidate();
    }

    public int getScrollOffset() {
        return scrollOffset;
    }

    public void setShouldExpand(boolean shouldExpand) {
        this.shouldExpand = shouldExpand;
        requestLayout();
    }

    public boolean getShouldExpand() {
        return shouldExpand;
    }

    public boolean isTextAllCaps() {
        return textAllCaps;
    }

    public void setAllCaps(boolean textAllCaps) {
        this.textAllCaps = textAllCaps;
    }

    public void setTextSize(int textSizeDip) {
        this.tabTextSize = textSizeDip;
        updateTabStyles();
    }

    public int getTextSize() {
        return tabTextSize;
    }

    public void setTextColor(int textColor) {
        this.tabTextColor = textColor;
        updateTabStyles();
    }

    public void setTextColorResource(int resId) {
        this.tabTextColor = getResources().getColor(resId);
        updateTabStyles();
    }

    public int getTextColor() {
        return tabTextColor;
    }

    public void setTypeface(Typeface typeface, int style) {
        this.tabTypeface = typeface;
        this.tabTypefaceStyle = style;
        updateTabStyles();
    }

    public void setTabBackground(int resId) {
        this.tabBackgroundResId = resId;
    }

    public int getTabBackground() {
        return tabBackgroundResId;
    }

    public void setTabPaddingLeftRight(int paddingPx) {
        this.tabPadding = paddingPx;
        tabPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPadding, dm);
        updateTabStyles();
    }

    public void setWidthNum(int num) {
        this.widthNum = num;
    }

    public void setHasHalfTab(boolean hasHalfTab) {
        this.hasHalfTab = hasHalfTab;
    }

    public int getTabPaddingLeftRight() {
        return tabPadding;
    }

    public int getIndicatorSelectTextColorResource() {
        return selectTextColor;
    }

    public void setIndicatorSelectTextColorResource(int resId) {
        this.selectTextColor = getResources().getColor(resId);
    }

    public void setIndicatorSelectTextColor(int selectTextColor) {
        this.selectTextColor = selectTextColor;
    }

    public void setIndicatorpadding(int indicatorpadding) {
        this.indicatorpadding = indicatorpadding;
        invalidate();
    }

    public int getIndicatorpadding() {
        return indicatorpadding;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    public int getSelectedTabIndex() {
        return currentPosition;
    }

    //	public void setMargins(int margins) {
    //		this.margins = margins;
    //	}

    /**
     * <p>Set the current page of both the ViewPager and indicator.</p>
     * <p>
     * <p>This <strong>must</strong> be used if you need to set the page before
     * the views are drawn on screen (e.g., default start page).</p>
     *
     * @param item
     */
    public void setCurrentItem(int item) {
        pager.setCurrentItem(item);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    static class SavedState extends BaseSavedState {
        int currentPosition;

        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

}
