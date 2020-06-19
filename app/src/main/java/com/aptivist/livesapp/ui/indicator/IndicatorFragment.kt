package com.aptivist.livesapp.ui.indicator

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.aptivist.livesapp.R

class IndicatorFragment : Fragment() {

    private lateinit var indicatorViewModel: IndicatorViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        indicatorViewModel =
                ViewModelProviders.of(this).get(IndicatorViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_indicator, container, false)
        val textView: TextView = root.findViewById(R.id.text_indicator)
        indicatorViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
