package com.redlinetech.exoplayertiktok

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager2withexoplayer.VideoAdapter
import com.redlinetech.exoplayertiktok.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var videoAdapter: VideoAdapter
    private val videosList = ArrayList<String>()
    private val playerItem = ArrayList<PlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        addItemToList()

        videoAdapter = VideoAdapter(this, videosList, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: PlayerItem) {
                playerItem.add(exoPlayerItem)
            }
        })
        binding.viewPager.adapter = videoAdapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = playerItem.indexOfFirst { it.player.isPlaying }
                if (previousIndex != -1) {
                    val player = playerItem[previousIndex].player
                    player.pause()
                    player.playWhenReady = false
                }
                val newIndex = playerItem.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = playerItem[newIndex].player
                    player.playWhenReady = true
                    player.play()
                }
            }
        })
    }

    private fun addItemToList(){
        videosList.add("https://dopelappstag.azureedge.net/posts/436dfa40-d771-11ec-abe9-8df5ba53cb24/27-1652964230.mp4")
        videosList.add("https://dopelappstag.azureedge.net/posts/1ea13380-d771-11ec-abe9-8df5ba53cb24/89-1652964163.mp4")
        videosList.add("https://dopelappstag.azureedge.net/posts/d7102030-d770-11ec-abe9-8df5ba53cb24/79-1652964104.mp4")
        videosList.add("https://dopelappstag.blob.core.windows.net/posts/619464f0-7209-11ec-9346-537ed3b7c07a/1-1641815406.mp4")
        videosList.add("https://dopelappstag.blob.core.windows.net/posts/68291e00-7209-11ec-9346-537ed3b7c07a/7-1641815427.mp4")
        videosList.add("https://dopelappstag.blob.core.windows.net/posts/619464f0-7209-11ec-9346-537ed3b7c07a/1-1641815406.mp4")
    }

    override fun onPause() {
        super.onPause()
        val index = playerItem.indexOfFirst { it.position == binding.viewPager.currentItem }
        if (index != -1) {
            val player = playerItem[index].player
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = playerItem.indexOfFirst { it.position == binding.viewPager.currentItem }
        if (index != -1) {
            val player = playerItem[index].player
            player.playWhenReady = true
            player.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (playerItem.isNotEmpty()) {
            for (item in playerItem) {
                val player = item.player
                player.stop()
                player.clearMediaItems()
            }
        }
    }

}