package com.example.sampleapp

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.DecimalFormat

data class RateTier(val minOrder: Int, val maxOrder: Int, val rates: IntArray)

object SpxRateTables {
    val DELIVERY_TIERS = listOf(
        RateTier(1, 15, intArrayOf(25, 60, 60, 70, 70, 90, 110, 120)),
        RateTier(15, 30, intArrayOf(50, 110, 120, 120, 150, 180, 220, 240)),
        RateTier(30, 60, intArrayOf(75, 170, 170, 180, 220, 270, 330, 350)),
        RateTier(60, 100, intArrayOf(100, 220, 230, 240, 290, 360, 440, 470)),
        RateTier(100, 150, intArrayOf(125, 280, 290, 300, 360, 450, 550, 590)),
        RateTier(150, 200, intArrayOf(175, 390, 400, 420, 510, 630, 770, 820)),
        RateTier(200, 300, intArrayOf(275, 610, 630, 660, 800, 990, 1210, 1290)),
        RateTier(300, 400, intArrayOf(350, 770, 810, 840, 1020, 1260, 1540, 1650)),
        RateTier(400, 500, intArrayOf(525, 1160, 1210, 1260, 1530, 1890, 2310, 2470)),
        RateTier(500, 600, intArrayOf(675, 1490, 1560, 1620, 1960, 2430, 2970, 3170)),
        RateTier(600, 700, intArrayOf(800, 1760, 1840, 1920, 2320, 2880, 3520, 3760)),
        RateTier(700, 800, intArrayOf(950, 2090, 2190, 2280, 2760, 3420, 4180, 4470)),
        RateTier(800, 900, intArrayOf(1125, 2125, 2375, 2475, 3150, 4050, 4950, 5300)),
        RateTier(900, 1000, intArrayOf(1400, 2525, 2960, 3075, 3925, 5050, 6150, 6575)),
        RateTier(1000, 1100, intArrayOf(1800, 3250, 3775, 3950, 5050, 6475, 7925, 8450)),
        RateTier(1100, 1200, intArrayOf(2075, 3725, 4350, 4575, 5800, 7475, 9125, 9750)),
        RateTier(1200, 1300, intArrayOf(2275, 4100, 4775, 5000, 6375, 8200, 10000, 10700)),
        RateTier(1300, 1400, intArrayOf(2475, 4450, 5200, 5450, 6925, 8900, 10900, 11625)),
        RateTier(1400, 1500, intArrayOf(2625, 4725, 5525, 5775, 7350, 9450, 11550, 12350)),
        RateTier(1500, 1600, intArrayOf(2800, 5050, 5875, 6150, 7850, 10075, 12325, 13150)),
        RateTier(1600, 1700, intArrayOf(2900, 5225, 6100, 6375, 8125, 10450, 12750, 13625)),
        RateTier(1700, 1800, intArrayOf(3175, 5725, 6675, 6975, 8900, 11425, 13975, 14925)),
        RateTier(1800, 1900, intArrayOf(3450, 6200, 7250, 7600, 9650, 12425, 15175, 16225)),
        RateTier(1900, 2000, intArrayOf(3750, 6750, 7875, 8250, 10500, 13500, 16500, 17625)),
        RateTier(2000, 2100, intArrayOf(4050, 7300, 8500, 8900, 11350, 14575, 17825, 19025)),
        RateTier(2100, 2200, intArrayOf(4275, 7700, 8975, 9400, 11975, 15400, 18800, 20100)),
        RateTier(2200, 2300, intArrayOf(4425, 7975, 9300, 9725, 12400, 15925, 19475, 20800)),
        RateTier(2300, 2400, intArrayOf(4650, 8375, 9775, 10225, 13025, 16750, 20450, 21850)),
        RateTier(2400, 2500, intArrayOf(4825, 8675, 10125, 10625, 13550, 17375, 21225, 22675)),
        RateTier(2500, 2600, intArrayOf(5075, 9125, 10650, 11175, 14200, 18275, 22325, 23850)),
        RateTier(2600, 2700, intArrayOf(5275, 9500, 11075, 11600, 14775, 19000, 23200, 24800)),
        RateTier(2700, 2800, intArrayOf(5625, 10125, 11825, 12375, 15750, 20250, 24750, 26450)),
        RateTier(2800, 2900, intArrayOf(6000, 10800, 12600, 13200, 16800, 21600, 26400, 28200)),
        RateTier(2900, 3000, intArrayOf(6300, 11350, 13225, 13850, 17650, 22675, 27725, 29600)),
        RateTier(3000, 3100, intArrayOf(6600, 11875, 13850, 14525, 18475, 23750, 29050, 31025)),
        RateTier(3100, 3200, intArrayOf(6875, 12375, 14450, 15125, 19250, 24750, 30250, 32325)),
        RateTier(3200, 3300, intArrayOf(7125, 12825, 14975, 15675, 19950, 25650, 31350, 33500)),
        RateTier(3300, 3400, intArrayOf(7400, 13325, 15550, 16275, 20725, 26650, 32550, 34775)),
        RateTier(3400, 3500, intArrayOf(7725, 13900, 16225, 17000, 21625, 27800, 34000, 36300)),
        RateTier(3500, 3600, intArrayOf(8050, 14500, 16900, 17700, 22550, 28975, 35425, 37825)),
        RateTier(3600, 3700, intArrayOf(8525, 15350, 17900, 18750, 23875, 30700, 37500, 40075)),
        RateTier(3700, 3800, intArrayOf(8850, 15925, 18575, 19475, 24775, 31850, 38950, 41600)),
        RateTier(3800, 3900, intArrayOf(9200, 16550, 19325, 20250, 25750, 33125, 40475, 43250)),
        RateTier(3900, 4000, intArrayOf(9475, 17050, 19900, 20850, 26525, 34100, 41700, 44525)),
        RateTier(4000, 4100, intArrayOf(9625, 17325, 20225, 21175, 26950, 34650, 42350, 45250)),
        RateTier(4100, 4200, intArrayOf(9975, 17950, 20950, 21950, 27925, 35900, 43900, 46875)),
        RateTier(4200, 4300, intArrayOf(10550, 19000, 22150, 23200, 29550, 37975, 46425, 49575)),
        RateTier(4300, 4400, intArrayOf(10850, 19525, 22775, 23875, 30375, 39050, 47750, 50000)),
        RateTier(4400, 4500, intArrayOf(11075, 19925, 23250, 24375, 31000, 39875, 48725, 50000)),
        RateTier(4500, 4600, intArrayOf(11350, 20425, 23825, 24975, 31775, 40850, 49950, 50000)),
        RateTier(4600, 4700, intArrayOf(11475, 20650, 24100, 25250, 32125, 41300, 50000, 50000)),
        RateTier(4700, 4800, intArrayOf(11875, 21375, 24950, 26125, 33250, 42750, 50000, 50000)),
        RateTier(4800, 4900, intArrayOf(12300, 22150, 25825, 27050, 34450, 44275, 50000, 50000)),
        RateTier(4900, 5000, intArrayOf(12625, 22725, 26525, 27775, 35350, 45450, 50000, 50000)),
        RateTier(5000, 5100, intArrayOf(12750, 22950, 26775, 28050, 35700, 45900, 50000, 50000)),
        RateTier(5100, 5200, intArrayOf(13075, 23525, 27450, 28775, 36600, 47075, 50000, 50000)),
        RateTier(5200, 5300, intArrayOf(13275, 23900, 27875, 29200, 37175, 47800, 50000, 50000)),
        RateTier(5300, 5400, intArrayOf(13600, 24475, 28550, 29925, 38075, 48950, 50000, 50000)),
        RateTier(5400, 5500, intArrayOf(13675, 24625, 28725, 30075, 38300, 49225, 50000, 50000)),
        RateTier(5500, 5600, intArrayOf(13975, 25150, 29350, 30750, 39125, 50000, 50000, 50000)),
        RateTier(5600, 5700, intArrayOf(14200, 25550, 29825, 31250, 39750, 50000, 50000, 50000)),
        RateTier(5700, 5800, intArrayOf(14525, 26150, 30500, 31950, 40675, 50000, 50000, 50000)),
        RateTier(5800, 5900, intArrayOf(14675, 26425, 30825, 32275, 41100, 50000, 50000, 50000)),
        RateTier(5900, 6000, intArrayOf(15000, 27000, 31500, 33000, 42000, 50000, 50000, 50000)),
        RateTier(6000, 6100, intArrayOf(15150, 27275, 31825, 33325, 42425, 50000, 50000, 50000)),
        RateTier(6100, 6200, intArrayOf(15475, 27850, 32500, 34050, 43325, 50000, 50000, 50000)),
        RateTier(6200, 6300, intArrayOf(15650, 28175, 32875, 34425, 43825, 50000, 50000, 50000)),
        RateTier(6300, 6400, intArrayOf(15975, 28750, 33550, 35150, 44725, 50000, 50000, 50000)),
        RateTier(6400, 6500, intArrayOf(16200, 29150, 34025, 35650, 45350, 50000, 50000, 50000)),
        RateTier(6500, 6600, intArrayOf(16525, 29750, 34700, 36350, 46275, 50000, 50000, 50000)),
        RateTier(6600, 6700, intArrayOf(16650, 29975, 34975, 36625, 46625, 50000, 50000, 50000)),
        RateTier(6700, 6800, intArrayOf(16975, 30550, 35650, 37350, 47525, 50000, 50000, 50000)),
        RateTier(6800, 6900, intArrayOf(17100, 30775, 35900, 37625, 47875, 50000, 50000, 50000)),
        RateTier(6900, 7000, intArrayOf(17375, 31275, 36500, 38225, 48650, 50000, 50000, 50000)),
        RateTier(7000, 7200, intArrayOf(17650, 31775, 37075, 38825, 49425, 50000, 50000, 50000)),
        RateTier(7200, 7400, intArrayOf(18125, 32625, 38075, 39875, 50000, 50000, 50000, 50000)),
        RateTier(7400, 7600, intArrayOf(18550, 33400, 38950, 40800, 50000, 50000, 50000, 50000)),
        RateTier(7600, 7800, intArrayOf(19050, 34300, 40000, 41900, 50000, 50000, 50000, 50000)),
        RateTier(7800, 8000, intArrayOf(19425, 34975, 40800, 42725, 50000, 50000, 50000, 50000)),
        RateTier(8000, 8200, intArrayOf(20025, 36050, 42050, 44050, 50000, 50000, 50000, 50000)),
        RateTier(8200, 8400, intArrayOf(20425, 36775, 42900, 44925, 50000, 50000, 50000, 50000)),
        RateTier(8400, 8700, intArrayOf(21475, 38650, 45100, 47250, 50000, 50000, 50000, 50000)),
        RateTier(8700, 9000, intArrayOf(21925, 39475, 46050, 48225, 50000, 50000, 50000, 50000)),
        RateTier(9000, 9300, intArrayOf(22900, 41225, 48100, 50000, 50000, 50000, 50000, 50000)),
        RateTier(9300, 9600, intArrayOf(23375, 42075, 49100, 50000, 50000, 50000, 50000, 50000)),
        RateTier(9600, 9900, intArrayOf(23925, 43075, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(9900, 10200, intArrayOf(24875, 44775, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(10200, 10500, intArrayOf(25400, 45725, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(10500, 10800, intArrayOf(26325, 47375, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(10800, 11100, intArrayOf(27400, 49325, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(11100, 11400, intArrayOf(28125, 50000, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(11400, 11700, intArrayOf(28850, 50000, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(11700, 12000, intArrayOf(29575, 50000, 50000, 50000, 50000, 50000, 50000, 50000)),
        RateTier(12000, Int.MAX_VALUE, intArrayOf(30300, 50000, 50000, 50000, 50000, 50000, 50000, 50000))
    )

    val PICKUP_AND_RETURN_TIERS = listOf(
        RateTier(1, 15, intArrayOf(15, 30, 30, 40, 40, 50, 70, 70)),
        RateTier(15, 30, intArrayOf(25, 60, 60, 60, 70, 90, 110, 120)),
        RateTier(30, 90, intArrayOf(30, 70, 70, 70, 90, 110, 130, 140)),
        RateTier(90, 150, intArrayOf(50, 100, 100, 100, 150, 175, 225, 225)),
        RateTier(150, 300, intArrayOf(90, 150, 200, 200, 250, 325, 400, 425)),
        RateTier(300, 450, intArrayOf(140, 250, 300, 300, 400, 500, 625, 650)),
        RateTier(450, 600, intArrayOf(175, 325, 375, 375, 500, 625, 775, 825)),
        RateTier(600, 750, intArrayOf(225, 400, 475, 500, 625, 800, 1000, 1050)),
        RateTier(750, 900, intArrayOf(275, 500, 575, 600, 775, 1000, 1200, 1300)),
        RateTier(900, 1200, intArrayOf(325, 575, 675, 725, 900, 1175, 1425, 1525)),
        RateTier(1200, 1500, intArrayOf(425, 775, 900, 925, 1200, 1525, 1875, 2000)),
        RateTier(1500, 1800, intArrayOf(500, 900, 1050, 1100, 1400, 1800, 2200, 2350)),
        RateTier(1800, 2100, intArrayOf(525, 950, 1100, 1150, 1475, 1900, 2300, 2475)),
        RateTier(2100, 2400, intArrayOf(600, 1075, 1250, 1325, 1675, 2150, 2650, 2825)),
        RateTier(2400, 2700, intArrayOf(675, 1225, 1425, 1475, 1900, 2425, 2975, 3175)),
        RateTier(2700, 3000, intArrayOf(750, 1350, 1575, 1650, 2100, 2700, 3300, 3525)),
        RateTier(3000, 3600, intArrayOf(850, 1525, 1775, 1875, 2375, 3050, 3750, 4000)),
        RateTier(3600, 4200, intArrayOf(1050, 1900, 2200, 2300, 2950, 3775, 4625, 4925)),
        RateTier(4200, 4800, intArrayOf(1300, 2350, 2725, 2850, 3650, 4675, 5725, 6100)),
        RateTier(4800, 5400, intArrayOf(1575, 2825, 3300, 3475, 4400, 5675, 6925, 7400)),
        RateTier(5400, 6000, intArrayOf(1850, 3325, 3875, 4075, 5175, 6650, 8150, 8700)),
        RateTier(6000, 6600, intArrayOf(2025, 3650, 4250, 4450, 5675, 7300, 8900, 9525)),
        RateTier(6600, 8100, intArrayOf(2350, 4225, 4925, 5175, 6575, 8450, 10350, 11050)),
        RateTier(8100, 9600, intArrayOf(2775, 5000, 5825, 6100, 7775, 10000, 12200, 13050)),
        RateTier(9600, 11100, intArrayOf(3350, 6025, 7025, 7375, 9375, 12050, 14750, 15750)),
        RateTier(11100, 12600, intArrayOf(3900, 7025, 8200, 8575, 10925, 14050, 17150, 18325)),
        RateTier(12600, 14100, intArrayOf(4375, 7875, 9200, 9625, 12250, 15750, 19250, 20575)),
        RateTier(14100, 15600, intArrayOf(4950, 8900, 10400, 10900, 13850, 17825, 21775, 23275)),
        RateTier(15600, 17100, intArrayOf(5550, 10000, 11650, 12200, 15550, 19975, 24425, 26075)),
        RateTier(17100, 18600, intArrayOf(6225, 11200, 13075, 13700, 17425, 22400, 27400, 29250)),
        RateTier(18600, 21000, intArrayOf(7200, 12950, 15125, 15850, 20150, 25925, 31675, 33850)),
        RateTier(21000, 23400, intArrayOf(8325, 14975, 17475, 18325, 23300, 29975, 36625, 39125)),
        RateTier(23400, 25800, intArrayOf(9450, 17000, 19850, 20800, 26450, 34025, 41575, 44425)),
        RateTier(25800, 28200, intArrayOf(10975, 19750, 23050, 24150, 30725, 39500, 48300, 51575)),
        RateTier(28200, 30600, intArrayOf(12250, 22050, 25725, 26950, 34300, 44100, 53900, 57575)),
        RateTier(30600, 33000, intArrayOf(13200, 23750, 27725, 29050, 36950, 47525, 58075, 62050)),
        RateTier(33000, 35400, intArrayOf(14150, 25475, 29725, 31125, 39625, 50950, 62250, 66500)),
        RateTier(35400, 39400, intArrayOf(15450, 27800, 32450, 34000, 43250, 55625, 67975, 72625)),
        RateTier(39400, 43400, intArrayOf(17050, 30700, 35800, 37500, 47750, 61375, 75025, 80125)),
        RateTier(43400, 47400, intArrayOf(18650, 33575, 39175, 41025, 52225, 67150, 82050, 87650)),
        RateTier(47400, 51400, intArrayOf(20250, 36450, 42525, 44550, 56700, 72900, 89100, 95175)),
        RateTier(51400, 57400, intArrayOf(22450, 40400, 47150, 49400, 62850, 80825, 98775, 100000)),
        RateTier(57400, 63400, intArrayOf(24850, 44725, 52175, 54675, 69575, 89450, 100000, 100000)),
        RateTier(63400, 69400, intArrayOf(27250, 49050, 57225, 59950, 76300, 98100, 100000, 100000)),
        RateTier(69400, 75400, intArrayOf(29650, 53375, 62275, 65225, 83025, 100000, 100000, 100000)),
        RateTier(75400, Int.MAX_VALUE, intArrayOf(33500, 60300, 70350, 73700, 93800, 100000, 100000, 100000))
    )
}

class MainActivity : AppCompatActivity() {

    private lateinit var tvResult: TextView
    private lateinit var btnScan: Button
    private val formatter = DecimalFormat("#,###")

    private val selectImageLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            processImage(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(40, 60, 40, 60)
            gravity = Gravity.CENTER_HORIZONTAL
            setBackgroundColor(Color.parseColor("#F5F5F5"))
        }

        val tvTitle = TextView(this).apply {
            text = "TÍNH LƯƠNG TÀI XẾ SPX (KV1)"
            textSize = 20f
            setTextColor(Color.parseColor("#EE4D2D"))
            gravity = Gravity.CENTER
            setPadding(0, 0, 0, 40)
        }

        btnScan = Button(this).apply {
            text = "📷 CHỌN ẢNH CHỤP ĐƠN HÀNG"
            setBackgroundColor(Color.parseColor("#EE4D2D"))
            setTextColor(Color.WHITE)
            textSize = 16f
            setOnClickListener { selectImageLauncher.launch("image/*") }
        }

        val scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            ).apply { topMargin = 40 }
        }

        tvResult = TextView(this).apply {
            text = "Vui lòng chọn ảnh chụp màn hình ứng dụng SPX để tự động quét ngày công và tính tiền."
            textSize = 15f
            setTextColor(Color.DKGRAY)
            setLineSpacing(10f, 1.2f)
        }

        scrollView.addView(tvResult)
        rootLayout.addView(tvTitle)
        rootLayout.addView(btnScan)
        rootLayout.addView(scrollView)

        setContentView(rootLayout)
    }

    private fun processImage(uri: Uri) {
        tvResult.text = "Đang xử lý đọc ảnh, vui lòng đợi..."
        try {
            val image = InputImage.fromFilePath(this, uri)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val resultText = parseAndCalculate(visionText.text)
                    tvResult.text = resultText
                }
                .addOnFailureListener { e ->
                    tvResult.text = "Lỗi đọc ảnh: ${e.localizedMessage}"
                }
        } catch (e: Exception) {
            tvResult.text = "Lỗi nạp file ảnh: ${e.localizedMessage}"
        }
    }

    private fun parseAndCalculate(text: String): String {
        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }

        val dateMatch = Regex("""(\d{2}/\d{2})""").find(text)
        val date = dateMatch?.value ?: "Chưa rõ"

        val type = when {
            text.contains("trả hàng", ignoreCase = true) -> "HOAN"
            text.contains("lấy", ignoreCase = true) -> "LAY"
            else -> "GIAO"
        }

        val items = mutableMapOf<Int, Int>()

        for (i in lines.indices) {
            val line = lines[i]
            val colIndex = when {
                line.contains("0.000 - 2.001") || line.contains("0 - 2") -> 0
                line.contains("2.001 - 4.001") || line.contains("2 - 4") -> 1
                line.contains("4.001 - 6.001") || line.contains("4 - 6") -> 2
                line.contains("6.001 - 8.001") || line.contains("6 - 8") -> 3
                line.contains("8.001 - 10.001") || line.contains("8 - 10") -> 4
                line.contains("10.001 - 12.001") || line.contains("10 - 12") -> 5
                line.contains("12.001 - 15.001") || line.contains("12 - 15") -> 6
                line.contains("> 15") || line.contains("15.001") -> 7
                else -> -1
            }

            if (colIndex != -1) {
                for (j in 1..3) {
                    if (i + j < lines.size) {
                        val subLine = lines[i + j]
                        val match = Regex("""(\d+)\s*(Đơn hàng|Đơn|don)?""", RegexOption.IGNORE_CASE).find(subLine)
                        if (match != null) {
                            items[colIndex] = match.groupValues[1].toIntOrNull() ?: 0
                            break
                        }
                    }
                }
            }
        }

        val tiers = if (type == "GIAO") SpxRateTables.DELIVERY_TIERS else SpxRateTables.PICKUP_AND_RETURN_TIERS
        var totalOrders = 0
        var dayEarnings = 0L
        val breakdown = StringBuilder()

        items.forEach { (col, count) ->
            totalOrders += count
            val tier = tiers.lastOrNull { count >= it.minOrder } ?: tiers.first()
            val money = tier.rates[col].toLong() * 1000L
            dayEarnings += money

            val colName = when (col) {
                0 -> "0-2kg"
                1 -> "2-4kg"
                2 -> "4-6kg"
                3 -> "6-8kg"
                4 -> "8-10kg"
                5 -> "10-12kg"
                6 -> "12-15kg"
                else -> ">15kg"
            }
            breakdown.append("• Dải $colName: $count đơn -> +${formatter.format(money)} đ\n")
        }

        val workCredit = when {
            totalOrders >= 80 -> 1.0
            totalOrders >= 40 -> 0.5
            else -> 0.0
        }

        return """
            KẾT QUẢ QUÉT:
            ────────────────────────
            📅 Ngày: $date
            📦 Loại đơn: Đơn $type
            🚚 Tổng số đơn: $totalOrders đơn
            ⭐ Ngày công: $workCredit công (Định mức 40/80)
            
            CHI TIẾT SẢN LƯỢNG:
            $breakdown
            ────────────────────────
            💰 TIỀN SẢN LƯỢNG NGÀY: 
            ${formatter.format(dayEarnings)} VNĐ
            
            📌 Lương cơ bản KV1: 5.310.000 đ / 26 công
        """.trimIndent()
    }
}
