package com.afollestad.materialdialogs.bottomdialog

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.*
import android.widget.FrameLayout
import com.afollestad.materialdialogs.DialogBehavior
import com.afollestad.materialdialogs.LayoutMode
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.internal.button.DialogActionButtonLayout
import com.afollestad.materialdialogs.internal.main.DialogLayout

/**
 * author wangjianxiong (@wang642357)
 * date 2020/12/14
 *
 * 底部弹出的MaterialDialog
 * @param height container高度
 */
class BottomDialog(private val height: Int = ViewGroup.LayoutParams.WRAP_CONTENT) : DialogBehavior {

  private lateinit var dialogLayout: DialogLayout
  private lateinit var buttonsLayout: DialogActionButtonLayout
  private lateinit var root: FrameLayout
  private lateinit var container: FrameLayout
  private var dialog: MaterialDialog? = null

  override fun createView(
      creatingContext: Context,
      dialogWindow: Window,
      layoutInflater: LayoutInflater,
      dialog: MaterialDialog
  ): ViewGroup {
    this.dialog = dialog
    //init root
    root = FrameLayout(creatingContext)

    //init DialogContent's container And add DialogContent
    val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, height)
    layoutParams.gravity = Gravity.BOTTOM
    container = getDialogContentContainer(creatingContext)
    dialogLayout = container.addView(R.layout.md_dialog_base_no_buttons) as DialogLayout
    root.addView(container, layoutParams)

    //init DialogActionButtonLayout And add it into root view
    buttonsLayout = root.addView(R.layout.md_dialog_stub_buttons, FrameLayout.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT,
        Gravity.BOTTOM)) as DialogActionButtonLayout
    return root
  }

  private fun getDialogContentContainer(context: Context): FrameLayout {
    val container = FrameLayout(context)

    container.setOnClickListener(null)
    return container
  }

  override fun getDialogLayout(root: ViewGroup): DialogLayout {
    return dialogLayout.also {
      it.layoutMode = LayoutMode.WRAP_CONTENT
      it.attachButtonsLayout(buttonsLayout)
    }
  }

  override fun getThemeRes(isDark: Boolean): Int {
    return if (isDark) {
      R.style.MD_Dark_BottomDialog
    } else {
      R.style.MD_Light_BottomDialog
    }
  }

  override fun onDismiss(): Boolean {
    return false
  }

  override fun onPostShow(dialog: MaterialDialog) = Unit

  override fun onPreShow(dialog: MaterialDialog) {
    if (dialog.cancelOnTouchOutside && dialog.cancelable) {
      // Clicking outside the dialogLayout dismisses the dialog
      root.setOnClickListener { this.dialog?.dismiss() }
    } else {
      root.setOnClickListener(null)
    }
  }

  override fun setBackgroundColor(view: DialogLayout, color: Int, cornerRadius: Float) {
    container.background = GradientDrawable().apply {
      this.cornerRadii = floatArrayOf(
          cornerRadius, cornerRadius, // top left
          cornerRadius, cornerRadius, // top right
          0f, 0f, // bottom left
          0f, 0f // bottom right
      )
      setColor(color)
    }
    buttonsLayout.setBackgroundColor(color)
  }

  override fun setWindowConstraints(
      context: Context,
      window: Window,
      view: DialogLayout,
      maxWidth: Int?
  ) {
    if (maxWidth == 0) {
      // Postpone
      return
    }
    window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
    val lp = WindowManager.LayoutParams()
        .apply {
          copyFrom(window.attributes)
          gravity = Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL
          width = WindowManager.LayoutParams.MATCH_PARENT
          height = WindowManager.LayoutParams.MATCH_PARENT
        }
    window.attributes = lp
  }
}