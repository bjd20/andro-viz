package com.example.visualizeapp

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.Parcelable
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieEntry
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVParser
import org.apache.commons.csv.CSVRecord
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    private var isFrags = false
    private var nCharts = 0
    private var activateFrag = 0
    private var totFrags = 2

    lateinit var fileTextView : TextView

    lateinit var csvRecords : MutableList<CSVRecord>
    lateinit var headerLabels : MutableList<String>

    lateinit var chartList : ArrayList<ChartSetup>


    private val requestCSV = registerForActivityResult(
        ActivityResultContracts.GetContent()){
        it?.path?.let {p->
            var pathsplit = p.split("/").toTypedArray()
            var filename = pathsplit[pathsplit.size - 1]
            Log.d(TAG, "CSV File Path: $p")
            headerLabels = mutableListOf<String>();
            csvRecords = mutableListOf<CSVRecord>();
            Log.d(TAG, "CSV contents:\n ${readCSV(it)}")

            fileTextView.text = "$filename"
//            fileTextView.setBackgroundColor(resources.getColor(R.color.lime, null))
            val bgDrawable = fileTextView.background
            if(bgDrawable is GradientDrawable){
                bgDrawable.setStroke(3,Color.BLUE)
                fileTextView.background = bgDrawable
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val vizBut : Button = findViewById(R.id.vizButton)
        val browseBut : Button = findViewById(R.id.browse)
        fileTextView = findViewById(R.id.file_textView)

        val addButton : ImageButton = findViewById(R.id.add_imgButton)
        val arrowLeft : ImageButton = findViewById(R.id.arrowLeft)
        val arrowRight : ImageButton = findViewById(R.id.arrowRight)

        val frameLayout: FrameLayout = findViewById(R.id.frameLayout)

        chartList = arrayListOf<ChartSetup>()

//        val pieEntries = ArrayList<PieEntry>()

//        chartList.add(LineChartSetup(lineEntries, "descc1"))
//        chartList.add(PieChartSetup(pieEntries, 7,"descc2"))


        vizBut.setOnClickListener {

/*            chartList.withIndex().forEach {

                var type : Int = -1
                when(it.value){
                    is LineChartSetup -> type = 0
                    is PieChartSetup -> type = 1
                }
                Log.d(TAG, "Chart_${it.index}: ${it.value}")
            }*/

            val intent = Intent(this, ChartsActivity::class.java)
            intent.putParcelableArrayListExtra(resources.getString(R.string.chart_list_key),chartList)
            startActivity(intent)
        }

        browseBut.setOnClickListener {
            isFrags=true
            requestCSV.launch("*/*")
        }
        val myColors = arrayOf(
            Color.GREEN,
            Color.BLUE,
            Color.YELLOW,
            Color.CYAN,
            resources.getColor(R.color.lime, null)
        )

        var col = 0
        var fragmentList = mutableListOf<Fragment>()

        addButton.setOnClickListener {
            var chartSetFrag : Fragment = ChartSetFragment()
            frameLayout.visibility = View.VISIBLE

            nCharts++
            var bundle = Bundle().apply {
                putInt("num", nCharts)
                putInt("color", myColors[col])
            }
            col = (col+1) % myColors.size

            chartSetFrag.arguments = bundle
            fragmentList.add(chartSetFrag)
            replaceFragment(chartSetFrag)
            activateFrag = nCharts
            Log.d(TAG, "Add Chart: ${fragmentList.size}")
        }

        arrowLeft.setOnClickListener {
            if(activateFrag != 0){
                activateFrag -=1
            }
//            it.setBackgroundColor(Color.YELLOW)
//            lineFrag1?.let {
//                replaceFragment(lineFrag1)
//            }
        }

        arrowRight.setOnClickListener {
            activateFrag +=1
            activateFrag %= totFrags

//            pieFrag1?.let {
//                replaceFragment(pieFrag1)
//            }
        }

    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragContainer, fragment)
        fragmentTransaction.commit()
    }

    fun getPathFromUri(uri: Uri?): String {
        val filename1: Array<String>
        val fn: String
        val filepath = uri?.path ?: return ""
        val filePath1 = filepath.split(":").toTypedArray()
        filename1 = filepath.split("/").toTypedArray()
        fn = filename1[filename1.size - 1]
        return "${Environment.getExternalStorageDirectory().path}/${filePath1[1]}"
    }

    private fun readCSV(uri: Uri?) : String{
        var inpStream = InputStreamReader(contentResolver.openInputStream(uri!!))
        val buffReader = BufferedReader(inpStream)

        var dataString = ""

        val lines = mutableListOf<String>()

        /*         var line: String? = null
                while (run {
                        line = buffReader.readLine()
                        line
                    } != null) {
                    lines.add(line!!)
                }*/

        val csvParser = CSVParser.parse(buffReader, CSVFormat.DEFAULT)

        csvParser.withIndex().forEach(){
            it?.let {
                if(it.index==0){
                    it.value.forEach(){
                        headerLabels.add(it)
                    }
                }else{
                    csvRecords.add(it.value)
                    var tempStr = it.index.toString() + "-> "
                    it.value.forEach(){
                        tempStr += it + " - "
                    }
//                Log.d(TAG, "$tempStr \n")
                    dataString += "$tempStr\n"
                }
            }
        }
//        Log.d(TAG, "readCSV: ${buffReader.readLine()}")

        return dataString
    }

}