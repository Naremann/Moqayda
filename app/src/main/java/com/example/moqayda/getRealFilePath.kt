package com.example.moqayda

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.res.AssetFileDescriptor
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.os.StatFs
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.moqayda.ui.addProduct.AddProductViewModel
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.default
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.withContext
import java.io.*
import java.util.*


internal fun getFilePathFromUri(
    context: Context,
    uri: Uri,
    addProductViewModel: AddProductViewModel,
    selectedImageName: String
): String =
    if (uri.path?.contains("file://") == true) {
        uri.path!!
    } else {
        getFileFromContentUri(context, uri,addProductViewModel,selectedImageName).path
    }

@SuppressLint("SuspiciousIndentation")
private fun getFileFromContentUri(
    context: Context,
    contentUri: Uri,
    addProductViewModel: AddProductViewModel,
    selectedImageName: String
): File {

    val tempFile = File(context.cacheDir, selectedImageName)
    tempFile.createNewFile()
    var oStream: FileOutputStream? = null
    var inputStream: InputStream? = null

    try {
        oStream = FileOutputStream(tempFile)
        inputStream = context.contentResolver.openInputStream(contentUri)

        inputStream?.let { copy(inputStream, oStream) }

        oStream.flush()
    } catch (e: Exception) {
        e.printStackTrace()
    } finally {
        // Close streams
        inputStream?.close()
        oStream?.close()
    }
    addProductViewModel.setFileName(selectedImageName)
    return tempFile
}

private fun getFileExtension(context: Context, uri: Uri): String? =
    if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
        MimeTypeMap.getSingleton().getExtensionFromMimeType(context.contentResolver.getType(uri))
    } else {
        uri.path?.let { MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(it)).toString()) }
    }

@Throws(IOException::class)
private fun copy(source: InputStream, target: OutputStream) {
    val buffer = ByteArray(8192)
    var length: Int
    while (source.read(buffer).also { length = it } > 0) {
        target.write(buffer, 0, length)
    }

//  length = source.read(buffer)
//    while (length != -1) {
//        target.write(buffer, 0, length)
//        length = source.read(buffer)
//    }

}

fun getAvailableInternalMemorySize(): Long {
    val path = Environment.getDataDirectory()
    val stat = StatFs(path.path)
    val blockSize: Long = stat.blockSizeLong
    val availableBlocks: Long = stat.availableBlocksLong
    return availableBlocks * blockSize
}

@SuppressLint("Recycle")
fun getFileSize(context: Context,uri:Uri): Long {
    val fileDescriptor: AssetFileDescriptor =
        context.contentResolver.openAssetFileDescriptor(uri, "r")!!
    return fileDescriptor.length
}

suspend fun compressImageFile(file: File?,context: Context) : File?  = withContext(GlobalScope.coroutineContext)  {
    return@withContext try {
        Compressor.compress(context,file!!, Dispatchers.IO) { default() }
    } catch (e: IOException) {
        e.printStackTrace()
        file
    }
}

suspend fun convertBitmapToFile(imageUri:Uri,context: Context): File? {
    val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
    return try {
        val f = File(context.cacheDir, "image")
        f.createNewFile()
        val bos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 60 /*ignored for PNG*/, bos)
        val bitmapdata = bos.toByteArray()
        val fos = FileOutputStream(f)
        fos.write(bitmapdata)
        fos.flush()
        fos.close()
        compressImageFile(f,context)
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}