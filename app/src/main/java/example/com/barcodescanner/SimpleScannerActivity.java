package example.com.barcodescanner;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class SimpleScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final int PERMISSIONS_REQUEST = 2;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.activity_simple_scanner);
       // setupToolbar();

        try {
            /** Check Mobile Version Code **/
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                /** Check Mobile Version Code  of MarshMallow to get Permission From User **/
                getPermission();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);
    }

    public void getPermission() {
        try {
            int readcameraPerFlag = 0;
            int permissionCount = 0;
            int permissionCountNew = 0;
            int flag = 0;

            /** check permission is GRANTED or Not in Marshmallow */
            int readcameraPer = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);


            if (readcameraPer != PackageManager.PERMISSION_GRANTED) {
                readcameraPerFlag = 1;
                permissionCount += 1;
                flag = 1;
            }


            String[] permissionCArr = new String[permissionCount];

            if (readcameraPerFlag == 1) {
                permissionCArr[permissionCountNew] = Manifest.permission.CAMERA;
                permissionCountNew += 1;
            }

            if (flag == 1) {
                ActivityCompat.requestPermissions(
                        this,
                        permissionCArr,
                        PERMISSIONS_REQUEST
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        try {
            if (requestCode == PERMISSIONS_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        Toast.makeText(this, "Contents = " + rawResult.getText() +
                ", Format = " + rawResult.getBarcodeFormat().toString(), Toast.LENGTH_SHORT).show();

        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScannerView.resumeCameraPreview(SimpleScannerActivity.this);
            }
        }, 2000);
    }
}
