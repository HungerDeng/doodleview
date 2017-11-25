package com.example.skinlibrary.base

import android.content.Context
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.skinlibrary.attr.base.DynamicAttr
import com.example.skinlibrary.listener.IDynamicNewView
import com.example.skinlibrary.loader.SkinInflaterFactory
import kotlin.properties.Delegates

/**
 * Created by dkmFromCG on 2017/9/11.
 * function:
 */
class SkinBaseFragment:Fragment(),IDynamicNewView {

    private var mIDynamicNewView:IDynamicNewView?=null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        try {
            mIDynamicNewView=context as IDynamicNewView
        }catch (e:ClassCastException){
            mIDynamicNewView=null
        }
    }

    override fun onDestroyView() {
        removeAllView(view!!)
        super.onDestroyView()
    }

    override fun dynamicAddView(view: View, pDAttrs: MutableList<DynamicAttr>) {
        mIDynamicNewView?.let {
            mIDynamicNewView!!.dynamicAddView(view, pDAttrs)
        }?:(throw RuntimeException("IDynamicNewView should be implements !"))
    }

    override fun dynamicAddView(view: View, attrName: String, attrValueResId: Int) {
        mIDynamicNewView?.dynamicAddView(view, attrName, attrValueResId)
    }

    override fun dynamicAddFontView(textView: TextView) {
        mIDynamicNewView?.dynamicAddFontView(textView)
    }

    private fun getSkinInflaterFactory(): SkinInflaterFactory? {
        if (activity is SkinBaseActivity){
            return (activity as SkinBaseActivity).getSkinInflaterFactory()
        }
        return null
    }

    private fun removeViewInSkinInflaterFactory(view: View){
        getSkinInflaterFactory()?.let {
            //此方法用于Activity中Fragment销毁的时候，移除Fragment中的View
            getSkinInflaterFactory()!!.removeSkinView(view)
        }
    }

    fun  removeAllView(view: View) {
        if (view is ViewGroup){
            val viewGroup=view
            for (i in 0..viewGroup.childCount){
                removeAllView(viewGroup.getChildAt(i))
            }
            removeViewInSkinInflaterFactory(view)
        }else{
            removeViewInSkinInflaterFactory(view)
        }
    }

}