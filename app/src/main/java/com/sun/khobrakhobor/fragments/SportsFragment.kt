package com.sun.khobrakhobor.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.sun.khobrakhobor.ApiInterface
import com.sun.khobrakhobor.NewsResponse
import com.sun.khobrakhobor.R
import com.sun.khobrakhobor.adapter.NewsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class SportsFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val apiKey = "1bb313f732b04e25ac5381fdb45ce12e"
    private val country = "us"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_sports, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSports)
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        swipeRefreshLayout.setOnRefreshListener {
            fetchNews()
        }

        fetchNews()
    }

    private fun fetchNews() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(ApiInterface::class.java)
        val call = newsApiService.getCategoryNews(country, "sports", apiKey)
        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                swipeRefreshLayout.isRefreshing = false
                if (response.isSuccessful) {
                    val newsResponse = response.body()
                    var articles = newsResponse?.articles
                    articles?.let {
                        articles = it.shuffled() // Shuffle the list of articles
                        newsAdapter = NewsAdapter(articles!!)
                        recyclerView.adapter = newsAdapter
                    }
                } else {
                    // Handle unsuccessful response
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
                swipeRefreshLayout.isRefreshing = false
            }
        })
    }
}