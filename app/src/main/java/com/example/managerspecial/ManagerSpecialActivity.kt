package com.example.managerspecial

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.managerspecial.network.Status
import org.koin.core.KoinComponent
// Representation with just a plain simple recycler view without aspect ratio applied
class ManagerSpecialActivity : AppCompatActivity(), KoinComponent {
    private val listAdapter: ManagerSpecialAdapter = ManagerSpecialAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {
        val divider = DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        val managerSpecialViewModel = ViewModelProvider(this).get(ManagerSpecialViewModel::class.java)
        val recyclerView = findViewById<RecyclerView>(R.id.manager_special_recycler)
        recyclerView.addItemDecoration(divider)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = listAdapter
        setupLiveData( managerSpecialViewModel)
    }
    private fun setupLiveData( managerSpecialViewModel: ManagerSpecialViewModel) {
        managerSpecialViewModel.managerSpecialList.observe(this) {
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

    companion object {
        @JvmStatic
        fun startActivity(context: Context) {
            val intent = Intent(context, ManagerSpecialActivity::class.java)
            context.startActivity(intent)
        }
    }
}
