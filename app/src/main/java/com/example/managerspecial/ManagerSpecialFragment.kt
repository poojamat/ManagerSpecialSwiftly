package com.example.managerspecial

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.managerspecial.network.ManagerSpecial
import com.example.managerspecial.network.ManagerSpecials
import com.example.managerspecial.network.Status
import com.squareup.picasso.Picasso
import org.koin.core.KoinComponent


/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 * This fragment Displays the code based on display pixel calculation as specified in the requirements
 */
class ManagerSpecialFragment : Fragment(), KoinComponent {
    private val viewModel: ManagerSpecialViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_manager_special, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLiveData(view)
        view.findViewById<Button>(R.id.button_first).setOnClickListener {
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
    }

    private fun populateLayout(specials: ManagerSpecials, view: View) {
        val specialsList = view.findViewById<LinearLayout>(R.id.manager_special_list)
        specialsList.removeAllViews()
        var special: ManagerSpecial

        context?.run{
            var horizontalLayout = getHorizontalLayout()
            var widthUsed = 0
            var heightUsed = 0

            while (specials.managerSpecials.listIterator().hasNext()) {
                special = specials.managerSpecials.listIterator().next()
                if(wouldFitHorizontally(specials.canvasUnit, special.width, widthUsed)){
                    horizontalLayout.addView(ControlSpecialItem(this, special))
                    widthUsed += special.width
                }else if(wouldFitVertically(specials.canvasUnit, special.height, heightUsed)){
                    horizontalLayout = getHorizontalLayout()
                    horizontalLayout.addView(ControlSpecialItem(this, special))
                    specialsList.addView(
                        horizontalLayout)
                    widthUsed = special.width
                }else{
                    break
                }
                heightUsed += special.height
            }
        }
    }

    private fun wouldFitHorizontally(canvasUnit: Int, tileWidth: Int, dimenUsed: Int ): Boolean{
        val totalWidthPixels = getDisplayMetrics().widthPixels
        return viewModel.wouldFitInDimension(canvasUnit, totalWidthPixels, tileWidth, dimenUsed)
    }
    private fun wouldFitVertically(canvasUnit: Int, tileWidth: Int, dimenUsed: Int ): Boolean{
        val totalHeightPixels = getDisplayMetrics().heightPixels
        return viewModel.wouldFitInDimension(canvasUnit, totalHeightPixels, tileWidth, dimenUsed)
    }


    private fun getHorizontalLayout(): LinearLayout {
        val horizontalLayout = LinearLayout(context)
        horizontalLayout.layoutParams =
            LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        horizontalLayout.orientation = LinearLayout.HORIZONTAL
        return horizontalLayout
    }

    private fun getDisplayMetrics(): DisplayMetrics{
        val outMetrics = DisplayMetrics()
            val display = activity?.display
            display?.getRealMetrics(outMetrics)
        return outMetrics
    }
    private fun setupLiveData(view: View) {
        viewModel.managerSpecialList.observe(
            viewLifecycleOwner,
            { viewState: ManagerSpecialViewState ->
                when (viewState.status) {
                    Status.SUCCESS -> viewState.data?.run { populateLayout(this, view) }
                    Status.ERROR, Status.UNSET, Status.IRRECOVERABLE_ERROR -> {
                    }
                    Status.LOADING -> onLoading()

                }
            })
    }

    private fun onLoading() {
    }

}
@SuppressLint("ViewConstructor")

class ControlSpecialItem(context: Context, private val item: ManagerSpecial): ConstraintLayout(
    context
) {
    init {
        val view = inflate(context, R.layout.item_manager_special, this)
        populateData(view)
    }

    private fun populateData(view: View) {
        view.findViewById<TextView>(R.id.original_price).text = """${"$"}${item.original_price}"""
        view.findViewById<TextView>(R.id.discount_price).text = """${"$"}${item.price}"""
        view.findViewById<TextView>(R.id.description).text = item.display_name
        val ratio =String.format("h,%d:%d", item.width, item.height)
        val set = ConstraintSet()
        val imageView = view.findViewById<ImageView>(R.id.image)
        val body = view.findViewById<ConstraintLayout>(R.id.body)
        set.clone(body)
        set.setDimensionRatio(imageView.id, ratio)
        set.applyTo(body)
        Picasso.with(context)
            .load(item.imageUrl)
            .into(imageView)
    }
}

