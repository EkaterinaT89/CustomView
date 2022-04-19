package ru.netology.customview

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.customview.util.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : View(context, attrs, defStyleAttr, defStyleRes) {
    private var radius = 0F
    private var center = PointF(0F, 0F)
    private var oval = RectF(0F, 0F, 0F, 0F)

    private var lineWidth = AndroidUtils.dp(context, 5F).toFloat()
    private var fontSize = AndroidUtils.dp(context, 40F).toFloat()
    private var colors = emptyList<Int>()

    init {
        context.withStyledAttributes(attrs, R.styleable.StatsView) {
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth)
            fontSize = getDimension(R.styleable.StatsView_fontSize, fontSize)
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

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = lineWidth
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        textAlign = Paint.Align.CENTER
        textSize = fontSize
    }

    var data: List<Float> = emptyList()
        set(value) {
            field = value
            invalidate()
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        radius = min(w, h) / 2F - lineWidth / 2
        center = PointF(w / 2F, h / 2F)
        oval = RectF(
            center.x - radius, center.y - radius,
            center.x + radius, center.y + radius,
        )
    }

    override fun onDraw(canvas: Canvas) {
        if (data.isEmpty()) {
            return
        }

        var startFrom = -90F
        for ((index, datum) in data.withIndex()) {
            val angle = 360F * datum / data.sum()
            paint.color = colors.getOrNull(index) ?: randomColor()
            canvas.drawArc(oval, startFrom, angle, false, paint)
            startFrom += angle
        }

        paint.color = colors.first()
        canvas.drawCircle(center.x, center.y - radius, 1F, paint)

        canvas.drawText(
            "%.2f%%".format(100F),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint,
        )
    }

    private fun randomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}