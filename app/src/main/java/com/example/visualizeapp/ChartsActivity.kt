package com.example.visualizeapp

import android.graphics.Color
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import java.io.BufferedReader
import java.io.InputStreamReader

class ChartsActivity : AppCompatActivity(){

    val TAG = "ChartsActivity"

    private lateinit var lineChartView : LineChart
    private lateinit var pieChartView : PieChart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_charts)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
/*

        val chartSetupList = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableArrayListExtra(resources.getString(R.string.chart_list_key), ChartSetup::class.java)
        } else {
            TODO("VERSION.SDK_INT < TIRAMISU")
        }

        chartSetupList!!.withIndex().forEach {

                var type : Int = -1
                when(it.value){
                    is LineChartSetup -> type = 0
                    is PieChartSetup -> type = 1
                }
                Log.d(TAG, "Chart_${it.index}: ${it.value}")
            }
*/


        lineChartView = findViewById(R.id.lineChart)
        pieChartView = findViewById(R.id.pieChart)

        lineChartView.visibility = View.VISIBLE
        pieChartView.visibility = View.VISIBLE

        configLineChart()
        lineChartView.data = prepLineChartData()
        lineChartView.invalidate()

        configPieChart()
        pieChartView.data = prepPieChartData()
        pieChartView.invalidate()



    }


    private fun configLineChart() {
        // Set chart properties, such as title, axis labels, etc.
        lineChartView.description.isEnabled = false
        lineChartView.setTouchEnabled(true)
        lineChartView.setPinchZoom(true)
        lineChartView.setMaxVisibleValueCount(25)
//        lchartView.setDrawGridBackground(false)
        lineChartView.setBackgroundColor(Color.WHITE)

        val xAxis = lineChartView.xAxis

        xAxis.position = XAxis.XAxisPosition.BOTTOM
//        xAxis.setDrawGridLines(false)
        xAxis.granularity = 1f
        //...
    }

    private fun prepLineChartData(): LineData {
        val entries1 = ArrayList<Entry>()
//        val entries2 = ArrayList<Entry>()

        for (i in 0..11) {
            val v = (Math.random() * 100).toFloat()
            entries1.add(Entry(i.toFloat(), v))

//            val v2 = (Math.random() * 100).toFloat()
//            entries2.add(Entry(i.toFloat(), v2))

        }

        val dataSet1 = LineDataSet(entries1, "Revenue")
        dataSet1.setColor(Color.GREEN)
        dataSet1.setCircleColor(Color.GREEN)

//        val dataSet2 = LineDataSet(entries2, "Revenue2")
//        dataSet2.setColor(Color.RED)
//        dataSet2.setCircleColor(Color.RED)

        return LineData(dataSet1)
    }

    private fun configPieChart() {
        pieChartView.description.isEnabled = false
        pieChartView.holeRadius = 30f
        pieChartView.transparentCircleRadius = 40f
        pieChartView.description.isEnabled = false
        pieChartView.setExtraOffsets(5F, 5F, 5F, 5F)
        pieChartView.setEntryLabelTextSize(11f)
        pieChartView.setEntryLabelColor(Color.BLACK)
        pieChartView.setBackgroundColor(Color.WHITE)
        pieChartView.setCenterTextColor(Color.RED)
        pieChartView.setNoDataTextColor(Color.MAGENTA)
        pieChartView.setEntryLabelTypeface(Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD))
//        pieChartView.setDrawEntryLabels(false)
//        pieChartView.setDrawHoleEnabled(false)

        pieChartView.animateY(1000, Easing.EaseInOutQuad)
        pieChartView.spin(2000, 0F, 360F, Easing.EaseInOutQuad)

        val l = pieChartView.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.xEntrySpace = 7f
        l.yEntrySpace = 0f
        l.yOffset = 0f
//        pieChartView.onChartGestureListener = On
    }


    private fun prepPieChartData(): PieData {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(30f, "North"))
        entries.add(PieEntry(50f, "South"))
        entries.add(PieEntry(5f, "East"))
        entries.add(PieEntry(15f, "West"))


        val dataSet = PieDataSet(entries, "Pie Chart")

        var pieColors = ArrayList<Int>()
        for (c in ColorTemplate.JOYFUL_COLORS)
            pieColors.add(c)

        for (c in ColorTemplate.VORDIPLOM_COLORS)
            pieColors.add(c)

//        Log.d(TAG, "prepPieChartData: ${pieColors.size}")
//        dataSet.colors.add(Color.RED)
//        dataSet.colors.add(Color.GREEN)
//        dataSet.colors.add(Color.BLUE)
        dataSet.colors = pieColors

        return PieData(dataSet)
    }

}

/*    private fun OnChartGestureListener.onChartGestureEnd() {
        val highlight = .getHighlightByTouchPoint(me!!.x, me.y)

        if(highlight != null){
            val sliceIndex = highlight.x.toInt()
            val sliceValue = pieChartView.data.getDataSetByIndex(highlight.dataSetIndex).getEntryForIndex(sliceIndex).y

            Log.d(TAG, "onChartGestureEnd: SLICE-VALUE: $sliceValue")
        }
    }*/
