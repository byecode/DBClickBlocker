package io.zyw.dbclickblocker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import io.zyw.fastclickblocker.DBClickBlocker;

public class MainActivity extends AppCompatActivity {

  private Activity self;

  public MainActivity() {
    this.self = this;
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DBClickBlocker.apply(this);
    setContentView(R.layout.activity_main);
    findViewById(R.id.main_btn_goto_second).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        startActivity(new Intent(self,SecondActivity.class));
      }
    });
  }
}
