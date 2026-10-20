package linhpham.bit230239_foodmanager

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import linhpham.bit230239_foodmanager.api.ApiService
import linhpham.bit230239_foodmanager.databinding.ActivityDetailDishBinding

class DetailDishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailDishBinding
    private var dishId: String = ""
    private var dishName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailDishBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dishId = intent.getStringExtra("DISH_ID") ?: ""
        dishName = intent.getStringExtra("DISH_NAME") ?: ""
        val price = intent.getDoubleExtra("DISH_PRICE", 0.0)
        val time = intent.getIntExtra("DISH_TIME", 0)
        val desc = intent.getStringExtra("DISH_DESC") ?: ""
        val isAvailable = intent.getBooleanExtra("DISH_STATUS", true)
        val imageUrl = intent.getStringExtra("DISH_IMAGE") ?: ""

        binding.tvDetailName.text = dishName
        binding.tvDetailPrice.text = "Giá: $price VNĐ"

        // Đã sửa lại nhánh if-else đầy đủ để tránh lỗi biên dịch
        binding.tvDetailTime.text = if (time <= 15) {
            "Quick / Nhanh ($time phút)"
        } else {
            "Regular / Thông thường ($time phút)"
        }

        binding.tvDetailStatus.text = if (isAvailable) {
            "Trạng thái: Đang bán"
        } else {
            "Trạng thái: Ngừng bán"
        }

        binding.tvDetailDesc.text = "Mô tả: $desc"

        Glide.with(this).load(imageUrl).into(binding.imgDetail)

        // Xử lý nút Xóa với hộp thoại xác nhận[cite: 5]
        binding.btnDelete.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa món '$dishName' không?")
                .setPositiveButton("Xóa") { _, _ -> deleteDish() }
                .setNegativeButton("Hủy", null)
                .show()
        }

        // Xử lý nút Sửa
        binding.btnEdit.setOnClickListener {
            val intent = Intent(this, AddEditDishActivity::class.java)
            intent.putExtra("IS_EDIT", true)
            intent.putExtra("DISH_ID", dishId)
            startActivity(intent)
        }
    }

    private fun deleteDish() {
        lifecycleScope.launch {
            try {
                val response = ApiService.create().deleteDish(dishId)
                if (response.isSuccessful) {
                    Toast.makeText(this@DetailDishActivity, "Đã xóa thành công!", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@DetailDishActivity, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DetailDishActivity, "Lỗi kết nối: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}