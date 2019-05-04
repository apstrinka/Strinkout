package net.strinka.strinkout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat

class GraphView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    var barValues: DoubleArray = DoubleArray(0)

    override fun onDraw(canvas: Canvas?) {
        val can = canvas!!
        val paint = Paint()
        paint.color = Color.BLACK
        can.drawRect(Rect(0, 0, width, height), paint);

        if (barValues.isNotEmpty()) {

            val maxVal = barValues.max()!! + 5

            paint.color = Color.WHITE
            paint.textSize = 50f
            for (i in 10 until maxVal.toInt() step 10){
                val y = (height - height*i/maxVal).toFloat()
                can.drawLine(0f, y, width.toFloat(), y, paint)
                can.drawText(i.toString(), 0f, y, paint)
            }

            paint.color = ContextCompat.getColor(context, R.color.colorBlue)
            val barWidth = width.toDouble() / barValues.size
            barValues.forEachIndexed { index, value ->
                val left = (index * barWidth + barWidth/8).toFloat()
                val right = (index * barWidth + 7*barWidth/8).toFloat()
                val bottom = height.toFloat()
                val top = (height - height*value/maxVal).toFloat()
                can.drawRect(left, top, right, bottom, paint)
            }
        }
    }
}