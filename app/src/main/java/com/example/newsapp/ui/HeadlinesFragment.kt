package com.example.newsapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newsapp.adapters.NewsAdapter
import com.example.newsapp.api.RetrofitClient
import com.example.newsapp.data.repository.NewsRepository
import com.example.newsapp.databinding.FragmentHeadlinesBinding
import com.example.newsapp.viewmodels.HeadlinesViewModel


class HeadlinesFragment : Fragment(), NewsAdapter.OnArticleClickListener {

    private lateinit var headlinesViewModel: HeadlinesViewModel
    private lateinit var headlinesNewsAdapter: NewsAdapter

    private var _binding: FragmentHeadlinesBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHeadlinesBinding.inflate(inflater, container, false)
        val view = binding.root

        headlinesNewsAdapter = NewsAdapter(this)

        val recyclerView = binding.rvHeadlines
        recyclerView.adapter = headlinesNewsAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Create instance of NewsApiService
        val newsApiService = RetrofitClient.createService()

        val newsRepository = NewsRepository(newsApiService)

        // Initialize HeadlinesViewModel with NewsRepository
        headlinesViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(HeadlinesViewModel::class.java)) {
                    @Suppress("UNCHECKED_CAST")
                    return HeadlinesViewModel(newsRepository) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        })[HeadlinesViewModel::class.java]


        headlinesViewModel.topHeadlines.observe(viewLifecycleOwner) { articles ->
            headlinesNewsAdapter.submitList(articles)
        }

        // Fetch top headlines when the fragment is created
        headlinesViewModel.fetchTopHeadlines("in")

        return view
    }

    override fun onArticleClicked(articleUrl: String) {
        val action = HomeFragmentDirections.actionHomeFragmentToArticleFragment(articleUrl)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}