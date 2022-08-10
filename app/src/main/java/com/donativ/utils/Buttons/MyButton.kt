package com.donativ.utils.Buttons

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

class MyButton(context: Context,attributeSet: AttributeSet):AppCompatButton(context,attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val boldTypeFace: Typeface =
            Typeface.createFromAsset(context.assets,"Comfortaa-Bold.ttf")
        typeface = boldTypeFace
    }
}