package com.example.stumart.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

object ImageUtils {
    /**
     * Chuyển đổi Content URI tạm thời từ bộ sưu tập thành File vĩnh viễn trong bộ nhớ nội bộ của App.
     * Đường dẫn file:// này sẽ KHÔNG BAO GIỜ bị hết hạn hay mất quyền truy cập khi đăng xuất / khởi động lại app.
     */
    fun processImageUri(context: Context, uri: Uri): String? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return uri.toString()
            val bitmap = BitmapFactory.decodeStream(inputStream) ?: return uri.toString()
            inputStream.close()

            // Nén ảnh xuống kích thước vừa phải (tối đa 800px) để tối ưu dung lượng
            val maxDimension = 800
            val scale = Math.min(
                maxDimension.toFloat() / bitmap.width,
                maxDimension.toFloat() / bitmap.height
            )
            val resizedBitmap = if (scale < 1.0f) {
                Bitmap.createScaledBitmap(
                    bitmap,
                    (bitmap.width * scale).toInt(),
                    (bitmap.height * scale).toInt(),
                    true
                )
            } else {
                bitmap
            }

            // Lưu vào thư mục tệp nội bộ của ứng dụng (context.filesDir)
            val fileName = "stumart_img_${System.currentTimeMillis()}.jpg"
            val file = File(context.filesDir, fileName)
            FileOutputStream(file).use { out ->
                resizedBitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
            }

            // Trả về URI kiểu file:// vĩnh viễn
            Uri.fromFile(file).toString()
        } catch (e: Exception) {
            e.printStackTrace()
            uri.toString()
        }
    }
}
