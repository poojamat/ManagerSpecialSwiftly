package com.example.managerspecial

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.managerspecial.network.Status

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 * In my understanding the reason the exercise requires the pixel calculation is because the aspect
 * ratio is coming from server. In cases like this a recycler view with staggeredLayoutGridAdapter
 * should be used like shown here.
 */
class StaggeredGridFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_staggered_grid, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        view.findViewById<Button>(R.id.button_second).setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    private val listAdapter: ManagerSpecialStaggeredListAdapter = ManagerSpecialStaggeredListAdapter()
    private fun initViews() {
        val divider = DividerItemDecoration(activity, LinearLayoutManager.VERTICAL)
        val managerSpecialViewModel = ViewModelProvider(this).get(ManagerSpecialViewModel::class.java)
        val recyclerView = view?.findViewById<RecyclerView>(R.id.manager_special_recycler)
        recyclerView?.run {
            addItemDecoration(divider)
            layoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
            adapter = listAdapter
        }
        setupLiveData( managerSpecialViewModel)
    }
    private fun setupLiveData( managerSpecialViewModel: ManagerSpecialViewModel) {
            managerSpecialViewModel.managerSpecialList.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    it.data?.run {
                        listAdapter.managerSpecialList = it.data.managerSpecials
                    }
                }
                Status.ERROR -> {

                }
                Status.LOADING -> {

                }
                else -> {}
            }

        }
    }
}