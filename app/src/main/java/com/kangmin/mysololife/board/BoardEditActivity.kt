package com.kangmin.mysololife.board

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.kangmin.mysololife.R
import com.kangmin.mysololife.databinding.ActivityBoardEditBinding
import com.kangmin.mysololife.utils.FBAuth
import com.kangmin.mysololife.utils.FBRef

class BoardEditActivity : AppCompatActivity() {

    private lateinit var key : String

    private lateinit var binding : ActivityBoardEditBinding

    private val TAG = BoardEditActivity::class.java.simpleName

    private lateinit var writeUid : String

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_board_edit)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_board_edit)

        key = intent.getStringExtra("key").toString()

        getBoardData(key)
        getImageData(key)

        binding.editBtn.setOnClickListener {
            editBoardData(key)
        }


    }

    private fun editBoardData(key : String){

        FBRef.boardRef
            .child(key)
            .setValue(
                BoardModel(
                    binding.titleArea.text.toString(),
                    binding.contentArea.text.toString(),
                    writeUid,
                    FBAuth.getTime()
                )
            )

        Toast.makeText(this,"수정완료",Toast.LENGTH_LONG).show()

        finish()


    }

    private fun getImageData(key : String){

        // Reference to an image file in Cloud Storage
        val storageReference = Firebase.storage.reference.child(key + ".png")

        // ImageView in your Activity
        val imageViewFromFB = binding.imageArea

        storageReference.downloadUrl.addOnCompleteListener(OnCompleteListener { task ->
            if(task.isSuccessful){

                Glide.with(this)
                    .load(task.result)
                    .into(imageViewFromFB)

            } else{

            }
        })
    }

    private fun getBoardData(key : String){
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                try{
                    val dataModel = dataSnapshot.getValue(BoardModel::class.java)

                    binding.titleArea.setText(dataModel?.title)
                    binding.contentArea.setText(dataModel?.content)
                    writeUid = dataModel!!.uid

                }catch(e : Exception){
                    Log.d(TAG, "삭제완료")

                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w(TAG, "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.boardRef.child(key).addValueEventListener(postListener)
    }

}