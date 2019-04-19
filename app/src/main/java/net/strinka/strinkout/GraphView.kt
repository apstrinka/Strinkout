package net.strinka.strinkout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View

class GraphView(context: Context, attributeSet: AttributeSet) : View(context, attributeSet) {
    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.color = Color.MAGENTA
        val rect = Rect(0, 0, width, height)
        canvas!!.drawRect(rect, paint);
    }
}