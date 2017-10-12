package io.zyw.dbclickblocker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import io.zyw.fastclickblocker.DBClickBlocker;

public class SecondActivity extends AppCompatActivity {

  private SecondActivity self;
  private TextView tvLog;
  private ScrollView svLogContainer;

  @Override protected void onCreate(Bundle savedInstanceState) {
    this.self = this;
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    tvLog = (TextView) findViewById(R.id.second_tv_log);
    svLogContainer = (ScrollView) findViewById(R.id.second_sv_log_container);
    findViewById(R.id.second_btn_install_block).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DBClickBlocker.apply(self);
      }
    });
    findViewById(R.id.second_btn_uninstall_block).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        DBClickBlocker.uninstall(self);
      }
    });
    findViewById(R.id.second_btn_blocked).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        log("click success !!");
      }
    });
  }

  private void log(CharSequence log) {
    if (tvLog != null) {
      tvLog.append(System.currentTimeMillis() + " : ");
      tvLog.append(log);
      tvLog.append("\n");
      svLogContainer.fullScroll(ScrollView.FOCUS_DOWN);
    }
  }
}
