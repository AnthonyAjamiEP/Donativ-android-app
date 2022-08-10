package com.donativ.utils.TextViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView

class RegularTextView(context:Context,attributeSet: AttributeSet) : AppCompatTextView(context, attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeFace: Typeface = Typeface.createFromAsset(context.assets,"Comfortaa-VariableFont_wght.ttf")
        typeface = regularTypeFace
    }
}