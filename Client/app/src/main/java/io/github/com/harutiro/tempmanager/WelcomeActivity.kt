package io.github.com.harutiro.tempmanager

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.AppLaunchChecker
import androidx.viewpager2.widget.ViewPager2
import io.github.com.harutiro.tempmanager.adapter.ViewPagerAdapter
import io.github.com.harutiro.tempmanager.databinding.ActivityWelcomeBinding
import pub.devrel.easypermissions.EasyPermissions

class WelcomeActivity : AppCompatActivity(), EasyPermissions.PermissionCallbacks{

    private lateinit var binding: ActivityWelcomeBinding
    private val PERMISSION_REQUEST_CODE = 1

    val permissions = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.BLUETOOTH_CONNECT,
        Manifest.permission.BLUETOOTH_SCAN,
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        binding = ActivityWelcomeBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if(AppLaunchChecker.hasStartedFromLauncher(this)){
            Log.d("AppLaunchChecker","2回目以降");
        } else {
            AppLaunchChecker.onActivityCreate(this);
            AlertDialog.Builder(this) // FragmentではActivityを取得して生成
                .setTitle("位置情報の取り扱い")
                .setMessage("このアプリでは位置情報によるアラート機能を可能にするために、現在地のデータが収集されます。\n" +
                        "アプリを閉じている時や、使用していないときにも収集されます。\n" +
                        "位置情報は、個人を特定できない統計的な情報として、\n" +
                        "お知らせの配信、位置情報の利用を許可しない場合は、\n" +
                        "この後表示されるダイアログで「許可しない」を選択してください。")
                .setPositiveButton("OK") { dialog, which ->
                    if (!EasyPermissions.hasPermissions(this, *permissions)) {
                        // パーミッションが許可されていない時の処理
                        EasyPermissions.requestPermissions(this, "パーミッションに関する説明", PERMISSION_REQUEST_CODE, *permissions)
                    }
                }
                .show()
        }



        binding.viewPager.adapter = ViewPagerAdapter(this)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        //スワイプでの動作を無効
        binding.viewPager.isUserInputEnabled = false



        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                //画面が切り替わった時に呼ばれる。

            }
        })
    }

    //戻る動作無効
    override fun onBackPressed() {}

    override fun onPermissionsGranted(requestCode: Int, list: List<String>) {
        recreate()
    }

    override fun onPermissionsDenied(requestCode: Int, list: List<String>) {
        finish()
    }
}