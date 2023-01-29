package com.vaiki.cleanshoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.vaiki.cleanshoppinglist.R
import com.vaiki.cleanshoppinglist.domain.ShopItem

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var shopListAdapter: ShopListAdapter
    private var shopItemContainer: FragmentContainerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        shopItemContainer = findViewById(R.id.shop_item_container)



        setupRecyclerView()
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        viewModel.shopList.observe(this) {
            //внутри адаптера запускается новый поток, в котором происходят вычисления DiffCallback
            // submitList() - передать список с liveData в RV или обновить
            shopListAdapter.submitList(it)
        }
        val btnAdd = findViewById<FloatingActionButton>(R.id.btn_add_shop_item)
        if (isOnePaneMode()) {
            btnAdd.setOnClickListener {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            }
        } else {
            launchFragment(ShopItemFragment.newInstanceAddItem())
        }
    }

    //проверяем в какой ориентации приложение
    private fun isOnePaneMode(): Boolean {
        return shopItemContainer == null
    }

    //в режиме ландшафта вызываем fragment
    private fun launchFragment(fragment: Fragment) {

        //убирает прошлый фрагмент с backstack,
        // чтобы если нажать back мы не вернулись к предыдущему фрагменту
        supportFragmentManager.popBackStack()

        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupRecyclerView() {
        val rvShopList = findViewById<RecyclerView>(R.id.rv_shop_list)
        shopListAdapter = ShopListAdapter()
        with(rvShopList) {
            adapter = shopListAdapter
            // задаем пул вьюх, чтобы не плодить новые,
            // при недостатке данных в стандартном размере пула
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLE_VIEW_TYPE,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLE_VIEW_TYPE,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupLongClickListener()
        setupClickItemListener()
        setupSwipeListener(rvShopList)
    }

    private fun setupSwipeListener(rvShopList: RecyclerView) {
        val callback = object :
            ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //currentList - получить текущий лист из RV
                val item = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteItem(item)
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupClickItemListener() {

        shopListAdapter.onItemClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupLongClickListener() {
        shopListAdapter.onShopItemLongClickListener =
            {
                viewModel.changeEnableState(it)
            }
    }

}