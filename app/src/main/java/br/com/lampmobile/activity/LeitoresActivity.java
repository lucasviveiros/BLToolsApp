package br.com.lampmobile.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.Result;

import br.com.lampmobile.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class LeitoresActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leitores);
    }


    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    public void navQRCode(View v){
        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void handleResult(Result result) {

        String s = "http://www.google.com/search?q=";
        s += result.getText();
        Intent myIntent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
        startActivity(myIntent1);
        // Resulme;
        mScannerView.resumeCameraPreview(this);
    }


}
