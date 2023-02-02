package com.kangmin.mysololife.contentsList

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.kangmin.mysololife.R
import com.kangmin.mysololife.utils.FBAuth
import com.kangmin.mysololife.utils.FBRef

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference

    val bookmarkIdList = mutableListOf<String>()

    lateinit var rvAdapter: ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)


        val database = Firebase.database

        val category = intent.getStringExtra("category")

        if (category == "category1") {

            myRef = database.getReference("contents")

        } else if (category == "category2") {

            myRef = database.getReference("contents2")

        }


        // Database에 있는 contents 안의 내용을 받아오는 부분
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                for (dataModel in dataSnapshot.children) {
                    Log.d("ContentListActivity", dataModel.toString())
                    Log.d("ContentListActivity", dataModel.key.toString())
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString())
                }
                rvAdapter.notifyDataSetChanged()
                Log.d("ContentListActivity", items.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)


        val rv: RecyclerView = findViewById(R.id.rv)


        // ContentListActivity 에서 activity.xml 로 전달
        rv.adapter = rvAdapter

        rv.layoutManager = GridLayoutManager(this, 2)

        getBookmarkData()
    }


    private fun getBookmarkData() {
        // Database에 있는 contents 안의 내용을 받아오는 부분
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIdList.clear()

                for (dataModel in dataSnapshot.children) {
                    bookmarkIdList.add(dataModel.key.toString())
                }
                Log.d("Bookmark : ", bookmarkIdList.toString())
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Getting Post failed, log a message
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }


//         꿀팁 목록 데이터 삽입 (push 앞 myRef 번호와 title 번호만 바꿔서 사용)
//        val myRef = database.getReference("contents")
//        myRef.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png","title1","https://philosopher-chan.tistory.com/1235")
//        )
//        myRef.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png","title2","https://philosopher-chan.tistory.com/1236")
//        )
//        myRef.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png","title3","https://philosopher-chan.tistory.com/1237")
//        )
//        myRef.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png","title4","https://philosopher-chan.tistory.com/1238")
//        )
//
//        val myRef2 = database.getReference("contents2")
//        myRef2.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png","title5","https://philosopher-chan.tistory.com/1239")
//        )
//        myRef2.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F123LP%2Fbtq65qy4hAd%2F6dgpC13wgrdsnHigepoVT1%2Fimg.png","title6","https://philosopher-chan.tistory.com/1240")
//        )
//        myRef2.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png","title7","https://philosopher-chan.tistory.com/1241")
//        )
//        myRef2.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png","title8","https://philosopher-chan.tistory.com/1242")
//        )
//
//        val myRef3 = database.getReference("contents3")
//        myRef3.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png","title9","https://philosopher-chan.tistory.com/1243")
//        )
//        myRef3.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FNNrYR%2Fbtq64wsW5VN%2FqIaAsfmFtcvh4Bketug9m0%2Fimg.png","title10","https://philosopher-chan.tistory.com/1244")
//        )
//        myRef3.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FK917N%2Fbtq64SP5gxj%2FNzsfNAykamW7qv1hdusp1K%2Fimg.png","title11","https://philosopher-chan.tistory.com/1245")
//        )
//        myRef3.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEO4sy%2Fbtq69SgK8L3%2FttCUxYHx9aPNebNwkPcI21%2Fimg.png","title12","https://philosopher-chan.tistory.com/1246")
//        )
//        val myRef4 = database.getReference("contents4")
//        myRef4.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbdIKDG%2Fbtq64M96JFa%2FKcJiYgKuwKuP3fIyviXm90%2Fimg.png","title13","https://philosopher-chan.tistory.com/1247")
//        )
//        myRef4.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png","title14","https://philosopher-chan.tistory.com/1248")
//        )
//        myRef4.push().setValue(
//            ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png","title15","https://philosopher-chan.tistory.com/1249")
//        )
//
//
//
//        // 데이터 모델 넘기기 (imageUrl, title)
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FblYPPY%2Fbtq66v0S4wu%2FRmuhpkXUO4FOcrlOmVG4G1%2Fimg.png","title1","https://philosopher-chan.tistory.com/1235"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FznKK4%2Fbtq665AUWem%2FRUawPn5Wwb4cQ8BetEwN40%2Fimg.png","title2","https://philosopher-chan.tistory.com/1236"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fbtig9C%2Fbtq65UGxyWI%2FPRBIGUKJ4rjMkI7KTGrxtK%2Fimg.png","title3","https://philosopher-chan.tistory.com/1237"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FcOYyBM%2Fbtq67Or43WW%2F17lZ3tKajnNwGPSCLtfnE1%2Fimg.png","title4","https://philosopher-chan.tistory.com/1238"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fekn5wI%2Fbtq66UlN4bC%2F8NEzlyot7HT4PcjbdYAINk%2Fimg.png","title5","https://philosopher-chan.tistory.com/1239"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F123LP%2Fbtq65qy4hAd%2F6dgpC13wgrdsnHigepoVT1%2Fimg.png","title6","https://philosopher-chan.tistory.com/1240"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2Fl2KC3%2Fbtq64lkUJIN%2FeSwUPyQOddzcj6OAkPKZuk%2Fimg.png","title7","https://philosopher-chan.tistory.com/1241"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FmBh5u%2Fbtq651yYxop%2FX3idRXeJ0VQEoT1d6Hln30%2Fimg.png","title8","https://philosopher-chan.tistory.com/1242"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FlOnja%2Fbtq69Tmp7X4%2FoUvdIEteFbq4Z0ZtgCd4p0%2Fimg.png","title9","https://philosopher-chan.tistory.com/1243"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FNNrYR%2Fbtq64wsW5VN%2FqIaAsfmFtcvh4Bketug9m0%2Fimg.png","title10","https://philosopher-chan.tistory.com/1244"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FK917N%2Fbtq64SP5gxj%2FNzsfNAykamW7qv1hdusp1K%2Fimg.png","title11","https://philosopher-chan.tistory.com/1245"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FeEO4sy%2Fbtq69SgK8L3%2FttCUxYHx9aPNebNwkPcI21%2Fimg.png","title12","https://philosopher-chan.tistory.com/1246"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FbdIKDG%2Fbtq64M96JFa%2FKcJiYgKuwKuP3fIyviXm90%2Fimg.png","title13","https://philosopher-chan.tistory.com/1247"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png","title14","https://philosopher-chan.tistory.com/1248"))
//        items.add(ContentModel("https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png","title15","https://philosopher-chan.tistory.com/1249"))

}