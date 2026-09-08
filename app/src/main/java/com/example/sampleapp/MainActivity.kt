package com.example.sampleapp

import android.app.AlertDialog
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.net.Uri
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class RateTier(val minOrder: Int, val maxOrder: Int, val rates: IntArray)
data class DayLog(val date: String, val totalOrders: Int, val note: String)

object SpxRateTables {
    val WEIGHT_LABELS = listOf(
        ">0 - 2 kg", ">2 - 4 kg", ">4 - 6 kg", ">6 - 8 kg",
        ">8 - 10 kg", ">10 - 12 kg", ">12 - 15 kg", ">15 kg"
    )

    // Bảng tính Đơn Giao (áp dụng chung chuẩn các hub)
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
        RateTier(2000, Int.MAX_VALUE, intArrayOf(4050, 7300, 8500, 8900, 11350, 14575, 17825, 19025))
    )

    // Bảng tính Đơn Lấy & Trả hàng hoàn
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
        RateTier(1500, Int.MAX_VALUE, intArrayOf(500, 900, 1050, 1100, 1400, 1800, 2200, 2350))
    )

    // Mức lương cơ sở tối thiểu 4 vùng (chuẩn 26 ngày công)
    val REGION_SALARIES = mapOf(
        "KV1" to 5310000L, // Vùng 1
        "KV2" to 4730000L, // Vùng 2
        "KV3" to 4140000L, // Vùng 3
        "KV4" to 3700000L  // Vùng 4
    )
}

class MainActivity : AppCompatActivity() {

    private val fmt = DecimalFormat("#,###")
    private var currentTab = 0 // 0: Giao, 1: Lấy, 2: Hoàn, 3: Tổng Kết
    private var selectedRegion = "KV1" // Mặc định KV1, có thể đổi sang KV2, KV3, KV4
    private var manualWorkDays = 26.0 // Số ngày công tiêu chuẩn trong tháng

