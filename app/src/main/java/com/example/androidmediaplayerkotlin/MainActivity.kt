package com.example.androidmediaplayerkotlin

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.media.MediaDataSource
import android.media.MediaPlayer
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.View
import android.view.View.OnClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import android.os.Environment.getExternalStorageDirectory
import android.widget.ListView
import java.io.File


class MainActivity : AppCompatActivity(), OnClickListener {


    private var list: MutableList<Song> = ArrayList()
    private var player:MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
        }else
            getSongList()


        play.setOnClickListener(this)
        fastforward.setOnClickListener(this)
        stop.setOnClickListener(this)
        next.setOnClickListener(this)
        previous.setOnClickListener(this)
        fastforward.isEnabled = false
        stop.isEnabled = false

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getSongList()
            }
        }
    }

    private fun getSongList(){
        val cursor = contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            null, null, null, null)
        if (cursor != null && cursor.moveToFirst()){
            val titleColumn = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)
            val idColumn = cursor.getColumnIndex(MediaStore.Audio.Media._ID)
            val artistColumn = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)

            do {
                val id = cursor.getLong(idColumn)
                val title = cursor.getString(titleColumn)
                val artist = cursor.getString(artistColumn)
                list!!.add(Song(id, title, artist))

            }while (cursor.moveToNext())
        }
        listview.adapter = MyAdapter(this, list)
//        listview.setOnItemClickListener { adapterView, view, i, l ->
//            //view.isSelected = true
//        }
    }


    override fun onClick(view: View?) {

        when(view){

            play -> {
                if (player == null){
                    val path = Uri.withAppendedPath(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, list[0].id.toString())
                    player= MediaPlayer().apply {
                        setDataSource(this@MainActivity, path)
                        prepare()
                        start()
                        play.setImageResource(android.R.drawable.ic_media_pause)
                        stop.isEnabled = true
                        fastforward.isEnabled = true
                        listview.requestFocusFromTouch()
                        listview.setSelection(0)
                    }
                }else if(player!!.isPlaying){
                    player!!.apply {
                        pause()
                        play.setImageResource(android.R.drawable.ic_media_play)
                        stop.isEnabled = false
                        fastforward.isEnabled = false
                    }
                }else {
                    player!!.apply {
                        start()
                        play.setImageResource(android.R.drawable.ic_media_pause)
                        stop.isEnabled = true
                        fastforward.isEnabled = true
                    }
                }
            }

            stop ->{
                    player!!.stop()
                    player!!.release()
                    player = null
                    play.setImageResource(android.R.drawable.ic_media_play)
                    stop.isEnabled = false
                    fastforward.isEnabled = false
            }
            fastforward -> {
                    if (player!!.isPlaying) {
                        player!!.seekTo(player!!.currentPosition + 2000)
                    }
                }

            next ->{

                var nextPos= listview.selectedItemPosition + 1
                if (nextPos < list.size) {
                    listview.requestFocusFromTouch()
                    listview.setSelection(nextPos)
                }
            }
        }
    }
}
