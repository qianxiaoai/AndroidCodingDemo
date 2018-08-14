package cn.lovexiaoai.androidcodingdemo

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onResume() {
        super.onResume()
        val helloTV: TextView = findViewById(R.id.hello_tv) as TextView
        helloTV.text = "你好啊，kotlin"
        helloTV.setTextColor(getResources().getColor(R.color.colorPrimary))
    }
}
