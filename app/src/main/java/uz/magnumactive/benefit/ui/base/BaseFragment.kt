package uz.magnumactive.benefit.ui.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import dagger.hilt.android.AndroidEntryPoint
import uz.magnumactive.benefit.ui.viewgroups.ItemProductListEmpty
import uz.magnumactive.benefit.ui.viewgroups.ItemProgress
import uz.magnumactive.benefit.util.RequestState

@AndroidEntryPoint
abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {


//    open fun attach() {
//
//    }
//
//    open fun setup() {
//
//    }
//
//    open fun observe() {
//
//    }

    fun <T> setupRVObservers(
        observable: LiveData<RequestState<List<T>>>,
        recyclerView: RecyclerView,
        adapter: GroupAdapter<GroupieViewHolder>,
        layoutManager: RecyclerView.LayoutManager = GridLayoutManager(context, 2),
        createItem: (obj: T) -> Item,
    ) {
        recyclerView.adapter = adapter
        observable.observe(viewLifecycleOwner) { requestState ->
            val resp = requestState ?: return@observe
            when (resp) {
                is RequestState.Error -> {
                    adapter.clear()
                }
                RequestState.Loading -> {
                    adapter.clear()
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    adapter.add(ItemProgress())
                }
                is RequestState.Success -> {
                    adapter.clear()
                    val result = resp.value
                    if (result.isNotEmpty()) {
                        recyclerView.layoutManager = layoutManager
                        result.forEach {
                            adapter.add(createItem(it))
                        }
                    } else {
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        adapter.add(ItemProductListEmpty())
                    }
                }
            }
            adapter.notifyDataSetChanged()
        }

    }

}