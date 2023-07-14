package com.sun.khobrakhobor.fragments

import android.app.ProgressDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
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

class SearchFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var newsAdapter: NewsAdapter
    private lateinit var btnSearch : Button
    private lateinit var etSearch : EditText
    private lateinit var mProgressDialog : ProgressDialog

    private val apiKey = "1bb313f732b04e25ac5381fdb45ce12e"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        recyclerView = view.findViewById(R.id.recyclerViewSearch)
        btnSearch = view.findViewById(R.id.btnSearch)
        etSearch = view.findViewById(R.id.etSearch)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnSearch.setOnClickListener {
            mProgressDialog = ProgressDialog(requireContext())
            mProgressDialog.setTitle("Searching...")
            mProgressDialog.setMessage("Please wait for few seconds")
            mProgressDialog.show()
            val query = etSearch.text.toString()
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            searchNews(query)
        }
    }
    private fun searchNews(query: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://newsapi.org/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val newsApiService = retrofit.create(ApiInterface::class.java)
        val call = newsApiService.searchNews(query, apiKey)

        call.enqueue(object : Callback<NewsResponse> {
            override fun onResponse(call: Call<NewsResponse>, response: Response<NewsResponse>) {
                if (response.isSuccessful) {
                    mProgressDialog.cancel()
                    val newsResponse = response.body()
                    val articles = newsResponse?.articles

                    articles?.let {
                        newsAdapter = NewsAdapter(it)
                        recyclerView.adapter = newsAdapter
                    }
                } else {
                    // Handle unsuccessful response
                    mProgressDialog.cancel()
                }
            }

            override fun onFailure(call: Call<NewsResponse>, t: Throwable) {
                // Handle failure
                mProgressDialog.cancel()
            }
        })
    }
}