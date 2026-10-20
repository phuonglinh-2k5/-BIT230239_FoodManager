package linhpham.bit230239_foodmanager

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import linhpham.bit230239_foodmanager.api.ApiService
import linhpham.bit230239_foodmanager.databinding.ActivityAddEditDishBinding
import linhpham.bit230239_foodmanager.model.Dish
import linhpham.bit230239_foodmanager.model.DishImage

class AddEditDishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditDishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            validateAndSaveDish()
        }
    }

    private fun validateAndSaveDish() {
        val name = binding.edtName.text.toString().trim()
        val priceStr = binding.edtPrice.text.toString().trim()
        val timeStr = binding.edtPreparationTime.text.toString().trim()
        val description = binding.edtDescription.text.toString().trim()
        val imageUrl = binding.edtImageUrl.text.toString().trim()
        val isAvailable = binding.cbIsAvailable.isChecked

        // Ràng buộc kiểm tra lỗi (Validation) theo đề bài
        if (name.isEmpty()) {
            binding.edtName.error = "Tên món không được để trống"
            return
        }
        val price = priceStr.toDoubleOrNull()
        if (price == null || price <= 0) {
            binding.edtPrice.error = "Giá phải lớn hơn 0"
            return
        }
        val prepTime = timeStr.toIntOrNull()
        if (prepTime == null || prepTime <= 0) {
            binding.edtPreparationTime.error = "Thời gian chế biến phải lớn hơn 0"
            return
        }

        val images = listOf(DishImage(imageUrl = imageUrl.ifEmpty { "https://via.placeholder.com/150" }, isThumbnail = true))

        // Tạo đối tượng Dish gửi lên MockAPI
        val newDish = Dish(
            id = null,
            name = name,
            price = price,
            preparationTime = prepTime,
            isAvailable = isAvailable,
            description = description,
            dishCategoryId = 1, // Mặc định loại 1 hoặc lấy từ Spinner
            dishImages = images
        )

        lifecycleScope.launch {
            try {
                val response = ApiService.create().createDish(newDish)
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEditDishActivity, "Thêm món thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@AddEditDishActivity, "Lỗi khi lưu dữ liệu", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddEditDishActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}