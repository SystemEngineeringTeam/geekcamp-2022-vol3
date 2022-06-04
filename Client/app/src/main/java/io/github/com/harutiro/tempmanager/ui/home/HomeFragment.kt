package io.github.com.harutiro.tempmanager.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import io.github.com.harutiro.tempmanager.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    val URL = "http://opc3.qqey.net/"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Web Viewの初期設定
        binding.webView.setWebViewClient(WebViewClient()) // WebViewを設定する
        binding.webView.settings.javaScriptEnabled = true // JavaScriptを有効にする
        binding.webView.loadUrl(URL) // URLを読み込む


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}