
package com.example.myapplication2048;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication2048.Game2048Layout;

public class MainActivity extends Activity implements Game2048Layout.onGame2048Listener {

	private Game2048Layout myGame2048Layout;
	private TextView myScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Log.d("zzxdebug", "GAME CREATE");

		myScore = (TextView) findViewById(R.id.id_score);
		myGame2048Layout = (Game2048Layout) findViewById(R.id.id_game2048);
		myGame2048Layout.setmGame2048Listener(this);

		findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				myGame2048Layout.reStart();
			}
		});

	}

//	@Override
//	protected void onPause() {
//		super.onPause();
//		System.out.println("onPause");
//	}
//
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//		System.out.println("onDestroy");
//	}
//
//	@Override
//	protected void onStart() {
//		super.onStart();
//		System.out.println("onStart");
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		System.out.println("onResume");
//	}
//
//	@Override
//	protected void onStop() {
//		super.onStop();
//		System.out.println("onStop");
//	}


	@Override
	public void onScoreChange(int score) {
		myScore.setText("SCORE:" + score);
	}

	@Override
	public void onGameOver() {
		new AlertDialog.Builder(this).setTitle("GAME OVER")
				.setMessage("LOST " + myScore.getText())
				.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						myGame2048Layout.reStart();
					}
				})
				.setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						finish();
					}
				}).show();
	}
}




