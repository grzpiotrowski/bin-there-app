package ie.setu.bin_there_app.firebase

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import timber.log.Timber
import java.io.ByteArrayOutputStream
import com.squareup.picasso.Target

object FirebaseImageManager {

    var storage = FirebaseStorage.getInstance().reference
    var imageUri = MutableLiveData<Uri>()

    fun checkStorageForExistingPoiPic(poiid: String) {
        val imageRef = storage.child("photos").child("${poiid}.jpg")
        val defaultImageRef = storage.child("homer.jpg")

        imageRef.metadata.addOnSuccessListener { //File Exists
            imageRef.downloadUrl.addOnCompleteListener { task ->
                imageUri.value = task.result!!
            }
            //File Doesn't Exist
        }.addOnFailureListener {
            imageUri.value = Uri.EMPTY
        }
    }

    fun uploadImageToFirebase(poiid: String, imageUri: Uri) {
        val imageRef = storage.child("photos").child("${poiid}.jpg")

        Picasso.get().load(imageUri).into(object : Target {
            override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
                val baos = ByteArrayOutputStream()
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()

                val uploadTask = imageRef.putBytes(data)
                uploadTask.addOnSuccessListener {
                    it.metadata!!.reference!!.downloadUrl.addOnCompleteListener { task ->
                        FirebaseDBManager.updateImageRef(poiid, task.result.toString())
                    }
                }
            }

            override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
                Timber.e(e, "Image upload failed.")
            }

            override fun onPrepareLoad(placeHolderDrawable: Drawable?) {}
        })
    }
}