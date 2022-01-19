package com.itmedicus.randomuser.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.itmedicus.randomuser.data.adapter.BreakfastAdapter
import com.itmedicus.randomuser.data.adapter.HistoryAdapter
import com.itmedicus.randomuser.data.adapter.ItemClickListener
import com.itmedicus.randomuser.databinding.ActivityListBinding
import com.itmedicus.randomuser.databinding.ActivityMealPlanBinding
import com.itmedicus.randomuser.model.Breakfast

class MealPlanActivity : AppCompatActivity(), ItemClickListener {

    private lateinit var binding: ActivityMealPlanBinding
    private val adapter by lazy { BreakfastAdapter(this) }
    val list = ArrayList<Breakfast>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMealPlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setProgressTo(20)
        setSecondaryProgressTo(30)
        initBreakfastRecyclerView()
        initLunchRecyclerView()
        initSnackRecyclerView()
        initDinnerRecyclerView()
        makeList()
    }

    private fun initBreakfastRecyclerView() {
        val mRecyclerView = binding.recyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
    }


    private fun initLunchRecyclerView() {
        val mRecyclerView = binding.lunchRecyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
    }


    private fun initSnackRecyclerView() {
        val mRecyclerView = binding.snackRecyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
    }

    private fun initDinnerRecyclerView() {
        val mRecyclerView = binding.dinnerRecyclerview
        mRecyclerView.adapter = adapter
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false)
    }



    private fun setProgressTo(progress: Int){
        binding.progressTv.text = "$progress%"
        binding.circularDeterminativePb.progress = progress
    }

    private fun setSecondaryProgressTo(progress: Int){
        binding.circularDeterminativePb.secondaryProgress = progress
    }

    private fun makeList(){

        val array = ArrayList<String>()
        array.add("https://cdn.cdnparenting.com/articles/2018/12/12122643/1211153146-H.jpg")
        array.add("https://thumbs.dreamstime.com/z/full-scottish-breakfast-containing-tomato-black-pudding-haggis-sausages-bacon-mushrooms-fried-eggs-toast-197884152.jpg")
        array.add("https://1.bp.blogspot.com/-ErlXpzR2xxI/Xxr-_VfXqGI/AAAAAAAACrc/BWK4lxTd5LIAlpr6qXu00KQiOzXpak7YACLcBGAsYHQ/s1600/Paratha%2528Cookingfever101%2529.JPG")
        array.add("https://i.insider.com/57964ffc4321f10d038baa99?width=1000&format=jpeg&auto=webp")
        array.add("https://static.wixstatic.com/media/884cac_cb2a193a81274e9ea5658eee914d85e3.jpg/v1/fill/w_960,h_720,al_c,q_90/884cac_cb2a193a81274e9ea5658eee914d85e3.jpg")
        array.add("https://247wallst.com/wp-content/uploads/2019/09/imageforentry24-pz5.jpg")
        array.add("https://img.sndimg.com/food/image/upload/q_92,fl_progressive,w_1200,c_scale/v1/img/recipes/34/67/61/picyw8hsm.jpg")
        array.add("https://www.brit.co/media-library/image.jpg?id=21377089")
        array.add("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRdam51GePS5_Te3NgHoxD5rHfwR4mE3k4s-Q&usqp=CAU")
        array.add("https://assets.marthastewart.com/styles/wmax-750/d20/coconut-bars-D101473/coconut-bars-D101473_horiz.jpg?itok=Dgta8t9s")


        val productData1 = Breakfast("Paratha",10,(array[0]),40,50,20)
        val productData2 = Breakfast("Egg toast",20,(array[1]),30,20,5)
        val productData3 = Breakfast("Roti",15,(array[2]),35,25,10)
        val productData4 = Breakfast("Milkshake",13,(array[3]),45,25,12)
        val productData5 = Breakfast("Cheese dosa",8,(array[4]),22,10,8)
        val productData6 = Breakfast("Neer dosa",18,(array[5]),35,20,15)
        val productData7 = Breakfast("Bread Pakura",14,(array[6]),55,10,5)
        val productData8 = Breakfast("Idli Upma",10,(array[7]),10,20,30)
        val productData9 = Breakfast("Dalia",20,(array[8]),55,40,2)
        val productData10 = Breakfast("Coconut Oats",30,(array[9]),70,27,18)


        list.add(productData1)
        list.add(productData2)
        list.add(productData3)
        list.add(productData4)
        list.add(productData5)
        list.add(productData6)
        list.add(productData7)
        list.add(productData8)
        list.add(productData9)
        list.add(productData10)
        adapter.setData(list)
    }

    override fun onItemSend(position: Int) {
        val intent = Intent(this,NutritionActivity::class.java)
        val item = list[position]
        intent.putExtra("title",item.title)
        intent.putExtra("image",item.image)
        intent.putExtra("kcal",item.kcal)
        intent.putExtra("protein",item.protein)
        intent.putExtra("fat",item.fat)
        intent.putExtra("carbs",item.carbs)
        Log.d("this","$item.kcal: $item.protein")
        startActivity(intent)
    }


}