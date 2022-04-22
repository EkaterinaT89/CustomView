package ru.netology.customview

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import androidx.core.content.withStyledAttributes
import ru.netology.customview.util.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    divStyleAttr: Int = 0,
    divStyleRes: Int = 0
) : View(context, attributeSet, divStyleAttr, divStyleRes) {

    private var center = PointF()
    private var radius = 0F
    private var oval = RectF()
    private var colors = emptyList<Int>()
    var progress = 0F
    var animator: Animator? = null

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            update()
        }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = AndroidUtils.dp(context, 5F).toFloat()
    }

    private val paintAnother = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
        strokeWidth = AndroidUtils.dp(context, 20F).toFloat()
        color = 0xFFDEDEDE.toInt()
        alpha = 127
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        strokeWidth = AndroidUtils.dp(context, 24F).toFloat()
        textAlign = Paint.Align.CENTER
    }


    init {
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            textPaint.textSize = getDimension(R.styleable.StatsView_fontSize, textPaint.textSize)
            paint.strokeWidth = getDimension(R.styleable.StatsView_lineWidth, paint.strokeWidth)
            colors = listOf(
                getColor(
                    R.styleable.StatsView_color1,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color2,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color3,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color4,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color5,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color6,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color7,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color8,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color9,
                    randomColor()
                ),
                getColor(
                    R.styleable.StatsView_color10,
                    randomColor()
                )
            )
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - paint.strokeWidth
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas?) {
        if (data.isEmpty()) {
            return
        }

        var startFrom = -90F
        val rotation = 360F * progress

        canvas?.drawArc(oval, startFrom, 360F, false, paintAnother)

        data.forEachIndexed { index, datum ->
            val angle = (datum / (data.maxOrNull()?.times(data.count())!!)) * 360F
            paint.color = colors.getOrElse(index) { randomColor() }
            canvas?.drawArc(
                oval, startFrom + rotation, angle * progress, false, paint
            )
            startFrom += angle
        }


        val text = (data.sum() / (data.maxOrNull()?.times(data.count())!!)) * 100
        canvas?.drawText(
            "%.2f%%".format(text),
            center.x,
            center.y + textPaint.textSize / 4F,
            textPaint
        )


        if (text == 100F) {
            paint.color = colors[0]
            canvas?.drawArc(oval, startFrom + rotation, 1F, false, paint)
        }
    }

    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())

    private fun update() {
        animator?.apply {
            cancel()
            removeAllListeners()
        }

        animator = ValueAnimator.ofFloat(0F, 1F).apply {
            addUpdateListener {
                interpolator = LinearInterpolator()
                duration = 5_000
                progress = it.animatedValue as Float
                invalidate()
            }
            start()
        }
    }
}