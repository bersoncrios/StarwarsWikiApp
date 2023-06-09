package io.github.bersoncrios.starwarsfans.view

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.facebook.shimmer.ShimmerFrameLayout
import io.github.bersoncrios.starwarsfans.Application
import io.github.bersoncrios.starwarsfans.R
import io.github.bersoncrios.starwarsfans.databinding.ActivityMainBinding
import io.github.bersoncrios.starwarsfans.view.adapter.PersonAdapter
import io.github.bersoncrios.starwarsfans.viewmodels.PersonViewModel
import io.github.bersoncrios.starwarsfans.viewmodels.ViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var viewModel : PersonViewModel
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.rvPersons.setHasFixedSize(true)
        binding.rvPersons.layoutManager = LinearLayoutManager(this)

        val repository = (application as Application).personRepository

        viewModel = ViewModelProvider(this, ViewModelFactory(repository))[PersonViewModel::class.java]


        viewModel.persons.observe(this) { persons ->
            persons?.let { persons ->
                adapter = PersonAdapter(
                    this, persons,
                    PersonAdapter.OnClickListener {result ->
                        Toast.makeText(applicationContext, result.url, Toast.LENGTH_SHORT).show()
                    }
                )

                binding.rvPersons.adapter = adapter
                adapter.notifyDataSetChanged()
                binding.shimmerLayout.startShimmerAnimation()
                binding.shimmerLayout.visibility = View.GONE
                binding.rvPersons.visibility = View.VISIBLE
            }
        }
    }

    override fun onResume() {
        binding.shimmerLayout.startShimmerAnimation()
        super.onResume()
    }

    override fun onPause() {
        binding.shimmerLayout.stopShimmerAnimation()
        super.onPause()
    }
}