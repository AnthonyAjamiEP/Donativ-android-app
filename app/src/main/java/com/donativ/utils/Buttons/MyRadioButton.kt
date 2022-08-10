package com.donativ.utils.Buttons

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton

class MyRadioButton(context: Context,attributeSet: AttributeSet):AppCompatRadioButton(context,attributeSet) {

    init {
        applyFont()
    }

    private fun applyFont() {
        val regularTypeFace: Typeface =
                Typeface.createFromAsset(context.assets,"Montserrat-Bold.ttf")
        typeface = regularTypeFace
    }
}