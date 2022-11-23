package com.example.wintopia.view.list

import android.annotation.SuppressLint
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.widget.Button
import android.widget.ListAdapter
import androidx.coordinatorlayout.widget.CoordinatorLayout.Behavior.getTag
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.RecyclerView
import java.lang.Float.max
import java.lang.Float.min

class SwipeController: ItemTouchHelper.Callback() {
    private var swipeBack = false
    private val buttonWidth = 200f //버튼 너비 지정
    private var buttonsShowedState = Button.GONE
    private var buttonInstance: RectF? = null //버튼 객체 초기 지정
    private lateinit var listener: ItemTouchHelperListener
    private var currentItemViewHolder: RecyclerView.ViewHolder? = null

    private var currentPosition: Int? = null
    private var previousPosition: Int? = null
    private var currentDx = 0f
    private var clamp = 0f

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val draw_flags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipe_flags = ItemTouchHelper.LEFT
        return makeMovementFlags(draw_flags, swipe_flags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        currentDx = 0f
        getDefaultUIUtil().clearView(getView(viewHolder))
        previousPosition = viewHolder.adapterPosition
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
    viewHolder?.let{
        currentPosition = viewHolder.adapterPosition
        getDefaultUIUtil().onSelected(getView(it))
        }
    }

    override fun getSwipeEscapeVelocity(defaultValue: Float): Float {
        return defaultValue * 20
    }

    override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
        val isClamped = getTag(viewHolder)
        setTag(viewHolder, !isClamped && currentDx <= -clamp)
        return 4f
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ACTION_STATE_SWIPE) {
            val view = getView(viewHolder)
            val isClamped = getTag(viewHolder)
            val x =  clampViewPositionHorizontal(view, dX, isClamped, isCurrentlyActive)

            currentDx = x

            if (buttonsShowedState !== Button.GONE) {
                currentDx = x.coerceAtLeast(-buttonWidth)
            }
            getDefaultUIUtil().onDraw(
                c,
                recyclerView,
                view,
                x,
                dY,
                actionState,
                isCurrentlyActive
            )
            currentItemViewHolder = viewHolder
            drawButtons(c, currentItemViewHolder!!)
        }
    }
    private fun clampViewPositionHorizontal(
        view: View,
        dX: Float,
        isClamped: Boolean,
        isCurrentlyActive: Boolean
    ) : Float {
        // View의 가로 길이의 절반까지만 swipe 되도록
        val min: Float = -view.width.toFloat()/2
        // RIGHT 방향으로 swipe 막기
        val max: Float = 0f

        val x = if (isClamped) {
            // View가 고정되었을 때 swipe되는 영역 제한
            if (isCurrentlyActive) dX - clamp else -clamp
        } else {
            dX
        }

        return min(max(min, x), max)
    }

        private fun setTag(viewHolder: RecyclerView.ViewHolder, isClamped: Boolean) {
            // isClamped를 view의 tag로 관리
            viewHolder.itemView.tag = isClamped
        }

        private fun getTag(viewHolder: RecyclerView.ViewHolder) : Boolean {
            // isClamped를 view의 tag로 관리
            return viewHolder.itemView.tag as? Boolean ?: false
        }

        private fun getView(viewHolder: RecyclerView.ViewHolder) : View {
            return (viewHolder as ListVOAdapter.ListVOViewHolder).itemView
        }

        fun setClamp(clamp: Float) {
            this.clamp = clamp
        }

        fun removePreviousClamp(recyclerView: RecyclerView) {
            if (currentPosition == previousPosition)
                return
            previousPosition?.let {
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(it) ?: return
                getView(viewHolder).translationX = 0f
                setTag(viewHolder, false)
                previousPosition = null
            }
        }
    private fun drawButtons(
        c: Canvas,
        viewHolder: RecyclerView.ViewHolder
    ) { //레이아웃이 아닌 클래스에서 직접 버튼 구현
        val buttonWidthWithOutPadding = buttonWidth - 10
        val corners = 5f
        val itemView: View = viewHolder.itemView
        val p = Paint() //Paint 객체 p 생성
        buttonInstance = null

        //rectF 클래스로 버튼 형태 구현
        if (buttonsShowedState === Button.VISIBLE) {
            val rightButton = RectF(
                itemView.right - buttonWidthWithOutPadding, (itemView.top + 10).toFloat(),
                (itemView.right - 10).toFloat(), (itemView.bottom - 10).toFloat()
            )
            p.color = Color.parseColor("원하는 버튼 색상의 hex code 입력")
            c.drawRoundRect(rightButton, corners, corners, p)
            drawText("버튼에 표시될 텍스트", c, rightButton, p)
            buttonInstance = rightButton
        }
    }

    private fun drawText(text: String, c: Canvas, button: RectF, p: Paint) { //버튼 내에 글씨 삽입
        val textSize = 45f
        p.color = Color.WHITE
        p.isAntiAlias = true
        p.textSize = textSize
        val textWidth = p.measureText(text) //measureText : 글자의 너비 리턴
        c.drawText(
            text,
            button.centerX() - textWidth / 2,
            button.centerY() + textSize / 2,
            p
        ) //Canvas 객체의 drawText : 글자의 구체적인 속성 정의
    }

}
