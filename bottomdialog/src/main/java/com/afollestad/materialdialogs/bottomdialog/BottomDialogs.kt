package com.afollestad.materialdialogs.bottomdialog

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

/**
 * ViewGroup addView方法扩展
 */
fun ViewGroup.addView(@LayoutRes layoutResId: Int): View {
  val view = LayoutInflater.from(this.context).inflate(layoutResId, null, false)
  addView(view)
  return view
}

fun ViewGroup.addView(@LayoutRes layoutResId: Int, layoutParams: ViewGroup.LayoutParams): View {
  val view = LayoutInflater.from(this.context).inflate(layoutResId, null, false)
  addView(view, layoutParams)
  return view
}