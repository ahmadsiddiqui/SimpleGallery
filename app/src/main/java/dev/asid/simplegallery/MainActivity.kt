package dev.asid.simplegallery




import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

import android.Manifest


class MainActivity : AppCompatActivity() {


    private var uri: Uri? = null
    private var perms:String = Manifest.permission.READ_MEDIA_IMAGES


    private var getContent =
        registerForActivityResult(ActivityResultContracts.GetContent()){

            uri = it
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (ContextCompat.checkSelfPermission(this, perms) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(perms),100 )
        }
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission Granted!!!", Toast.LENGTH_SHORT).show()

            setOnClickListeners()
        } else {
            Toast.makeText(this, "Permission Denied!!!", Toast.LENGTH_SHORT).show()
        }
    }

    fun getAllImageViews(view: View): ArrayList<ImageView> {
        val imageViews = ArrayList<ImageView>()
        if (view is ImageView) {
            imageViews.add(view)
        } else if (view is ViewGroup) {
            for (i in 0 until view.childCount) {
                imageViews.addAll(getAllImageViews(view.getChildAt(i)))
            }
        }
        return imageViews
    }

    fun setOnClickListeners(){
        val allImageViews = getAllImageViews(findViewById(R.id.main))
        for (imageView in allImageViews) {
            imageView.setOnClickListener {
                getContent.launch("image/*")
                imageView.setImageURI(uri)

            }
        }
    }

}