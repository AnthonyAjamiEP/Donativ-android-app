package com.donativ.utils.TextViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class BoldTextView(context:Context,attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets,"Comfortaa-Bold.ttf")
        typeface = boldTypeFace
    }
}