package com.example.Hacker_News.activity

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.Hacker_News.Database.DatabaseHandler
import com.example.Hacker_News.R
import com.example.Hacker_News.model.Top
import com.example.Hacker_News.adapter.TopStoryAdapter
import com.example.Hacker_News.appconfig.ApiClient
import com.example.Hacker_News.model.InsertTopNews
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class HomeActivity : AppCompatActivity() {

    var dataList = ArrayList<Top>()
    var TopNews = ArrayList<InsertTopNews>()

    lateinit var Listint : List<Int>
    var filteredDataList= ArrayList<InsertTopNews>()
    lateinit var StoryList: RecyclerView
    lateinit var searchview: SearchView

    val databaseHandler: DatabaseHandler = DatabaseHandler(this)

    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var lytTopStories: LinearLayout
    lateinit var lytBestStories: LinearLayout
    lateinit var lytNewStories: LinearLayout
    lateinit var lytJobStories: LinearLayout
    lateinit var lytAskStories: LinearLayout

    lateinit var txttop: TextView
    lateinit var txtbest: TextView
    lateinit var txtnew: TextView
    lateinit var txtjob: TextView
    lateinit var txtask: TextView
    lateinit var txtno_news: TextView

    lateinit var lytNoData: LinearLayout
    lateinit var strNewstype: String
    lateinit var container: ShimmerFrameLayout

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize in xml resource
        StoryList = findViewById(R.id.StoryList)
        searchview = findViewById(R.id.searchview)
        lytTopStories = findViewById(R.id.lytTopStories)
        lytBestStories = findViewById(R.id.lytBestStories)
        lytNewStories = findViewById(R.id.lytNewStories)
        lytJobStories = findViewById(R.id.lytJobStories)
        lytAskStories = findViewById(R.id.lytAskStories)
        txttop = findViewById(R.id.txttop)
        txtbest = findViewById(R.id.txtbest)
        txtnew = findViewById(R.id.txtnew)
        txtjob = findViewById(R.id.txtjob)
        txtask = findViewById(R.id.txtask)
        txtno_news = findViewById(R.id.txtno_news)
        lytNoData = findViewById(R.id.lytNoData)

       /* var loading = true
        var pastVisiblesItems: Int
        var visibleItemCount: Int
        var totalItemCount: Int*/

        container = findViewById(R.id.shimmer_view_container)
        val builder = Shimmer.AlphaHighlightBuilder()
        builder.setDirection(com.facebook.shimmer.Shimmer.Direction.TOP_TO_BOTTOM)
        container.setShimmer(builder.build())

        strNewstype = "Top"
        lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
        lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
        lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
        lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
        lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
        txttop.setTextColor(resources.getColor(R.color.white))
        txtbest.setTextColor(resources.getColor(R.color.black))
        txtnew.setTextColor(resources.getColor(R.color.black))
        txtjob.setTextColor(resources.getColor(R.color.black))
        txtask.setTextColor(resources.getColor(R.color.black))

        lytTopStories.setOnClickListener(){
            strNewstype = "Top"
            TopNews.clear()
            StoryList.adapter?.notifyDataSetChanged()
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
            }
            lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
            lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            txttop.setTextColor(resources.getColor(R.color.white))
            txtbest.setTextColor(resources.getColor(R.color.black))
            txtnew.setTextColor(resources.getColor(R.color.black))
            txtjob.setTextColor(resources.getColor(R.color.black))
            txtask.setTextColor(resources.getColor(R.color.black))
        }

        lytBestStories.setOnClickListener(){
            strNewstype = "Best"
            TopNews.clear()
            StoryList.adapter?.notifyDataSetChanged()
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
            }
            lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
            lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            txttop.setTextColor(resources.getColor(R.color.black))
            txtbest.setTextColor(resources.getColor(R.color.white))
            txtnew.setTextColor(resources.getColor(R.color.black))
            txtjob.setTextColor(resources.getColor(R.color.black))
            txtask.setTextColor(resources.getColor(R.color.black))
        }

        lytNewStories.setOnClickListener(){
            strNewstype = "New"
            TopNews.clear()
            StoryList.adapter?.notifyDataSetChanged()
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
            }
            lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
            lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            txttop.setTextColor(resources.getColor(R.color.black))
            txtbest.setTextColor(resources.getColor(R.color.black))
            txtnew.setTextColor(resources.getColor(R.color.white))
            txtjob.setTextColor(resources.getColor(R.color.black))
            txtask.setTextColor(resources.getColor(R.color.black))
        }

        lytJobStories.setOnClickListener(){
            strNewstype = "Job"
            TopNews.clear()
            StoryList.adapter?.notifyDataSetChanged()
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
            }

            lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
            lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            txttop.setTextColor(resources.getColor(R.color.black))
            txtbest.setTextColor(resources.getColor(R.color.black))
            txtnew.setTextColor(resources.getColor(R.color.black))
            txtjob.setTextColor(resources.getColor(R.color.white))
            txtask.setTextColor(resources.getColor(R.color.black))
        }

        lytAskStories.setOnClickListener(){
            strNewstype = "Ask"
            TopNews.clear()
            StoryList.adapter?.notifyDataSetChanged()
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
            }
            lytTopStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytBestStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytNewStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytJobStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border_unselect))
            lytAskStories.setBackgroundDrawable(resources.getDrawable(R.drawable.ic_lyt_border))
            txttop.setTextColor(resources.getColor(R.color.black))
            txtbest.setTextColor(resources.getColor(R.color.black))
            txtnew.setTextColor(resources.getColor(R.color.black))
            txtjob.setTextColor(resources.getColor(R.color.black))
            txtask.setTextColor(resources.getColor(R.color.white))
        }

        //  swipeRefreshLayout reload data in recyclerview
        swipeRefreshLayout = findViewById(R.id.swipe)
        swipeRefreshLayout.setOnRefreshListener {
            if (checkForInternet(this)) {
                getTopStory()
            } else {
                getNews()
                Toast.makeText(this, "Not connected internet", Toast.LENGTH_SHORT).show()
            }
        }

        val mLayoutManager: LinearLayoutManager
        mLayoutManager = LinearLayoutManager(this)
        StoryList.setLayoutManager(mLayoutManager)
        StoryList.adapter= TopStoryAdapter(TopNews, this)

        if (checkForInternet(this)) {
            getTopStory()
        } else {
            getNews()
            Toast.makeText(this, "Not connected internet", Toast.LENGTH_SHORT).show()
        }

        //Search view option implement
        searchview.setOnClickListener(){
            searchview.onActionViewExpanded();
        }

        searchview.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(text: String?): Boolean {
                if (text != null) {
                    if (!text.equals("")) {
                        filteredDataList = filter(TopNews, text) as ArrayList<InsertTopNews>
                        StoryList.adapter = TopStoryAdapter(filteredDataList, applicationContext)
                        StoryList.adapter?.notifyDataSetChanged()
                    } else {
                        StoryList.adapter = TopStoryAdapter(TopNews, applicationContext)
                        StoryList.adapter?.notifyDataSetChanged()
                    }
                }
                return false
            }

            override fun onQueryTextChange(text: String?): Boolean {
                if (text != null) {
                    if (!text.equals("")) {
                        filteredDataList = filter(TopNews, text) as ArrayList<InsertTopNews>
                        StoryList.adapter = TopStoryAdapter(filteredDataList, applicationContext)
                        StoryList.adapter?.notifyDataSetChanged()
                    } else {
                        StoryList.adapter = TopStoryAdapter(TopNews, applicationContext)
                        StoryList.adapter?.notifyDataSetChanged()
                    }
                }
                return false
            }
        })

       /* StoryList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (dy > 0) { //check for scroll down
                    visibleItemCount = mLayoutManager.getChildCount()
                    totalItemCount = mLayoutManager.getItemCount()
                    pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition()
                    if (loading) {

                        if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                            if(totalItemCount == visibleItemCount+pastVisiblesItems) {
                                count += 10
                                totalItemCount +=10
                                if (totalItemCount == count) {
                                    for (i in visibleItemCount + pastVisiblesItems until count) {
                                        try {
                                            TalkToServer(
                                                this@HomeActivity,
                                                Listint[i].toString()
                                            ).execute()
                                        } catch (ex: Exception) {
                                            ex.printStackTrace()
                                        }
                                    }
                                }
                            }
                            loading = false

                            loading = true

                        }
                    }
                }
            }
        })*/
    }

    private fun filter(dataList: List<InsertTopNews>, newText: String): List<InsertTopNews?>? {
        var newText = newText
        newText = newText.toLowerCase()
        var text: String
        filteredDataList = ArrayList()
        for (dataFromDataList in dataList) {
            text = dataFromDataList.title.toLowerCase()
            if (text.contains(newText)) {
                filteredDataList.add(dataFromDataList)
            }
        }
        return filteredDataList
    }

    private fun getNews() {
        lytNoData.visibility = GONE
        StoryList.visibility = VISIBLE
        swipeRefreshLayout.isRefreshing = false
        TopNews.clear()
        val emp: List<InsertTopNews> = databaseHandler.viewNews(strNewstype)
        for(e in emp){
            TopNews.add(InsertTopNews(e.by, e.time.toString(), e.title, e.url, strNewstype))
        }

        if(TopNews.size == 0){
            lytNoData.visibility = VISIBLE
            StoryList.visibility = GONE
            txtno_news.visibility = VISIBLE
            container.visibility = GONE
        }
        StoryList.adapter?.notifyDataSetChanged()
    }

    var count: Int = 0
    var countget :Int =0

    private fun getTopStory() {
        val call: Call<List<Int>>
        lytNoData.visibility = VISIBLE
        StoryList.visibility = GONE
        txtno_news.visibility = GONE
        container.visibility = VISIBLE
        if(strNewstype.equals("Top")) {
            call = ApiClient.getClient.getTopStories()
        }else if(strNewstype.equals("Best")){
            call = ApiClient.getClient.getbestStories()
        }else if(strNewstype.equals("New")){
            call = ApiClient.getClient.getnewStories()
        }else if(strNewstype.equals("Job")){
            call = ApiClient.getClient.getjobStories()
        }else{
            call = ApiClient.getClient.getshowStories()
        }
        call.enqueue(object : Callback<List<Int>> {

            override fun onResponse(call: Call<List<Int>>?, response: Response<List<Int>>?) {
                try {
                    val topStories = response!!.body()
                    if (topStories != null) {
                        dataList.clear()
                        StoryList.adapter?.notifyDataSetChanged()
                        Listint = topStories
                        //count = Listint.size
                        if(Listint.size >= 100) {
                            count = 100
                        }else{
                            count = Listint.size
                        }
                        countget = 0
                        if (count != 0) {
                            val databaseHandler: DatabaseHandler = DatabaseHandler(applicationContext)
                            val status = databaseHandler.deleteNews(strNewstype)
                            if (status > -1) {
                                // Toast.makeText(applicationContext, "all data delete", Toast.LENGTH_LONG).show()
                            }
                            for (i in 0 until count) {
                                try {
                                    TalkToServer(this@HomeActivity, Listint[i].toString()).execute()
                                } catch (ex: Exception) {
                                    ex.printStackTrace()
                                }
                            }
                        } else {
                            swipeRefreshLayout.isRefreshing = false
                            getNews()
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }

            override fun onFailure(call: Call<List<Int>>?, t: Throwable?) {

            }
        })

    }

    class TalkToServer(context: HomeActivity, StrID: String) : AsyncTask<Void?, Void?, Void?>() {
        val context = context
        var srtid: Int = Integer.valueOf(StrID)
        override fun doInBackground(vararg params: Void?): Void? {
            try {
                val call: Call<Top> =
                    ApiClient.getClient.getNewsDetails(srtid)
                call.enqueue(object : Callback<Top> {
                    override fun onResponse(
                        call: Call<Top>?,
                        response: Response<Top>?
                    ) {
                        try {
                            context.countget = context.countget + 1
                            val dataList1: Top? = response?.body()
                            if (dataList1 != null) {
                                val title = dataList1.title.toString()
                                val time = dataList1.time.toString()
                                val by = dataList1.by.toString()
                                val url = dataList1.url.toString()
                                val type = dataList1.type.toString()
                                if (title != "" && time != "" && by != "" && url != "" && type != "") {
                                    val status = context.databaseHandler.addNews(
                                        InsertTopNews(by, time, title, url, context.strNewstype)
                                    )
                                    if (status > -1) {
                                        // Toast.makeText(context, "record save", Toast.LENGTH_LONG).show()
                                    }
                                }
                            }

                            if (context.count == context.countget) {
                                context.swipeRefreshLayout.isRefreshing = false
                                context.getNews()
                            }
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                    override fun onFailure(call: Call<Top>?, t: Throwable?) {
                        context.countget = context.countget + 1
                    }
                })
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            return null
        }
    }

    private fun checkForInternet(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION") val networkInfo =
                connectivityManager.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

}

