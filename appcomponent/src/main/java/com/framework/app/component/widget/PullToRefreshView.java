package com.framework.app.component.widget;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.framework.app.component.R;
import com.framework.app.component.utils.DateUtil;

/**
 * @ClassName: PullToRefreshView
 * @Description: 上拉刷新下拉加载控件
 * @author lim
 * @mail lgmshare@gmail.com
 * @date 2014-6-3 上午11:04:29
 */
public class PullToRefreshView extends LinearLayout {

	private static final String TAG = "PullToRefreshView";

	private Context mContext;
	// pull state
	private static final int PULL_UP_STATE = 0;
	private static final int PULL_DOWN_STATE = 1;
	// refresh states
	private static final int STATE_NORMAL = 2;
	private static final int STATE_READY = 3;
	private static final int STATE_REFRESHING = 4;

	private boolean mEnablePullRefresh = true;
	private boolean mEnablePullLoad = true;
	// last y
	private int mLastMotionY;
	// lock
	private boolean mLock;

	// list or grid
	private AdapterView<?> mAdapterView;
	private ScrollView mScrollView;

	// header view
	private View mHeaderView;
	private int mHeaderViewHeight;
	private ImageView mHeaderImageView;
	private TextView mHeaderTextView;
	private TextView mHeaderUpdateTextView;
	private ProgressBar mHeaderProgressBar;
	// header view current state
	private int mHeaderState = STATE_NORMAL;
	// footer view
	private View mFooterView;
	private int mFooterViewHeight;
	private ImageView mFooterImageView;
	private TextView mFooterTextView;
	private ProgressBar mFooterProgressBar;
	// footer view current state
	private int mFooterState = STATE_NORMAL;

	/** PULL_UP_STATE or PULL_DOWN_STATE */
	private int mPullState;
	/** 变为向下的箭头,改变箭头方向，旋转 */
	private RotateAnimation mFlipAnimation;
	private RotateAnimation mReverseFlipAnimation;
	private OnPullToRefreshListener onPullToRefreshListener;

	public PullToRefreshView(Context context) {
		super(context);
		mContext = context;
		init();
	}

