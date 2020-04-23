package com.asp.wattmelon.demo;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * @作者JTL.
 * @日期2017/12/1.
 * @说明：加载更多接口
 */

public abstract class OnLoadMoreListener extends RecyclerView.OnScrollListener {
    private int countItem;
    private int lastItem;
    private boolean isScrolled = false;//是否可以滑动
    private boolean isAllScreen = false;//是否充满全屏
    private RecyclerView.LayoutManager layoutManager;

    /**
     * 加载接口
     *
     * @param countItem 总数量
     * @param lastItem  最后显示的position
     */
    protected abstract void onLoading(int countItem, int lastItem);

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {//滚动状态变化时回调
        //拖拽或者惯性滑动时isScolled设置为true
        if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
            isScrolled = true;
            isAllScreen =true;
        } else {
            isScrolled = false;
        }

    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {//滚动时回调
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            layoutManager = recyclerView.getLayoutManager();
            countItem = layoutManager.getItemCount();
            lastItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
        }
        if (isScrolled && countItem != lastItem && lastItem == countItem - 1) {
            onLoading(countItem, lastItem);
        }
    }

    public boolean isAllScreen(){
        return isAllScreen;
    }
}
