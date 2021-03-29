package com.example.android.gymondoautomationtest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_list.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList


class ListActivity : AppCompatActivity(), ListItemClickListener {

    private val listOfExercises: ArrayList<String> = ArrayList()
    private val adapter: MyAdapter = MyAdapter(listOfExercises, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        getExercises(createRestService())
        setupRecyclerView()
        setupListeners()
    }

    override fun onListItemClicked(item: String) {
        val intent = Intent(this,ImageActivity::class.java)
        intent.putExtra("Item name", item)
        startActivity(intent)
    }

    private fun setupRecyclerView() {
        val dividerItemDecoration =
            DividerItemDecoration(recycler_view.context, LinearLayoutManager.VERTICAL)
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.adapter = adapter
        recycler_view.addItemDecoration(dividerItemDecoration)
    }

    private fun getExercises(restService: RestService) {
        restService.getExerciseList().enqueue(object : Callback<Exercise> {
            override fun onFailure(call: Call<Exercise>, t: Throwable) {
                Toast.makeText(this@ListActivity, "Call failed", Toast.LENGTH_LONG).show()
                t.printStackTrace()
            }

            override fun onResponse(call: Call<Exercise>, response: Response<Exercise>) {
                if (response.isSuccessful) {
                    response.body()?.results
                        ?.filter { !it.name.isNullOrEmpty() }
                        ?.forEach { listOfExercises.add("${it.id} - ${it.name}") }
                    recycler_view.adapter?.notifyDataSetChanged()
                } else {
                    Toast.makeText(this@ListActivity, "Response not successful", Toast.LENGTH_LONG)
                        .show()
                }
            }
        })
    }

    private fun createRestService(): RestService {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://wger.de/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(RestService::class.java)
    }

    private fun setupListeners() {
        btnSearch.setOnClickListener {
            adapter.setNewDataSet(listOfExercises.filter {
                it.toLowerCase().contains(
                    editTxtSearch.text.toString().toLowerCase()
                )
            })
            clearFocusAndCloseKeyboard()
        }

        btnClear.setOnClickListener {
            (recycler_view.adapter as MyAdapter).setNewDataSet(listOfExercises)
            editTxtSearch.text.clear()
        }
    }

    private fun clearFocusAndCloseKeyboard() {
        val view: View? = this.currentFocus
        if (view != null) {
            val imm: InputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
        editTxtSearch.clearFocus()
    }

    class MyAdapter(private var dataSet: List<String>, private val listener: ListItemClickListener) :
        RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

        class MyViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): MyViewHolder {

            val textView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_item, parent, false) as TextView

            return MyViewHolder(textView)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            holder.textView.text = dataSet[position]
            holder.textView.setOnClickListener {
                listener.onListItemClicked(dataSet[position])
            }
        }

        override fun getItemCount() = dataSet.size

        fun setNewDataSet(newDataSet: List<String>) {
            dataSet = newDataSet
            notifyDataSetChanged()
        }
    }


}