	public PullToRefreshView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		init();
	}

	private void init() {
		// Load all of the animations we need in code rather than through XML
		mFlipAnimation = new RotateAnimation(0, -180, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mFlipAnimation.setInterpolator(new LinearInterpolator());
		mFlipAnimation.setDuration(250);
		mFlipAnimation.setFillAfter(true);
		mReverseFlipAnimation = new RotateAnimation(-180, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
		mReverseFlipAnimation.setDuration(250);
		mReverseFlipAnimation.setFillAfter(true);
		// header view 在此添加,保证是第一个添加到linearlayout的最上端
		addHeaderView();
	}

	private void addHeaderView() {
		// header view
		mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_header, this, false);

		mHeaderImageView = (ImageView) mHeaderView.findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_text);
		mHeaderUpdateTextView = (TextView) mHeaderView.findViewById(R.id.pull_to_refresh_updated_at);
		mHeaderProgressBar = (ProgressBar) mHeaderView.findViewById(R.id.pull_to_refresh_progress);
		// header layout
		measureView(mHeaderView);
		mHeaderViewHeight = mHeaderView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mHeaderViewHeight);
		// 设置topMargin的值为负的header View高度,即将其隐藏在最上方
		params.topMargin = -(mHeaderViewHeight);
		// mHeaderView.setLayoutParams(params1);
		addView(mHeaderView, params);
	}

	private void addFooterView() {
		// footer view
		mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.pull_to_refresh_footer, this, false);
		mFooterImageView = (ImageView) mFooterView.findViewById(R.id.pull_to_load_image);
		mFooterTextView = (TextView) mFooterView.findViewById(R.id.pull_to_load_text);
		mFooterProgressBar = (ProgressBar) mFooterView.findViewById(R.id.pull_to_load_progress);
		// footer layout
		measureView(mFooterView);
		mFooterViewHeight = mFooterView.getMeasuredHeight();
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, mFooterViewHeight);
		// int top = getHeight();
		// params.topMargin
		// =getHeight();//在这里getHeight()==0,但在onInterceptTouchEvent()方法里getHeight()已经有值了,不再是0;
		// 由于是线性布局可以直接添加,只要AdapterView的高度是MATCH_PARENT,那么footer view就会被添加到最后,并隐藏
		addView(mFooterView, params);
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		// footer view 在此添加保证添加到linearlayout中的最后
		addFooterView();
		initContentAdapterView();
	}

	/**
	 * init AdapterView like ListView,GridView and so on;or init ScrollView
	 * 
	 */
	private void initContentAdapterView() {
		int count = getChildCount();
		if (count < 3) {
			throw new IllegalArgumentException("this layout must contain 3 child views,and AdapterView or ScrollView must in the second position!");
		}
		View view = null;
		for (int i = 0; i < count - 1; ++i) {
			view = getChildAt(i);
			if (view instanceof AdapterView<?>) {
				mAdapterView = (AdapterView<?>) view;
			}
			if (view instanceof ScrollView) {
				// finish later
				mScrollView = (ScrollView) view;
			}
		}
		if (mAdapterView == null && mScrollView == null) {
			throw new IllegalArgumentException("must contain a AdapterView or ScrollView in this layout!");
		}
	}

	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		SharedPreferences preferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		String lastTime = preferences.getString(mContext.getClass().getName() + ".lasttime", DateUtil.getStringDateLong());
		setLastUpdated(DateUtil.friendlyTime(lastTime));
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent e) {
		int y = (int) e.getRawY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 首先拦截down事件,记录y坐标
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			// deltaY > 0 是向下运动,< 0是向上运动
			int deltaY = y - mLastMotionY;
			// 如果滚动到顶部或底部，则拦截touch事件
			if (isRefreshViewScroll(deltaY)) {
				return true;
			}
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			break;
		}
		return false;
	}

	/**
	 * 如果在onInterceptTouchEvent()方法中没有拦截(即onInterceptTouchEvent()方法中 return
	 * false)则由PullToRefreshView 的子View来处理;否则由下面的方法来处理(即由PullToRefreshView自己来处理)
	 */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (mLock) {
			return true;
		}
		int y = (int) event.getRawY();
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// onInterceptTouchEvent已经记录
			// mLastMotionY = y;
			break;
		case MotionEvent.ACTION_MOVE:
			int deltaY = y - mLastMotionY;
			if (mPullState == PULL_DOWN_STATE) {
				// PullToRefreshView执行下拉
				// Log.i(TAG, " pull down!parent view move!");
				int newTopMargin = changingHeaderViewTopMargin(deltaY);
				if (newTopMargin > 0) {
					setHeaderRefreshState(STATE_READY);
				} else {
					setHeaderRefreshState(STATE_NORMAL);
				}
			} else if (mPullState == PULL_UP_STATE) {
				// PullToRefreshView执行上拉
				// Log.i(TAG, "pull up!parent view move!");
				int newTopMargin = changingHeaderViewTopMargin(deltaY);
				if (Math.abs(newTopMargin) >= (mHeaderViewHeight + mFooterViewHeight)) {
					setFooterLoadState(STATE_READY);
				} else {
					setFooterLoadState(STATE_NORMAL);
				}
			}
			mLastMotionY = y;
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			int topMargin = getHeaderTopMargin();
			if (mPullState == PULL_DOWN_STATE) {
				if (topMargin > 0 && mHeaderState == STATE_READY) {
					// 开始刷新
					setHeaderRefreshState(STATE_REFRESHING);
					if (onPullToRefreshListener != null) {
						onPullToRefreshListener.onRefresh();
					}
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			} else if (mPullState == PULL_UP_STATE) {
				if (Math.abs(topMargin) > mHeaderViewHeight + mFooterViewHeight && mFooterState == STATE_READY) {
					// 开始执行footer 刷新
					setFooterLoadState(STATE_REFRESHING);
					if (onPullToRefreshListener != null) {
						onPullToRefreshListener.onLoadMore();
					}
				} else {
					// 还没有执行刷新，重新隐藏
					setHeaderTopMargin(-mHeaderViewHeight);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	/**
	 * 是否应该到了父View,即PullToRefreshView滑动
	 * 
	 * @param deltaY
	 *            ---deltaY > 0 是向下运动,< 0是向上运动
	 * @return
	 */
	private boolean isRefreshViewScroll(int deltaY) {
		if (mHeaderState == STATE_REFRESHING || mFooterState == STATE_REFRESHING) {
			return false;
		}
		// 对于ListView和GridView
		if (mAdapterView != null) {
			// 子view(ListView or GridView)滑动到最顶端
			if (deltaY > 0) {
				// 判断是否禁用下拉刷新操作
				if (!mEnablePullRefresh) {
					return false;
				}
				View child = mAdapterView.getChildAt(0);
				if (child == null) {
					// 如果mAdapterView中没有数据,不拦截
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				if (mAdapterView.getFirstVisiblePosition() == 0 && child.getTop() == 0) {
					mPullState = PULL_DOWN_STATE;
					return true;
				}
				int top = child.getTop();
				int padding = mAdapterView.getPaddingTop();
				if (mAdapterView.getFirstVisiblePosition() == 0 && Math.abs(top - padding) <= 11) {// 这里之前用3可以判断,但现在不行,还没找到原因
					mPullState = PULL_DOWN_STATE;
					return true;
				}

			} else if (deltaY < 0) {
				// 判断是否禁用上拉加载更多操作
				if (!mEnablePullLoad) {
					return false;
				}
				View lastChild = mAdapterView.getChildAt(mAdapterView.getChildCount() - 1);
				if (lastChild == null) {
					// 如果mAdapterView中没有数据,不拦截
					return false;
				}
				// 最后一个子view的Bottom小于父View的高度说明mAdapterView的数据没有填满父view,
				// 等于父View的高度说明mAdapterView已经滑动到最后
				if (lastChild.getBottom() <= getHeight() && mAdapterView.getLastVisiblePosition() == mAdapterView.getCount() - 1) {
					mPullState = PULL_UP_STATE;
					return true;
				}
			}
		}
		// 对于ScrollView
		if (mScrollView != null) {
			// 子scroll view滑动到最顶端
			View child = mScrollView.getChildAt(0);
			if (deltaY > 0 && mScrollView.getScrollY() == 0) {
				// 判断是否禁用下拉刷新操作
				if (!mEnablePullRefresh) {
					return false;
				}
				mPullState = PULL_DOWN_STATE;
				return true;
			} else if (deltaY < 0 && child.getMeasuredHeight() <= getHeight() + mScrollView.getScrollY()) {
				// 判断是否禁用上拉加载更多操作
				if (!mEnablePullLoad) {
					return false;
				}
				mPullState = PULL_UP_STATE;
				return true;
			}
		}
		return false;
	}

	/**
	 * header 准备刷新,手指移动过程,还没有释放
	 * 
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void setHeaderRefreshState(int state) {
		if (state == mHeaderState)
			return;

		if (state == STATE_REFRESHING) {
			mHeaderImageView.setVisibility(View.GONE);
			mHeaderImageView.clearAnimation();
			mHeaderProgressBar.setVisibility(View.VISIBLE);
		} else {
			mHeaderImageView.setVisibility(View.VISIBLE);
			mHeaderProgressBar.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mHeaderState == STATE_READY) {
				mHeaderImageView.startAnimation(mReverseFlipAnimation);
			}
			if (mHeaderState == STATE_REFRESHING) {
				mHeaderImageView.clearAnimation();
			}

			mHeaderTextView.setText(R.string.pull_to_refresh_header_normal);
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			break;
		case STATE_READY:
			mHeaderImageView.clearAnimation();
			mHeaderImageView.startAnimation(mFlipAnimation);
			mHeaderTextView.setText(R.string.pull_to_refresh_header_ready);
			break;
		case STATE_REFRESHING:
			setHeaderTopMargin(0);
			mHeaderTextView.setText(R.string.pull_to_refresh_header_refreshing);
			break;

		default:
			break;
		}
		mHeaderState = state;
	}

	/**
	 * footer 准备刷新,手指移动过程,还没有释放 移动footer view高度同样和移动header view
	 * 高度是一样，都是通过修改header view的topmargin的值来达到
	 * 
	 * @param deltaY
	 *            ,手指滑动的距离
	 */
	private void setFooterLoadState(int state) {
		if (state == mFooterState)
			return;

		if (state == STATE_REFRESHING) {
			mFooterImageView.clearAnimation();
			mFooterImageView.setVisibility(View.GONE);
			mFooterProgressBar.setVisibility(View.VISIBLE);
		} else {
			mFooterImageView.setVisibility(View.VISIBLE);
			mFooterProgressBar.setVisibility(View.GONE);
		}

		switch (state) {
		case STATE_NORMAL:
			if (mFooterState == STATE_READY) {
				mFooterImageView.startAnimation(mReverseFlipAnimation);
			}
			if (mFooterState == STATE_REFRESHING) {
				mFooterImageView.clearAnimation();
			}
			mFooterTextView.setText(R.string.pull_to_refresh_footer_normal);
			break;
		case STATE_READY:
			mFooterImageView.clearAnimation();
			mFooterImageView.startAnimation(mFlipAnimation);
			mFooterTextView.setText(R.string.pull_to_refresh_footer_ready);
			break;
		case STATE_REFRESHING:
			int top = mHeaderViewHeight + mFooterViewHeight;
			setHeaderTopMargin(-top);
			mFooterTextView.setText(R.string.pull_to_refresh_footer_loading);
			break;

		default:
			break;
		}
		mFooterState = state;
	}

	/**
	 * 修改Header view top margin的值
	 * 
	 * @description
	 * @param deltaY
	 * @return params.topMargin
	 */
	private int changingHeaderViewTopMargin(int deltaY) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		float newTopMargin = params.topMargin + deltaY * 0.3f;
		// 这里对上拉做一下限制,因为当前上拉后然后不释放手指直接下拉,会把下拉刷新给触发了,感谢网友yufengzungzhe的指出
		// 表示如果是在上拉后一段距离,然后直接下拉
		if (deltaY > 0 && mPullState == PULL_UP_STATE && Math.abs(params.topMargin) <= mHeaderViewHeight) {
			return params.topMargin;
		}
		// 同样地,对下拉做一下限制,避免出现跟上拉操作时一样的bug
		if (deltaY < 0 && mPullState == PULL_DOWN_STATE && Math.abs(params.topMargin) >= mHeaderViewHeight) {
			return params.topMargin;
		}
		params.topMargin = (int) newTopMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
		return params.topMargin;
	}

	/**
	 * 设置header view 的topMargin的值
	 * 
	 * @description
	 * @param topMargin为0时
	 *            ，说明header view 刚好完全显示出来； 为-mHeaderViewHeight时，说明完全隐藏了
	 */
	private void setHeaderTopMargin(int topMargin) {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		params.topMargin = topMargin;
		mHeaderView.setLayoutParams(params);
		invalidate();
	}

	/**
	 * header view 完成更新后恢复初始状态
	 * 
	 */
	public void onHeaderRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		setHeaderRefreshState(STATE_NORMAL);
		SharedPreferences preferences = mContext.getSharedPreferences(TAG, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		editor.putString(mContext.getClass().getName() + ".lasttime", DateUtil.getStringDateLong());
		editor.commit();
		setLastUpdated("1分钟前");
	}

	/**
	 * Resets the list to a normal state after a refresh.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void onHeaderRefreshComplete(CharSequence lastUpdated) {
		setLastUpdated(lastUpdated);
		onHeaderRefreshComplete();
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete() {
		setHeaderTopMargin(-mHeaderViewHeight);
		setFooterLoadState(STATE_NORMAL);
	}

	/**
	 * footer view 完成更新后恢复初始状态
	 */
	public void onFooterRefreshComplete(int size) {
		if (size > 0) {
			mFooterView.setVisibility(View.VISIBLE);
		} else {
			mFooterView.setVisibility(View.GONE);
		}
		setHeaderTopMargin(-mFooterViewHeight);
		setFooterLoadState(STATE_NORMAL);
	}

	/**
	 * Set a text to represent when the list was last updated.
	 * 
	 * @param lastUpdated
	 *            Last updated at.
	 */
	public void setLastUpdated(CharSequence lastUpdated) {
		if (lastUpdated != null) {
			mHeaderUpdateTextView.setVisibility(View.VISIBLE);
			mHeaderUpdateTextView.setText(lastUpdated);
		} else {
			mHeaderUpdateTextView.setVisibility(View.GONE);
		}
	}

	/**
	 * 获取当前header view 的topMargin
	 * 
	 * @description
	 * @return params.topMargin
	 */
	private int getHeaderTopMargin() {
		LayoutParams params = (LayoutParams) mHeaderView.getLayoutParams();
		return params.topMargin;
	}

	/**
	 * lock
	 * 
	 */
	public void lock() {
		mLock = true;
	}

	/**
	 * unlock
	 * 
	 */
	public void unlock() {
		mLock = false;
	}

	public boolean isPullRefreshEnable() {
		return mEnablePullRefresh;
	}

	public void setPullRefreshEnable(boolean enable) {
		this.mEnablePullRefresh = enable;
	}

	public boolean isPullLoadEnable() {
		return mEnablePullLoad;
	}

	public void setPullLoadEnable(boolean enable) {
		this.mEnablePullLoad = enable;
	}

	public void setOnPullToRefreshListener(OnPullToRefreshListener listener) {
		onPullToRefreshListener = listener;
	}

	public interface OnPullToRefreshListener {

		public void onRefresh();

		public void onLoadMore();
	}
}
