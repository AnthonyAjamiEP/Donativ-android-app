package com.donativ.utils.TextViews

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText

class MyEditText(context: Context,attributeSet: AttributeSet):AppCompatEditText(context,attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeFace: Typeface =
            Typeface.createFromAsset(context.assets,"Montserrat-Regular.ttf")
        typeface = regularTypeFace
    }
}