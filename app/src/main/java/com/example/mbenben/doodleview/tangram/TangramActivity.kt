package com.example.mbenben.doodleview.tangram

import android.graphics.Color
import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.alibaba.android.vlayout.DelegateAdapter
import com.alibaba.android.vlayout.VirtualLayoutManager
import com.alibaba.android.vlayout.layout.LinearLayoutHelper
import com.example.mbenben.doodleview.R
import com.example.mbenben.doodleview.tangram.MyAdapter.ItemClickListener
import kotlinx.android.synthetic.main.activity_tangram.*
import java.util.*
import kotlin.collections.HashMap
import com.alibaba.android.vlayout.layout.StickyLayoutHelper
import com.alibaba.android.vlayout.layout.ScrollFixLayoutHelper
import com.alibaba.android.vlayout.layout.FixLayoutHelper
import com.alibaba.android.vlayout.layout.GridLayoutHelper
import android.view.ViewGroup
import com.alibaba.android.vlayout.layout.FloatLayoutHelper
import com.alibaba.android.vlayout.layout.ColumnLayoutHelper
import com.alibaba.android.vlayout.layout.SingleLayoutHelper
import com.alibaba.android.vlayout.layout.OnePlusNLayoutHelper
import com.alibaba.android.vlayout.layout.StaggeredGridLayoutHelper


class TangramActivity : AppCompatActivity() ,ItemClickListener{
    override fun onItemClick(view: View, position: Int) {
        Toast.makeText(this@TangramActivity, listItem[position]["ItemTitle"] as String, Toast.LENGTH_SHORT).show()
    }

