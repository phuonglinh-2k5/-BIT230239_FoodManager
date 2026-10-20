package linhpham.bit230239_foodmanager

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import linhpham.bit230239_foodmanager.api.ApiService
import linhpham.bit230239_foodmanager.databinding.ActivityMainBinding
import linhpham.bit230239_foodmanager.model.Dish
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dishAdapter: DishAdapter
    private var fullDishList: List<Dish> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearch()

        // Xử lý sự kiện bấm nút Thêm món ăn
        binding.btnAddDish.setOnClickListener {
            val intent = Intent(this, AddEditDishActivity::class.java)
            startActivity(intent)
        }

        // Xử lý sự kiện chuyển đổi ngôn ngữ Anh <-> Việt
        binding.btnChangeLang.setOnClickListener {
            val currentLocale = resources.configuration.locales.get(0).language
            if (currentLocale == "vi") {
                setAppLocale("en") // Chuyển về tiếng Anh
            } else {
                setAppLocale("vi") // Chuyển sang tiếng Việt
            }
        }

        fetchDishesFromApi()
    }

    override fun onResume() {
        super.onResume()
        fetchDishesFromApi()
    }

    private fun setAppLocale(localeCode: String) {
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = Configuration(resources.configuration)
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        // Khởi động lại Activity để áp dụng ngôn ngữ ngay lập tức
        recreate()
    }

    private fun setupRecyclerView() {
        dishAdapter = DishAdapter(listOf()) { dish ->
            val intent = Intent(this, DetailDishActivity::class.java)
            intent.putExtra("DISH_ID", dish.id.toString())
            intent.putExtra("DISH_NAME", dish.name)
            intent.putExtra("DISH_PRICE", dish.price)
            intent.putExtra("DISH_TIME", dish.preparationTime)
            intent.putExtra("DISH_DESC", dish.description)
            intent.putExtra("DISH_STATUS", dish.isAvailable)

            val thumbnail = dish.dishImages.find { it.isThumbnail }?.imageUrl ?: ""
            intent.putExtra("DISH_IMAGE", thumbnail)

            startActivity(intent)
        }
        binding.recyclerViewDishes.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDishes.adapter = dishAdapter
    }

    private fun setupSearch() {
        binding.edtSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            val filteredList = fullDishList.filter {
                it.name.contains(query, ignoreCase = true)
            }
            dishAdapter.updateData(filteredList)
        }
    }

    private fun fetchDishesFromApi() {
        lifecycleScope.launch {
            try {
                val response = ApiService.create().getDishes()
                if (response.isSuccessful && response.body() != null) {
                    fullDishList = response.body()!!
                    dishAdapter.updateData(fullDishList)
                } else {
                    Toast.makeText(this@MainActivity, "Không thể tải dữ liệu từ MockAPI", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@MainActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}