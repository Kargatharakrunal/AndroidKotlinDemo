
package  com.example.androidkotlindemo.activity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.androidkotlindemo.R
import com.example.androidkotlindemo.adapter.RecyclerAdapter
import com.example.androidkotlindemo.global.ApiClient
import com.example.androidkotlindemo.interfaces.ApiInterface
import com.example.androidkotlindemo.model.Movie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var movieList: List<Movie>? = null
    var recyclerView: RecyclerView? = null
    var recyclerAdapter: RecyclerAdapter? = null
    private var layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    private var layoutManager1 = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    private var thread: Thread? = null
    var imageView: ImageView? = null
    private var layoutVer: Boolean = true
    private lateinit var txtVer: TextView
    private lateinit var txtHor: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        movieList = ArrayList()
        recyclerView = findViewById(R.id.recyclerview)
        imageView = findViewById(R.id.splash)
        txtVer = findViewById(R.id.txtVer)
        txtHor = findViewById(R.id.txtHor)

        txtVer.setOnClickListener(this)
        txtHor.setOnClickListener(this)
        setData()

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Toast.makeText(this, "Finish", Toast.LENGTH_SHORT).show()
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.txtVer -> {
                layoutVer = true
                setData()
            }
            R.id.txtHor -> {
                layoutVer = false
                setData()
            }
        }
    }

    private fun setData() {
        recyclerView!!.layoutManager = if (layoutVer) {
            layoutManager
        } else {
            layoutManager1
        }
        recyclerAdapter = RecyclerAdapter(applicationContext, movieList)
        recyclerView!!.adapter = recyclerAdapter
        thread = object : Thread() {
            override fun run() {
                try {
                    synchronized(this) {
                        imageView!!.visibility = View.VISIBLE
                        recyclerView!!.visibility = View.GONE
                    }
                } catch (e: Exception) {
                } finally {
                    val apiInterface: ApiInterface = ApiClient.getClient!!.create(
                        ApiInterface::class.java
                    )
                    val call: Call<List<Movie>> =
                        apiInterface.getMovies
                    call.enqueue(object :
                        Callback<List<Movie>> {
                        override fun onResponse(
                            call: Call<List<Movie>>,
                            response: Response<List<Movie>>
                        ) {
                            movieList = response.body()
                            recyclerAdapter!!.setMovieList(movieList)
                            imageView!!.visibility = View.GONE
                            recyclerView!!.visibility = View.VISIBLE
                        }

                        override fun onFailure(
                            call: Call<List<Movie>>,
                            t: Throwable
                        ) {
                        }
                    })
                }
            }
        }
        (thread as Thread).start()
    }
}