    val listItem :ArrayList<HashMap<String, Any> > = ArrayList()
    lateinit var linearLayoutAdapter:MyAdapter
    lateinit var gridLayoutAdapter:MyAdapter
    lateinit var fixLayoutAdapter:MyAdapter
    lateinit var scrollFixLayoutAdapter:MyAdapter
    lateinit var floatLayoutAdapter:MyAdapter
    lateinit var columnLayoutAdapter:MyAdapter
    lateinit var singleLayoutAdapter:MyAdapter
    lateinit var onePlusNLayoutAdapter:MyAdapter
    lateinit var stickyLayoutAdapter:MyAdapter
    lateinit var staggeredGridLayoutAdapter:MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tangram)

        /**
         * 1:创建RecyclerView & VirtualLayoutManager 对象并进行绑定
         */
        // 创建VirtualLayoutManager对象
        // 同时内部会创建一个LayoutHelperFinder对象，用来后续的LayoutHelper查找
        val layoutManager= VirtualLayoutManager(this)

        // 将VirtualLayoutManager绑定到recyclerView
        recyclerView.layoutManager=layoutManager

        /**
         * 2:设置回收复用池大小
         */
        val viewPool=RecyclerView.RecycledViewPool()
        recyclerView.recycledViewPool=viewPool
        viewPool.setMaxRecycledViews(0,10)

        /**
         * 3:设置需要存放的数据
         */
        for (i in 1..100){
            val map=HashMap<String,Any>()
            map.put("ItemTitle","第"+i+"行")
            map.put("ItemImage",R.mipmap.ic_launcher)
            listItem.add(map)
        }

        /**
         * 4:根据数据列表，创建对应的LayoutHelper
         */
        setLinearLayoutAdapter()
        setGridLayoutAdapter()
        setFixLayoutAdapter()
        setScrollFixLayoutAdapter()
        setFloatLayoutAdapter()
        setColumnLayoutAdapter()
        setSingleLayoutAdapter()
        setOnePlusNLayoutAdapter()
        setStickyLayoutAdapter()
        setStaggeredGridLayoutAdapter()

        /**
         * 5:将生成的LayoutHelper 交给Adapter，并绑定到RecyclerView 对象
         */
        // 1. 设置Adapter列表（同时也是设置LayoutHelper列表）
        val adapters:MutableList<DelegateAdapter.Adapter<*>> = LinkedList()

        //2. 将上述创建的Adapter对象放入到DelegateAdapter.Adapter列表里
        adapters.add(linearLayoutAdapter)
        adapters.add(gridLayoutAdapter)
        adapters.add(fixLayoutAdapter)
        adapters.add(scrollFixLayoutAdapter)
        adapters.add(floatLayoutAdapter)
        adapters.add(columnLayoutAdapter)
        adapters.add(singleLayoutAdapter)
        adapters.add(onePlusNLayoutAdapter)
        adapters.add(stickyLayoutAdapter)
        adapters.add(staggeredGridLayoutAdapter)

        // 3. 创建DelegateAdapter对象 & 将layoutManager绑定到DelegateAdapter
        val delegateAdapter=DelegateAdapter(layoutManager)

        // 4. 将DelegateAdapter.Adapter列表绑定到DelegateAdapter
        delegateAdapter.setAdapters(adapters)

        // 5. 将delegateAdapter绑定到recyclerView
        recyclerView.adapter=delegateAdapter

        /**
         * 6.Item之间的间隔
         */
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect!!.set(5, 5, 5, 5)
            }
        })
    }

    //设置线性布局
    private fun setLinearLayoutAdapter() {
        val linearLayoutHelper=LinearLayoutHelper()
        // 所有布局的公共属性（属性会在下面详细说明）
        linearLayoutHelper.itemCount=4// 设置布局里Item个数
        linearLayoutHelper.setPadding(10,10,10,10);// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        linearLayoutHelper.setMargin(10,10,10,10);// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        linearLayoutHelper.bgColor=Color.GRAY// 设置背景颜色
        linearLayoutHelper.aspectRatio= 6F// 设置设置布局内每行布局的宽与高的比

        // linearLayoutHelper特有属性,设置间隔高度,与RecyclerView的addItemDecoration（）添加的间隔叠加.
        linearLayoutHelper.setDividerHeight(10)
        //设置布局底部与下个布局的间隔
        linearLayoutHelper.marginBottom=100

        /**
         *  创建自定义的Adapter对象 & 绑定数据 & 绑定对应的LayoutHelper进行布局绘制
         */
        linearLayoutAdapter=object : MyAdapter(this,linearLayoutHelper,4,listItem){
            // 参数2:绑定绑定对应的LayoutHelper
            // 参数3:传入该布局需要显示的数据个数
            // 参数4:传入需要绑定的数据

            // 通过重写onBindViewHolder()设置更丰富的布局效果
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                // 为了展示效果,将布局的第一个数据设置为linearLayout
                if (position == 0) {
                    holder.text.text="Linear"
                }

                //为了展示效果,将布局里不同位置的Item进行背景颜色设置
                if (position < 2) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128);
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa22ff22.toInt());
                } else {
                    holder.itemView.setBackgroundColor(0xccff22ff.toInt());
                }
            }
        }

        linearLayoutAdapter.listener=object :ItemClickListener{
            override fun onItemClick(view: View, position: Int) {
                Toast.makeText(this@TangramActivity, listItem[position]["ItemTitle"] as String, Toast.LENGTH_SHORT).show()
            }
        }

        linearLayoutAdapter.setItemClickListener{
            _, position ->
            Toast.makeText(this@TangramActivity, listItem[position]["ItemTitle"] as String, Toast.LENGTH_SHORT).show()
        }
    }

    //设置Grid布局
    private fun setGridLayoutAdapter() {
        val gridLayoutHelper = GridLayoutHelper(3)
        // 在构造函数设置每行的网格个数

        // 公共属性
        gridLayoutHelper.itemCount = 36// 设置布局里Item个数
        gridLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        gridLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        // gridLayoutHelper.setBgColor(Color.GRAY);// 设置背景颜色
        gridLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比


        // gridLayoutHelper特有属性
        gridLayoutHelper.setWeights(floatArrayOf(40f, 30f, 30f))//设置每行中 每个网格宽度 占 每行总宽度 的比例
        gridLayoutHelper.vGap = 20// 控制子元素之间的垂直间距
        gridLayoutHelper.hGap = 20// 控制子元素之间的水平间距
        gridLayoutHelper.setAutoExpand(false)//是否自动填充空白区域
        gridLayoutHelper.spanCount = 3// 设置每行多少个网格
        // 通过自定义SpanSizeLookup来控制某个Item的占网格个数
        gridLayoutHelper.setSpanSizeLookup(object : GridLayoutHelper.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position > 7) {
                    return 3
                    // 第7个位置后,每个Item占3个网格
                } else {
                    return 2
                    // 第7个位置前,每个Item占2个网格
                }
            }
        })

        gridLayoutAdapter = object : MyAdapter(this, gridLayoutHelper, 36, listItem) {
            // 设置需要展示的数据总数,此处设置是8,即展示总数是8个,然后每行是4个(上面设置的)
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为gridLayoutHelper
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                // 为了展示效果,将布局里不同位置的Item进行背景颜色设置
                if (position < 2) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128)
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa22ff22.toInt())
                } else {
                    holder.itemView.setBackgroundColor(0xccff22ff.toInt())
                }

                if (position == 0) {
                    holder.text.text="Grid"
                }
            }
        }
        gridLayoutAdapter.listener=this
    }

    //设置固定布局
    private fun setFixLayoutAdapter() {
        val fixLayoutHelper = FixLayoutHelper(FixLayoutHelper.TOP_LEFT, 40, 100)
        // 参数说明:
        // 参数1:设置吸边时的基准位置(alignType) - 有四个取值:TOP_LEFT(默认), TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
        // 参数2:基准位置的偏移量x
        // 参数3:基准位置的偏移量y


        // 公共属性
        fixLayoutHelper.itemCount = 1// 设置布局里Item个数
        // 从设置Item数目的源码可以看出，一个FixLayoutHelper只能设置一个
//        @Override
//        public void setItemCount(int itemCount) {
//            if (itemCount > 0) {
//                super.setItemCount(1);
//            } else {
//                super.setItemCount(0);
//            }
//        }
        fixLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        fixLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        fixLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        fixLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比

        // fixLayoutHelper特有属性
        fixLayoutHelper.setAlignType(FixLayoutHelper.TOP_LEFT)// 设置吸边时的基准位置(alignType)
        fixLayoutHelper.setX(30)// 设置基准位置的横向偏移量X
        fixLayoutHelper.setY(50)// 设置基准位置的纵向偏移量Y

        fixLayoutAdapter = object : MyAdapter(this, fixLayoutHelper, 1, listItem) {
            // 设置需要展示的数据总数,此处设置是1
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为Fix
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="Fix"
                }
            }
        }
        fixLayoutAdapter.listener=this

    }

    //设置可选固定布局
    private fun setScrollFixLayoutAdapter() {
        val scrollFixLayoutHelper = ScrollFixLayoutHelper(ScrollFixLayoutHelper.TOP_RIGHT, 0, 0)
        // 参数说明:
        // 参数1:设置吸边时的基准位置(alignType) - 有四个取值:TOP_LEFT(默认), TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT
        // 参数2:基准位置的偏移量x
        // 参数3:基准位置的偏移量y


        // 公共属性
        scrollFixLayoutHelper.itemCount = 1// 设置布局里Item个数
        // 从设置Item数目的源码可以看出，一个FixLayoutHelper只能设置一个
//        @Override
//        public void setItemCount(int itemCount) {
//            if (itemCount > 0) {
//                super.setItemCount(1);
//            } else {
//                super.setItemCount(0);
//            }
//        }
        scrollFixLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        scrollFixLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        scrollFixLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        scrollFixLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比

        // fixLayoutHelper特有属性
        scrollFixLayoutHelper.setAlignType(FixLayoutHelper.TOP_LEFT)// 设置吸边时的基准位置(alignType)
        scrollFixLayoutHelper.setX(30)// 设置基准位置的横向偏移量X
        scrollFixLayoutHelper.setY(50)// 设置基准位置的纵向偏移量Y
        scrollFixLayoutHelper.showType = ScrollFixLayoutHelper.SHOW_ON_LEAVE// 设置Item的显示模式


        scrollFixLayoutAdapter = object : MyAdapter(this, scrollFixLayoutHelper, 1, listItem) {
            // 设置需要展示的数据总数,此处设置是1
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为scrollFix
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="scrollFix"
                }
            }
        }
        scrollFixLayoutAdapter.listener=this
    }

    // 设置浮动布局
    private fun setFloatLayoutAdapter() {
        val floatLayoutHelper = FloatLayoutHelper()
        // 创建FloatLayoutHelper对象

        // 公共属性
        floatLayoutHelper.itemCount = 1// 设置布局里Item个数
        // 从设置Item数目的源码可以看出，一个FixLayoutHelper只能设置一个
//        @Override
//        public void setItemCount(int itemCount) {
//            if (itemCount > 0) {
//                super.setItemCount(1);
//            } else {
//                super.setItemCount(0);
//            }
//        }
        floatLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        floatLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        floatLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        floatLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比

        // floatLayoutHelper特有属性
        floatLayoutHelper.setDefaultLocation(300, 300)// 设置布局里Item的初始位置

        floatLayoutAdapter = object : MyAdapter(this, floatLayoutHelper, 1, listItem) {
            // 设置需要展示的数据总数,此处设置是1
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为float
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val layoutParams = ViewGroup.LayoutParams(500, 1000)
                holder.itemView.layoutParams=layoutParams
                holder.itemView.setBackgroundColor(Color.RED)

                if (position == 0) {
                    holder.text.text="float"
                }
            }
        }
        floatLayoutAdapter.listener=this
    }

    // 设置栏格布局
    private fun setColumnLayoutAdapter() {
        val columnLayoutHelper = ColumnLayoutHelper()
        // 创建对象

        // 公共属性
        columnLayoutHelper.itemCount = 3// 设置布局里Item个数
        columnLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        columnLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        columnLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        columnLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比


        // columnLayoutHelper特有属性
        columnLayoutHelper.setWeights(floatArrayOf(30f, 40f, 30f))// 设置该行每个Item占该行总宽度的比例

        columnLayoutAdapter = object : MyAdapter(this, columnLayoutHelper, 3, listItem) {
            // 设置需要展示的数据总数,此处设置是3
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为Column
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="Column"
                }
            }
        }
        columnLayoutAdapter.listener=this
    }

    //设置通栏布局
    private fun setSingleLayoutAdapter() {
        val singleLayoutHelper = SingleLayoutHelper()

        // 公共属性
        singleLayoutHelper.itemCount = 3// 设置布局里Item个数
        singleLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        singleLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        singleLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        singleLayoutHelper.aspectRatio = 6f// 设置设置布局内每行布局的宽与高的比


        singleLayoutAdapter = object : MyAdapter(this, singleLayoutHelper, 1, listItem) {
            // 设置需要展示的数据总数,此处设置是1
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为Single
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="Single"
                }
            }
        }
        singleLayoutAdapter.listener=this
    }

    //设置1拖N布局
    private fun setOnePlusNLayoutAdapter() {
        val onePlusNLayoutHelper = OnePlusNLayoutHelper(5)
        // 在构造函数里传入显示的Item数
        // 最多是1拖4,即5个

        // 公共属性
        onePlusNLayoutHelper.itemCount = 3// 设置布局里Item个数
        onePlusNLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        onePlusNLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        onePlusNLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        onePlusNLayoutHelper.aspectRatio = 3f// 设置设置布局内每行布局的宽与高的比


        onePlusNLayoutAdapter = object : MyAdapter(this, onePlusNLayoutHelper, 5, listItem) {
            // 设置需要展示的数据总数,此处设置是5,即1拖4
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为onePlus
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="onePlus"
                }
            }
        }
        onePlusNLayoutAdapter.listener=this
    }

    //设置吸边布局
    private fun setStickyLayoutAdapter() {
        val stickyLayoutHelper = StickyLayoutHelper()

        // 公共属性
        stickyLayoutHelper.itemCount = 3// 设置布局里Item个数
        stickyLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        stickyLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        stickyLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        stickyLayoutHelper.aspectRatio = 3f// 设置设置布局内每行布局的宽与高的比

        // 特有属性
        stickyLayoutHelper.setStickyStart(true)
        // true = 组件吸在顶部
        // false = 组件吸在底部

        stickyLayoutHelper.setOffset(100)// 设置吸边位置的偏移量

        stickyLayoutAdapter = object : MyAdapter(this, stickyLayoutHelper, 1, listItem) {
            // 设置需要展示的数据总数,此处设置是1
            // 为了展示效果,通过重写onBindViewHolder()将布局的第一个数据设置为Stick
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                if (position == 0) {
                    holder.text.text="Stick"
                }
            }
        }
        stickyLayoutAdapter.listener=this
    }

    //设置瀑布流布局
    private fun setStaggeredGridLayoutAdapter() {
        val staggeredGridLayoutHelper = StaggeredGridLayoutHelper()
        // 创建对象

        // 公有属性
        staggeredGridLayoutHelper.itemCount = 20// 设置布局里Item个数
        staggeredGridLayoutHelper.setPadding(20, 20, 20, 20)// 设置LayoutHelper的子元素相对LayoutHelper边缘的距离
        staggeredGridLayoutHelper.setMargin(20, 20, 20, 20)// 设置LayoutHelper边缘相对父控件（即RecyclerView）的距离
        staggeredGridLayoutHelper.bgColor = Color.GRAY// 设置背景颜色
        staggeredGridLayoutHelper.aspectRatio = 3f// 设置设置布局内每行布局的宽与高的比

        // 特有属性
        staggeredGridLayoutHelper.lane = 3// 设置控制瀑布流每行的Item数
        staggeredGridLayoutHelper.hGap = 20// 设置子元素之间的水平间距
        staggeredGridLayoutHelper.vGap = 15// 设置子元素之间的垂直间距

        staggeredGridLayoutAdapter = object : MyAdapter(this, staggeredGridLayoutHelper, 20, listItem) {
            // 设置需要展示的数据总数,此处设置是20

            // 通过重写onBindViewHolder()设置更加丰富的布局
            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                super.onBindViewHolder(holder, position)
                val layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 150 + position % 5 * 20)
                holder.itemView.setLayoutParams(layoutParams)

                // 为了展示效果,设置不同位置的背景色
                if (position > 10) {
                    holder.itemView.setBackgroundColor(0x66cc0000 + (position - 6) * 128)
                } else if (position % 2 == 0) {
                    holder.itemView.setBackgroundColor(0xaa22ff22.toInt())
                } else {
                    holder.itemView.setBackgroundColor(0xccff22ff.toInt())
                }

                // 为了展示效果,通过将布局的第一个数据设置为staggeredGrid
                if (position == 0) {
                    holder.text.text="staggeredGrid"
                }
            }
        }
        staggeredGridLayoutAdapter.listener=this
    }
}
