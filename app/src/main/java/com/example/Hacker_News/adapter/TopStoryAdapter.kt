package com.example.Hacker_News.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.Hacker_News.R
import com.example.Hacker_News.activity.WebviewActivity
import com.example.Hacker_News.model.InsertTopNews
import java.text.SimpleDateFormat
import java.util.*

class TopStoryAdapter(
        private var dataList: List<InsertTopNews>,
        private val context: Context
) : RecyclerView.Adapter<TopStoryAdapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                        LayoutInflater.from(context).inflate(
                                R.layout.lyt_story,
                                parent,
                                false
                        )
                )
        }

        override fun getItemCount(): Int {
                return dataList.size
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                val dataModel=dataList.get(position)

                holder.titleTextView.text=dataModel.title
                holder.by.text=dataModel.by
                try {
                        //unix timestamp to date format
                        val dv: Long = java.lang.Long.valueOf(dataModel.time) * 1000
                        val df: Date = Date(dv)
                        val vv: String = SimpleDateFormat("hh:mma  dd, MMM yyyy").format(df)
                        holder.time.text = vv
                }catch (ex: Exception){
                        ex.printStackTrace()
                }
                holder.lytnews.setOnClickListener(){
                        val intent = Intent(context, WebviewActivity::class.java)
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("getData", dataModel.url)
                        context?.startActivity(intent)
                }
        }

        class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
                var titleTextView: TextView
                var by: TextView
                var time: TextView
                var lytnews: LinearLayout
                init {
                        titleTextView=itemLayoutView.findViewById(R.id.title)
                        by=itemLayoutView.findViewById(R.id.by)
                        time=itemLayoutView.findViewById(R.id.time)
                        lytnews=itemLayoutView.findViewById(R.id.lytnews)
                }
        }
}
