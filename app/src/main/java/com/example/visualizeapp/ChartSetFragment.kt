package com.example.visualizeapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout

class ChartSetFragment : Fragment() {

    val TAG = "ChartSetFragment"

    lateinit var allComps : ArrayList<Pair<Int, String>>
    lateinit var currentType : ChartTypes

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chart_set, container, false)

        val labelText : TextView = view.findViewById(R.id.label_txt)
        val dropX : TextInputLayout = view.findViewById(R.id.dropdown_x)
        val dropY : TextInputLayout = view.findViewById(R.id.dropdown_y)

        allComps = ArrayList<Pair<Int, String>>(5)
        for (i in 0..4) {
            allComps.add(Pair(0, "Not-Set"))
        }


        val bundle = this.arguments

        val col = bundle?.getInt("color")
        val chartNum = bundle?.getInt("num")

        labelText.text = "Chart $chartNum"

//        col?.let { view.setBackgroundColor(it) }

//        val chartTypes = listOf("Line Chart", "Pie Chart", "Bar Chart")
        val chartTypes = ChartTypes.values().map {
            it.name
        }

        val autoComplete : AutoCompleteTextView = view.findViewById(R.id.auto_comp_chart)
        val autoCompX : AutoCompleteTextView = view.findViewById(R.id.auto_comp_x)
        val autoCompY : AutoCompleteTextView = view.findViewById(R.id.auto_comp_y)
        val saveButton : Button = view.findViewById(R.id.saveButton)


        val typeAdapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, chartTypes)
        var xAdapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, (activity as MainActivity).headerLabels)
        var yAdapter = ArrayAdapter<String>(requireContext(), R.layout.list_item, (activity as MainActivity).headerLabels)


        autoComplete.setAdapter(typeAdapter)
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            val selectedChart = parent.getItemAtPosition(position)
            Toast.makeText(requireContext(), "Selected:$position - $selectedChart",Toast.LENGTH_SHORT).show()

            dropX.visibility = View.VISIBLE
            dropY.visibility = View.VISIBLE
            autoCompX.setAdapter(xAdapter);
            autoCompY.setAdapter(yAdapter);

            when(position){
                0 -> {

                    dropX.setHint(R.string.select_x)
                    dropY.setHint(R.string.select_y)

                    autoCompX.setHint(R.string.select_x)
                    autoCompY.setHint(R.string.select_y)
                    currentType = ChartTypes.LINE

                }
                1 ->{
                    dropX.setHint(R.string.select_pie_labels)
                    dropY.setHint(R.string.select_pie_values)

                    autoCompX.setHint(R.string.select_pie_labels)
                    autoCompY.setHint(R.string.select_pie_values)
                    currentType = ChartTypes.PIE
                }
                2 ->{

                }
            }

            saveButton.visibility = View.VISIBLE
        }

        autoCompX.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            val selectedAttrib = parent.getItemAtPosition(pos)
            allComps[0] = Pair(pos, selectedAttrib.toString())
        }

        autoCompY.onItemClickListener = AdapterView.OnItemClickListener { parent, view, pos, id ->
            val selectedAttrib = parent.getItemAtPosition(pos)
            allComps[1] = Pair(pos, selectedAttrib.toString())
        }
        
        saveButton.setOnClickListener(View.OnClickListener {
            Log.d(TAG, "onCreateView: CHART SAVED with comps= \n $allComps")

            Toast.makeText(requireContext(), "CHART SAVED",Toast.LENGTH_SHORT).show()

            when(currentType){
                ChartTypes.LINE ->{
                    var x = allComps[0].first
                    var y = allComps[1].first

                    var lineAttrib = EntryPair(allComps[0].second, allComps[1].second)
                    var lineData  = ArrayList<EntryPair>()

                    (activity as MainActivity).csvRecords.forEach {
                        lineData.add(EntryPair(it[x], it[y]))
                    }

                    var lcSetup = LineChartSetup(lineData, lineAttrib, "This is a Line Chart")
                    (activity as MainActivity).chartList.add(lcSetup)
                }
                ChartTypes.PIE ->{
                    var labl = allComps[0].first
                    var v = allComps[1].first

                    var pieAttrib = EntryPair(allComps[0].second, allComps[1].second)
                    var pieData  = ArrayList<EntryPair>()

                    (activity as MainActivity).csvRecords.forEach {
                        pieData.add(EntryPair(it[labl], it[v]))
                    }

                    var pcSetup = PieChartSetup(pieData, pieAttrib, "This is a Pie Chart")
                    (activity as MainActivity).chartList.add(pcSetup)
                }
                ChartTypes.BAR ->{

                }
            }

        })
        return view
    }
}


/*
val pieEntries = ArrayList<PieEntry>()
var labelSet = mutableSetOf<String>()

var aggregates = ArrayList<Float>();

(activity as MainActivity).csvRecords.forEach {
    labelSet.add(it[l])
    Log.d(TAG, "Set :${labelSet.size} :${labelSet.toString()} ")
    var indx = labelSet.indexOf(it[l])

    if(aggregates.size > indx)
        aggregates[indx] = aggregates[indx] + it[v].replace(",", "").toFloat();
    else
        aggregates.add(it[v].replace(",", "").toFloat())
}

labelSet.withIndex().forEach{
    pieEntries.add(PieEntry(aggregates[it.index], it.value))
}

Log.d(TAG, "Set: ${labelSet.toString()} \nSum: ${aggregates.toString()}")

(activity as MainActivity).chartList.add(PieChartSetup(pieEntries, labelSet.size ,"This is a Pie Chart"))*/
