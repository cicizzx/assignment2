
package com.example.myapplication2048;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication2048.Game2048Layout;

public class MainActivity extends Activity implements Game2048Layout.onGame2048Listener {

	private Game2048Layout mGanme2048Layout;
	private TextView mScore;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		System.out.println("onCreate");

		mScore = (TextView) findViewById(R.id.id_score);
		mGanme2048Layout = (Game2048Layout) findViewById(R.id.id_game2048);
		mGanme2048Layout.setmGame2048Listener(this);

		findViewById(R.id.btn_result).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				mGanme2048Layout.reStart();
			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		System.out.println("onPause");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		System.out.println("onDestroy");
	}

	@Override
	protected void onStart() {
		super.onStart();
		System.out.println("onStart");
	}

	@Override
	protected void onResume() {
		super.onResume();
		System.out.println("onResume");
	}

	@Override
	protected void onStop() {
		super.onStop();
		System.out.println("onStop");
	}


	@Override
	public void onScoreChange(int score) {
		mScore.setText("SCORE:" + score);
	}

	@Override
	public void onGameOver() {
		new AlertDialog.Builder(this).setTitle("GAME OVER")
				.setMessage("LOST " + mScore.getText())
				.setPositiveButton("RESTART", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
						mGanme2048Layout.reStart();
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