    private val deliveryCounts = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0)
    private val pickupCounts = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0)
    private val returnCounts = mutableMapOf(0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0)

    private val deliveryLogs = mutableListOf<DayLog>()
    private val pickupLogs = mutableListOf<DayLog>()
    private val returnLogs = mutableListOf<DayLog>()

    private lateinit var contentScrollView: ScrollView
    private lateinit var contentLayout: LinearLayout
    private val navTabViews = mutableListOf<LinearLayout>()
    private lateinit var btnRegionSelector: TextView

    private val photoPickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) parseOcrImage(uri)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val rootLayout = RelativeLayout(this).apply {
            setBackgroundColor(Color.parseColor("#F7F8FA"))
        }

        val header = createHeader()
        rootLayout.addView(header)

        val bottomNav = createBottomNav()
        rootLayout.addView(bottomNav)

        contentScrollView = ScrollView(this).apply {
            val p = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT
            ).apply {
                addRule(RelativeLayout.BELOW, header.id)
                addRule(RelativeLayout.ABOVE, bottomNav.id)
            }
            layoutParams = p
        }

        contentLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(30, 20, 30, 160)
        }
        contentScrollView.addView(contentLayout)
        rootLayout.addView(contentScrollView)

        val fab = createFloatingActionButton(bottomNav.id)
        rootLayout.addView(fab)

        setContentView(rootLayout)
        switchTab(0)
    }

    private fun createHeader(): View {
        return RelativeLayout(this).apply {
            id = View.generateViewId()
            setBackgroundColor(Color.WHITE)
            setPadding(30, 25, 30, 20)
            elevation = 6f

            val logoBox = LinearLayout(this@MainActivity).apply {
                id = View.generateViewId()
                background = makeRounded(Color.parseColor("#EE4D2D"), 14f)
                setPadding(14, 6, 14, 6)
                val tv = TextView(this@MainActivity).apply {
                    text = "SPX"
                    setTextColor(Color.WHITE)
                    textSize = 12f
                    typeface = Typeface.DEFAULT_BOLD
                }
                addView(tv)
            }

            val title = TextView(this@MainActivity).apply {
                text = "Sản Lượng SPX"
                textSize = 17f
                setTextColor(Color.parseColor("#1F2937"))
                typeface = Typeface.DEFAULT_BOLD
                setPadding(16, 0, 0, 0)
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.RIGHT_OF, logoBox.id)
                    addRule(RelativeLayout.CENTER_VERTICAL)
                }
                layoutParams = p
            }

            // Cụm chức năng bên phải: Nút chọn Khu Vực + Nút Reset
            val rightGroup = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                gravity = Gravity.CENTER_VERTICAL
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    addRule(RelativeLayout.CENTER_VERTICAL)
                }
                layoutParams = p

                // Nút chọn nhanh Khu Vực (KV1 - KV4)
                btnRegionSelector = TextView(this@MainActivity).apply {
                    text = "📍 $selectedRegion"
                    textSize = 12f
                    typeface = Typeface.DEFAULT_BOLD
                    setTextColor(Color.parseColor("#1E40AF"))
                    background = makeRounded(Color.parseColor("#DBEAFE"), 14f)
                    setPadding(16, 10, 16, 10)
                    setOnClickListener { showRegionPickerDialog() }
                }

                val space = View(this@MainActivity).apply { layoutParams = LinearLayout.LayoutParams(14, 1) }

                // Nút Reset
                val btnReset = TextView(this@MainActivity).apply {
                    text = "🔄"
                    textSize = 14f
                    background = makeRounded(Color.parseColor("#FEE2E2"), 14f)
                    setPadding(16, 8, 16, 8)
                    setOnClickListener { showResetConfirmationDialog() }
                }

                addView(btnRegionSelector)
                addView(space)
                addView(btnReset)
            }

            addView(logoBox)
            addView(title)
            addView(rightGroup)
        }
    }

    private fun showRegionPickerDialog() {
        val regions = arrayOf(
            "Vùng 1 (KV1) - Lương CB: 5.310.000 đ",
            "Vùng 2 (KV2) - Lương CB: 4.730.000 đ",
            "Vùng 3 (KV3) - Lương CB: 4.140.000 đ",
            "Vùng 4 (KV4) - Lương CB: 3.700.000 đ"
        )
        val keys = listOf("KV1", "KV2", "KV3", "KV4")

        AlertDialog.Builder(this)
            .setTitle("Chọn Khu Vực Hoạt Động")
            .setItems(regions) { _, which ->
                selectedRegion = keys[which]
                btnRegionSelector.text = "📍 $selectedRegion"
                renderCurrentTabContent()
                Toast.makeText(this, "Đã chuyển sang $selectedRegion", Toast.LENGTH_SHORT).show()
            }
            .show()
    }

    private fun showResetConfirmationDialog() {
        val tabNames = listOf("Đơn Giao", "Đơn Lấy", "Đơn Hoàn", "Tổng Kết")
        val currentName = tabNames[currentTab]

        val options = arrayOf("Xóa dữ liệu mục $currentName", "Xóa toàn bộ cả tháng (Giao + Lấy + Hoàn)")
        AlertDialog.Builder(this)
            .setTitle("Đặt lại dữ liệu")
            .setItems(options) { _, which ->
                if (which == 0) {
                    when (currentTab) {
                        0 -> { deliveryCounts.keys.forEach { deliveryCounts[it] = 0 }; deliveryLogs.clear() }
                        1 -> { pickupCounts.keys.forEach { pickupCounts[it] = 0 }; pickupLogs.clear() }
                        2 -> { returnCounts.keys.forEach { returnCounts[it] = 0 }; returnLogs.clear() }
                    }
                    Toast.makeText(this, "Đã làm trống $currentName", Toast.LENGTH_SHORT).show()
                } else {
                    deliveryCounts.keys.forEach { deliveryCounts[it] = 0 }; deliveryLogs.clear()
                    pickupCounts.keys.forEach { pickupCounts[it] = 0 }; pickupLogs.clear()
                    returnCounts.keys.forEach { returnCounts[it] = 0 }; returnLogs.clear()
                    Toast.makeText(this, "Đã xóa toàn bộ dữ liệu ứng dụng", Toast.LENGTH_SHORT).show()
                }
                renderCurrentTabContent()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun createBottomNav(): View {
        val nav = LinearLayout(this).apply {
            id = View.generateViewId()
            orientation = LinearLayout.HORIZONTAL
            setBackgroundColor(Color.WHITE)
            setPadding(0, 18, 0, 20)
            elevation = 20f
            val p = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply { addRule(RelativeLayout.ALIGN_PARENT_BOTTOM) }
            layoutParams = p
        }

        val items = listOf(
            Pair("🚚", "Đơn Giao"),
            Pair("📥", "Đơn Lấy"),
            Pair("🔄", "Đơn Hoàn"),
            Pair("📊", "Tổng Kết")
        )

        val navP = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
        for (i in items.indices) {
            val tabItem = LinearLayout(this).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER
                setPadding(0, 4, 0, 4)
                setOnClickListener { switchTab(i) }
                val icon = TextView(this@MainActivity).apply {
                    text = items[i].first
                    textSize = 20f
                    gravity = Gravity.CENTER
                }
                val title = TextView(this@MainActivity).apply {
                    text = items[i].second
                    textSize = 12f
                    typeface = Typeface.DEFAULT_BOLD
                    gravity = Gravity.CENTER
                    setPadding(0, 4, 0, 0)
                }
                addView(icon)
                addView(title)
            }
            navTabViews.add(tabItem)
            nav.addView(tabItem, navP)
        }
        return nav
    }

    private fun createFloatingActionButton(bottomNavId: Int): View {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            background = makeRounded(Color.parseColor("#EE4D2D"), 40f)
            setPadding(35, 20, 35, 20)
            gravity = Gravity.CENTER
            elevation = 16f
            setOnClickListener { photoPickerLauncher.launch("image/*") }

            val p = RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                addRule(RelativeLayout.ABOVE, bottomNavId)
                rightMargin = 30
                bottomMargin = 25
            }
            layoutParams = p

            val star = TextView(this@MainActivity).apply { text = "✨ "; setTextColor(Color.WHITE); textSize = 14f }
            val label = TextView(this@MainActivity).apply {
                text = "Quét ảnh SPX"
                setTextColor(Color.WHITE)
                typeface = Typeface.DEFAULT_BOLD
                textSize = 13f
            }
            addView(star)
            addView(label)
        }
    }

    private fun switchTab(tabIndex: Int) {
        currentTab = tabIndex
        for (i in navTabViews.indices) {
            val tv = navTabViews[i].getChildAt(1) as TextView
            if (i == tabIndex) {
                tv.setTextColor(Color.parseColor("#EE4D2D"))
            } else {
                tv.setTextColor(Color.parseColor("#9CA3AF"))
            }
        }
        renderCurrentTabContent()
    }

    private fun renderCurrentTabContent() {
        contentLayout.removeAllViews()
        if (currentTab == 3) {
            renderSummaryTab()
        } else {
            renderOrderTypeTab()
        }
    }

    // TÍNH TOÁN THEO MỐC LŨY KẾ VÀ ĐƠN GIÁ BẬC THANG
    private fun renderOrderTypeTab() {
        val (counts, tiers, logs, titleStr) = when (currentTab) {
            0 -> Tuple4(deliveryCounts, SpxRateTables.DELIVERY_TIERS, deliveryLogs, "giao")
            1 -> Tuple4(pickupCounts, SpxRateTables.PICKUP_AND_RETURN_TIERS, pickupLogs, "lấy")
            else -> Tuple4(returnCounts, SpxRateTables.PICKUP_AND_RETURN_TIERS, returnLogs, "hoàn")
        }

        val totalCumulativeOrders = counts.values.sum()
        val globalTier = if (totalCumulativeOrders > 0) {
            tiers.lastOrNull { totalCumulativeOrders >= it.minOrder } ?: tiers.first()
        } else null

        var totalMoney = 0L
        val tierRates = globalTier?.rates

        val mainCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = makeRounded(Color.WHITE, 28f)
            setPadding(40, 35, 40, 40)
            elevation = 4f
        }

        val topRow = RelativeLayout(this).apply {
            val walletIcon = TextView(this@MainActivity).apply {
                id = View.generateViewId()
                text = "💵"
                textSize = 24f
                background = makeRounded(Color.parseColor("#E8F5E9"), 20f)
                setPadding(16, 12, 16, 12)
            }
            val labelBox = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.RIGHT_OF, walletIcon.id)
                    leftMargin = 20
                }
                layoutParams = p

                val sub = TextView(this@MainActivity).apply {
                    text = "Lương lũy kế ($totalCumulativeOrders đơn đã dồn)"
                    textSize = 12f
                    setTextColor(Color.parseColor("#6B7280"))
                }
                val tvMoney = TextView(this@MainActivity).apply {
                    id = View.generateViewId()
                    textSize = 23f
                    typeface = Typeface.DEFAULT_BOLD
                    setTextColor(Color.parseColor("#EE4D2D"))
                }
                val tierText = TextView(this@MainActivity).apply {
                    text = if (globalTier != null) {
                        "Mốc lũy kế: ${globalTier.minOrder} - ${if (globalTier.maxOrder == Int.MAX_VALUE) "+" else globalTier.maxOrder} đơn"
                    } else {
                        "Chưa đạt mốc tính"
                    }
                    textSize = 12f
                    setTextColor(Color.parseColor("#EE4D2D"))
                }
                addView(sub)
                addView(tvMoney)
                addView(tierText)
            }
            addView(walletIcon)
            addView(labelBox)
        }
        mainCard.addView(topRow)

        val tvSubTable = TextView(this).apply {
            text = "Chi tiết theo từng mức cân ($selectedRegion)"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#1F2937"))
            setPadding(0, 35, 0, 20)
        }
        mainCard.addView(tvSubTable)

        val table = TableLayout(this).apply { isStretchAllColumns = true }
        val headerRow = TableRow(this).apply {
            background = makeRounded(Color.parseColor("#F3F4F6"), 12f)
            setPadding(20, 16, 20, 16)
            addView(createCell("Cân nặng", true, Color.parseColor("#374151")))
            addView(createCell("Tổng đơn", true, Color.parseColor("#374151")))
            addView(createCell("Mốc tính", true, Color.parseColor("#374151")))
            addView(createCell("Thành tiền", true, Color.parseColor("#374151")))
        }
        table.addView(headerRow)

        for (i in 0..7) {
            val count = counts[i] ?: 0
            val itemMoney = if (tierRates != null && count > 0) {
                tierRates[i].toLong() * 1000L
            } else 0L
            totalMoney += itemMoney

            val row = TableRow(this).apply {
                setPadding(20, 14, 20, 14)
                addView(createCell(SpxRateTables.WEIGHT_LABELS[i], false, if (count > 0) Color.parseColor("#111827") else Color.parseColor("#9CA3AF")))
                addView(createCell(if (count > 0) "$count" else "0", false, if (count > 0) Color.parseColor("#EE4D2D") else Color.parseColor("#9CA3AF"), true))
                addView(createCell(if (globalTier != null && count > 0) "${globalTier.minOrder} - ${if (globalTier.maxOrder == Int.MAX_VALUE) "+" else globalTier.maxOrder}" else "-", false, if (count > 0) Color.parseColor("#EE4D2D") else Color.parseColor("#9CA3AF")))
                addView(createCell(if (count > 0) "${fmt.format(itemMoney)} đ" else "0 đ", false, if (count > 0) Color.parseColor("#10B981") else Color.parseColor("#9CA3AF"), true))
            }
            table.addView(row)
        }
        mainCard.addView(table)
        contentLayout.addView(mainCard)

        // Cập nhật text hiển thị số tiền
        (topRow.findViewById<LinearLayout>(topRow.getChildAt(1).id)?.getChildAt(1) as? TextView)?.text = "${fmt.format(totalMoney)} đ"

        // Nút bấm thao tác
        val btnRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setPadding(0, 30, 0, 30)
        }
        val btnScanAction = Button(this).apply {
            text = "✨  QUÉT TỪ ẢNH"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.WHITE)
            background = makeRounded(Color.parseColor("#EE4D2D"), 18f)
            setOnClickListener { photoPickerLauncher.launch("image/*") }
        }
        val btnAddAction = Button(this).apply {
            text = "＋  THÊM SỐ LIỆU"
            textSize = 14f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#374151"))
            background = makeRoundedStroke(Color.WHITE, Color.parseColor("#D1D5DB"), 18f)
            setOnClickListener { showAddOrderDialog(counts, logs) }
        }
        val halfP = LinearLayout.LayoutParams(0, 120, 1f)
        btnRow.addView(btnScanAction, halfP)
        val space = View(this).apply { layoutParams = LinearLayout.LayoutParams(25, 1) }
        btnRow.addView(space)
        btnRow.addView(btnAddAction, halfP)
        contentLayout.addView(btnRow)

        // Nhật ký theo ngày
        val logHeader = RelativeLayout(this).apply {
            val tvTitle = TextView(this@MainActivity).apply {
                text = "Nhật ký sản lượng từng ngày"
                textSize = 15f
                typeface = Typeface.DEFAULT_BOLD
                setTextColor(Color.parseColor("#1F2937"))
            }
            val tvCountBadge = TextView(this@MainActivity).apply {
                text = "${logs.size} ngày"
                textSize = 11f
                setTextColor(Color.parseColor("#6B7280"))
                background = makeRounded(Color.parseColor("#F3F4F6"), 20f)
                setPadding(16, 6, 16, 6)
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply { leftMargin = 20 }
                layoutParams = p
            }
            val box = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                addView(tvTitle)
                addView(tvCountBadge)
            }
            val tvExport = TextView(this@MainActivity).apply {
                text = "Xuất Excel"
                textSize = 12f
                setTextColor(Color.parseColor("#EE4D2D"))
                typeface = Typeface.DEFAULT_BOLD
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply { addRule(RelativeLayout.ALIGN_PARENT_RIGHT) }
                layoutParams = p
                setOnClickListener { Toast.makeText(this@MainActivity, "Đang xuất dữ liệu...", Toast.LENGTH_SHORT).show() }
            }
            addView(box)
            addView(tvExport)
        }
        contentLayout.addView(logHeader)

        val logsContainer = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(0, 20, 0, 0) }
        if (logs.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = "Chưa có dữ liệu. Hãy quét ảnh để bắt đầu cộng dồn."
                textSize = 13f
                setTextColor(Color.parseColor("#9CA3AF"))
                gravity = Gravity.CENTER
                setPadding(0, 40, 0, 40)
            }
            logsContainer.addView(tvEmpty)
        } else {
            for (log in logs) {
                val logCard = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    background = makeRounded(Color.WHITE, 20f)
                    setPadding(35, 28, 35, 28)
                    val p = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { bottomMargin = 20 }
                    layoutParams = p
                }

                val r1 = RelativeLayout(this).apply {
                    val dateBox = LinearLayout(this@MainActivity).apply {
                        orientation = LinearLayout.HORIZONTAL
                        val icon = TextView(this@MainActivity).apply { text = "📅 "; textSize = 13f }
                        val d = TextView(this@MainActivity).apply { text = log.date; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.parseColor("#1F2937")) }
                        addView(icon)
                        addView(d)
                    }
                    val orderBox = TextView(this@MainActivity).apply {
                        text = "+${log.totalOrders} đơn"
                        textSize = 13f
                        typeface = Typeface.DEFAULT_BOLD
                        setTextColor(Color.parseColor("#EE4D2D"))
                        background = makeRounded(Color.parseColor("#FEE2E2"), 14f)
                        setPadding(20, 8, 20, 8)
                        val p = RelativeLayout.LayoutParams(
                            RelativeLayout.LayoutParams.WRAP_CONTENT,
                            RelativeLayout.LayoutParams.WRAP_CONTENT
                        ).apply { addRule(RelativeLayout.ALIGN_PARENT_RIGHT) }
                        layoutParams = p
                    }
                    addView(dateBox)
                    addView(orderBox)
                }
                val noteTv = TextView(this).apply {
                    text = "Ghi chú: ${log.note}"
                    textSize = 12f
                    setTextColor(Color.parseColor("#6B7280"))
                    setPadding(0, 14, 0, 0)
                }
                logCard.addView(r1)
                logCard.addView(noteTv)
                logsContainer.addView(logCard)
            }
        }
        contentLayout.addView(logsContainer)
    }

    // TAB TỔNG KẾT: TÍNH LƯƠNG CƠ BẢN THEO KHU VỰC + LƯƠNG SẢN LƯỢNG
    private fun renderSummaryTab() {
        val baseSalaryMonthly = SpxRateTables.REGION_SALARIES[selectedRegion] ?: 5310000L
        val salaryPerDay = baseSalaryMonthly / 26.0
        val actualBaseSalary = (salaryPerDay * manualWorkDays).toLong()

        // Tiền sản lượng từng mục
        val deliveryMoney = calculateTierTotal(deliveryCounts, SpxRateTables.DELIVERY_TIERS)
        val pickupMoney = calculateTierTotal(pickupCounts, SpxRateTables.PICKUP_AND_RETURN_TIERS)
        val returnMoney = calculateTierTotal(returnCounts, SpxRateTables.PICKUP_AND_RETURN_TIERS)
        val totalProductEarnings = deliveryMoney + pickupMoney + returnMoney
        val grandTotal = actualBaseSalary + totalProductEarnings

        // Card Tổng Thu Nhập Hoàn Chỉnh
        val grandCard = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = makeRounded(Color.parseColor("#1E293B"), 28f)
            setPadding(40, 35, 40, 40)
            elevation = 6f
        }

        val tvTitleGrand = TextView(this).apply {
            text = "TỔNG THU NHẬP THÁNG ($selectedRegion)"
            textSize = 13f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#94A3B8"))
        }
        val tvTotalMoney = TextView(this).apply {
            text = "${fmt.format(grandTotal)} VNĐ"
            textSize = 28f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#38BDF8"))
            setPadding(0, 10, 0, 15)
        }

        val detailBase = TextView(this).apply {
            text = "• Lương cứng ($manualWorkDays công): ${fmt.format(actualBaseSalary)} đ"
            textSize = 13f
            setTextColor(Color.WHITE)
        }
        val detailProd = TextView(this).apply {
            text = "• Lương sản lượng (Giao + Lấy + Hoàn): ${fmt.format(totalProductEarnings)} đ"
            textSize = 13f
            setTextColor(Color.parseColor("#4ADE80"))
            setPadding(0, 6, 0, 0)
        }

        // Cho phép chạm vào để đổi số ngày công
        val btnEditWorkDays = TextView(this).apply {
            text = "✏️ Chạm để chỉnh số ngày công (Hiện tại: $manualWorkDays)"
            textSize = 11f
            setTextColor(Color.parseColor("#CBD5E1"))
            setPadding(0, 16, 0, 0)
            setOnClickListener { showEditWorkDaysDialog() }
        }

        grandCard.addView(tvTitleGrand)
        grandCard.addView(tvTotalMoney)
        grandCard.addView(detailBase)
        grandCard.addView(detailProd)
        grandCard.addView(btnEditWorkDays)
        contentLayout.addView(grandCard)

        // Khối Báo cáo Excel
        val excelCard = RelativeLayout(this).apply {
            background = makeRounded(Color.WHITE, 24f)
            setPadding(35, 30, 35, 30)
            elevation = 3f
            val p = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 30 }
            layoutParams = p

            val iconSheet = TextView(this@MainActivity).apply {
                id = View.generateViewId()
                text = "📊"
                textSize = 24f
                background = makeRounded(Color.parseColor("#E8F5E9"), 16f)
                setPadding(16, 12, 16, 12)
            }

            val textBox = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                val p1 = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.RIGHT_OF, iconSheet.id)
                    leftMargin = 20
                }
                layoutParams = p1

                val t1 = TextView(this@MainActivity).apply { text = "Báo cáo Excel (Tháng này)"; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.parseColor("#111827")) }
                val t2 = TextView(this@MainActivity).apply { text = "Bảng đối soát đầy đủ 8 mức cân $selectedRegion"; textSize = 12f; setTextColor(Color.parseColor("#6B7280")) }
                addView(t1)
                addView(t2)
            }

            val btnExport = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.HORIZONTAL
                background = makeRounded(Color.parseColor("#2E7D32"), 20f)
                setPadding(25, 14, 25, 14)
                gravity = Gravity.CENTER
                val p2 = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    addRule(RelativeLayout.CENTER_VERTICAL)
                }
                layoutParams = p2

                val icon = TextView(this@MainActivity).apply { text = "🔗 "; setTextColor(Color.WHITE); textSize = 11f }
                val label = TextView(this@MainActivity).apply { text = "Xuất file"; setTextColor(Color.WHITE); textSize = 12f; typeface = Typeface.DEFAULT_BOLD }
                addView(icon)
                addView(label)
                setOnClickListener { Toast.makeText(this@MainActivity, "Đang xuất file đối soát...", Toast.LENGTH_SHORT).show() }
            }

            addView(iconSheet)
            addView(textBox)
            addView(btnExport)
        }
        contentLayout.addView(excelCard)

        // Tiêu đề Gợi ý
        val tvTip = TextView(this).apply {
            text = "💡 Gợi ý số đơn cần đạt mốc tiếp theo"
            textSize = 15f
            typeface = Typeface.DEFAULT_BOLD
            setTextColor(Color.parseColor("#111827"))
            setPadding(0, 35, 0, 20)
        }
        contentLayout.addView(tvTip)

        val deliveryCard = buildDynamicSuggestionCard("Bảng tính đơn giao", deliveryCounts, SpxRateTables.DELIVERY_TIERS)
        val pickupCard = buildDynamicSuggestionCard("Bảng tính đơn lấy", pickupCounts, SpxRateTables.PICKUP_AND_RETURN_TIERS)

        contentLayout.addView(deliveryCard)
        val space = View(this).apply { layoutParams = LinearLayout.LayoutParams(1, 30) }
        contentLayout.addView(space)
        contentLayout.addView(pickupCard)
    }

    private fun showEditWorkDaysDialog() {
        val input = EditText(this).apply {
            inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(manualWorkDays.toString())
        }
        AlertDialog.Builder(this)
            .setTitle("Nhập số ngày công làm việc")
            .setView(input)
            .setPositiveButton("Cập nhật") { _, _ ->
                val days = input.text.toString().toDoubleOrNull() ?: 26.0
                manualWorkDays = days
                renderCurrentTabContent()
                Toast.makeText(this, "Đã cập nhật $manualWorkDays ngày công", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun calculateTierTotal(counts: Map<Int, Int>, tiers: List<RateTier>): Long {
        val totalCount = counts.values.sum()
        if (totalCount == 0) return 0L
        val tier = tiers.lastOrNull { totalCount >= it.minOrder } ?: tiers.first()
        var sum = 0L
        counts.forEach { (col, count) ->
            if (count > 0) sum += tier.rates[col].toLong() * 1000L
        }
        return sum
    }

    private fun buildDynamicSuggestionCard(title: String, counts: Map<Int, Int>, tiers: List<RateTier>): View {
        val totalCount = counts.values.sum()
        val curTierIndex = tiers.indexOfLast { totalCount >= it.minOrder }
        val currentTier = if (curTierIndex != -1) tiers[curTierIndex] else tiers.first()
        val tierName = if (totalCount == 0) "Chưa có đơn" else "${currentTier.minOrder} - ${if (currentTier.maxOrder == Int.MAX_VALUE) "+" else currentTier.maxOrder}"

        var currentMoney = 0L
        if (totalCount > 0) {
            counts.forEach { (col, count) ->
                if (count > 0) currentMoney += currentTier.rates[col].toLong() * 1000L
            }
        }

        val items = mutableListOf<Tuple3<String, String, String>>()
        if (curTierIndex + 1 < tiers.size) {
            val nextTier = tiers[curTierIndex + 1]
            val neededOrders = nextTier.minOrder - totalCount

            for (i in 0..7) {
                val count = counts[i] ?: 0
                if (count > 0) {
                    val diff = (nextTier.rates[i] - currentTier.rates[i]).toLong() * 1000L
                    items.add(Tuple3(
                        "Mức ${SpxRateTables.WEIGHT_LABELS[i]} ($count đơn)",
                        "Tổng đơn đạt ${nextTier.minOrder} (Cần thêm +$neededOrders đơn)",
                        "+${fmt.format(diff)} đ"
                    ))
                }
            }
        }

        return createNextTierCard(title, currentMoney, totalCount, tierName, items)
    }

    private fun createNextTierCard(title: String, totalMoney: Long, totalOrders: Int, tierName: String, items: List<Tuple3<String, String, String>>): View {
        val card = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            background = makeRounded(Color.WHITE, 28f)
            setPadding(35, 30, 35, 35)
            elevation = 3f
        }

        val r1 = RelativeLayout(this).apply {
            val icon = TextView(this@MainActivity).apply {
                id = View.generateViewId()
                text = "🚚"
                textSize = 20f
                background = makeRounded(Color.parseColor("#FFF3E0"), 16f)
                setPadding(14, 10, 14, 10)
            }
            val titleBox = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    addRule(RelativeLayout.RIGHT_OF, icon.id)
                    leftMargin = 20
                }
                layoutParams = p

                val t = TextView(this@MainActivity).apply { text = title; textSize = 14f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.parseColor("#1F2937")) }
                val sub = TextView(this@MainActivity).apply { text = "Mốc: $tierName đơn"; textSize = 12f; setTextColor(Color.parseColor("#6B7280")) }
                addView(t)
                addView(sub)
            }
            val rightBox = LinearLayout(this@MainActivity).apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.END
                val p = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT
                ).apply { addRule(RelativeLayout.ALIGN_PARENT_RIGHT) }
                layoutParams = p

                val m = TextView(this@MainActivity).apply { text = "${fmt.format(totalMoney)} đ"; textSize = 16f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.parseColor("#EE4D2D")) }
                val o = TextView(this@MainActivity).apply { text = "$totalOrders đơn"; textSize = 12f; setTextColor(Color.parseColor("#6B7280")) }
                addView(m)
                addView(o)
            }
            addView(icon)
            addView(titleBox)
            addView(rightBox)
        }
        card.addView(r1)

        val divider = View(this).apply {
            background = makeRounded(Color.parseColor("#EE4D2D"), 4f)
            val p = LinearLayout.LayoutParams(120, 6).apply { topMargin = 20; bottomMargin = 25 }
            layoutParams = p
        }
        card.addView(divider)

        if (items.isEmpty()) {
            val tvEmpty = TextView(this).apply {
                text = "Chưa có đủ dữ liệu hoặc đã chạm mức tối đa."
                textSize = 12f
                setTextColor(Color.parseColor("#9CA3AF"))
                setPadding(0, 10, 0, 10)
            }
            card.addView(tvEmpty)
        } else {
            for (item in items.take(6)) {
                val itemBox = LinearLayout(this).apply {
                    orientation = LinearLayout.VERTICAL
                    background = makeRounded(Color.parseColor("#F9FAFB"), 18f)
                    setPadding(30, 20, 30, 20)
                    val p = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply { bottomMargin = 16 }
                    layoutParams = p

                    val row = RelativeLayout(this@MainActivity).apply {
                        val info = LinearLayout(this@MainActivity).apply {
                            orientation = LinearLayout.VERTICAL
                            val t1 = TextView(this@MainActivity).apply { text = item.first; textSize = 12f; typeface = Typeface.DEFAULT_BOLD; setTextColor(Color.parseColor("#1F2937")) }
                            val t2 = TextView(this@MainActivity).apply { text = item.second; textSize = 11f; setTextColor(Color.parseColor("#EE4D2D")); setPadding(0, 4, 0, 0) }
                            addView(t1)
                            addView(t2)
                        }
                        val bonus = TextView(this@MainActivity).apply {
                            text = item.third
                            textSize = 13f
                            typeface = Typeface.DEFAULT_BOLD
                            setTextColor(Color.parseColor("#10B981"))
                            val p = RelativeLayout.LayoutParams(
                                RelativeLayout.LayoutParams.WRAP_CONTENT,
                                RelativeLayout.LayoutParams.WRAP_CONTENT
                            ).apply {
                                addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                                addRule(RelativeLayout.CENTER_VERTICAL)
                            }
                            layoutParams = p
                        }
                        addView(info)
                        addView(bonus)
                    }
                    addView(row)
                }
                card.addView(itemBox)
            }
        }
        return card
    }

    private fun showAddOrderDialog(targetMap: MutableMap<Int, Int>, targetLogs: MutableList<DayLog>) {
        val layout = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(40, 20, 40, 20) }
        val inputs = mutableListOf<EditText>()
        for (i in 0..7) {
            val et = EditText(this).apply {
                hint = "${SpxRateTables.WEIGHT_LABELS[i]} (Hiện có: ${targetMap[i] ?: 0})"
                inputType = InputType.TYPE_CLASS_NUMBER
            }
            inputs.add(et)
            layout.addView(et)
        }
        AlertDialog.Builder(this)
            .setTitle("Nhập thêm đơn theo dải cân")
            .setView(layout)
            .setPositiveButton("Lưu") { _, _ ->
                var addedTotal = 0
                for (i in 0..7) {
                    val txt = inputs[i].text.toString().trim()
                    if (txt.isNotEmpty()) {
                        val count = txt.toIntOrNull() ?: 0
                        targetMap[i] = (targetMap[i] ?: 0) + count
                        addedTotal += count
                    }
                }
                if (addedTotal > 0) {
                    val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
                    targetLogs.add(0, DayLog(today, addedTotal, "Nhập tay"))
                }
                renderCurrentTabContent()
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun parseOcrImage(uri: Uri) {
        Toast.makeText(this, "Đang kiểm tra và đọc ảnh...", Toast.LENGTH_SHORT).show()
        val image = InputImage.fromFilePath(this, uri)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text

                val detectedType = when {
                    text.contains("trả hàng", ignoreCase = true) || text.contains("trả", ignoreCase = true) -> 2 // Hoàn
                    text.contains("lấy hàng", ignoreCase = true) || text.contains("lấy", ignoreCase = true) -> 1 // Lấy
                    else -> 0 // Giao
                }

                val typeNames = listOf("Đơn Giao", "Đơn Lấy", "Đơn Hoàn")

                if (currentTab != 3 && detectedType != currentTab) {
                    val detectedName = typeNames[detectedType]
                    val currentName = typeNames[currentTab]

                    AlertDialog.Builder(this)
                        .setTitle("⚠️ Cảnh báo sai mục báo cáo!")
                        .setMessage("Ảnh tải lên là 【$detectedName】, nhưng bạn đang ở mục 【$currentName】.\n\nChuyển sang tab 【$detectedName】 để lưu chính xác?")
                        .setPositiveButton("Chuyển & Lưu") { _, _ ->
                            switchTab(detectedType)
                            executeDataImport(text, detectedType)
                        }
                        .setNegativeButton("Hủy bỏ", null)
                        .show()
                } else {
                    val targetTab = if (currentTab == 3) detectedType else currentTab
                    if (currentTab == 3) switchTab(detectedType)
                    executeDataImport(text, targetTab)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Lỗi đọc ảnh: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun executeDataImport(text: String, tabIndex: Int) {
        val (targetMap, targetLogs) = when (tabIndex) {
            0 -> Pair(deliveryCounts, deliveryLogs)
            1 -> Pair(pickupCounts, pickupLogs)
            else -> Pair(returnCounts, returnLogs)
        }

        val lines = text.lines().map { it.trim() }.filter { it.isNotEmpty() }
        var dayCount = 0

        for (i in lines.indices) {
            val line = lines[i]
            val idx = when {
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
            if (idx != -1) {
                for (j in 1..3) {
                    if (i + j < lines.size) {
                        val match = Regex("""(\d+)\s*(Đơn hàng|Đơn|don)?""", RegexOption.IGNORE_CASE).find(lines[i + j])
                        if (match != null) {
                            val count = match.groupValues[1].toIntOrNull() ?: 0
                            targetMap[idx] = (targetMap[idx] ?: 0) + count
                            dayCount += count
                            break
                        }
                    }
                }
            }
        }

        val dateMatch = Regex("""(\d{4}[-/]\d{2}[-/]\d{2}|\d{2}[-/]\d{2})""").find(text)
        val date = dateMatch?.value ?: SimpleDateFormat("dd/MM", Locale.getDefault()).format(Date())
        if (dayCount > 0) {
            targetLogs.add(0, DayLog(date, dayCount, "Phân tích tự động từ ảnh"))
        }
        renderCurrentTabContent()
        Toast.makeText(this, "Đã cộng dồn $dayCount đơn vào mốc lũy kế!", Toast.LENGTH_SHORT).show()
    }

    private fun createCell(text: String, isHeader: Boolean, color: Int, isBold: Boolean = false): TextView {
        return TextView(this).apply {
            this.text = text
            textSize = if (isHeader) 12f else 13f
            setTextColor(color)
            if (isHeader || isBold) typeface = Typeface.DEFAULT_BOLD
            gravity = Gravity.CENTER
            setPadding(10, 10, 10, 10)
        }
    }

    private fun makeRounded(colorInt: Int, radiusDp: Float): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radiusDp
            setColor(colorInt)
        }
    }

    private fun makeRoundedStroke(bgColor: Int, strokeColor: Int, radiusDp: Float): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = radiusDp
            setColor(bgColor)
            setStroke(2, strokeColor)
        }
    }

    data class Tuple3<A, B, C>(val first: A, val second: B, val third: C)
    data class Tuple4<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)
}
