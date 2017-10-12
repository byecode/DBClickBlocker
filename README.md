# DBClickBlocker
单击事件防止变成双击事件～ avoid fast click event in android

Usage：

   任意可拿到 Activity 或者 View 的实例对象 执行
   
   DBClickBlocker.apply(this);
   
   Simple：
  
   
   ```
  
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_second);
    findViewById(R.id.second_btn_install_block).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
      //enable DBClickBlocker
        DBClickBlocker.apply(self);
      }
    });
    findViewById(R.id.second_btn_uninstall_block).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        // uninstall DBClickBlocker
        DBClickBlocker.uninstall(self);
      }
    });
    findViewById(R.id.second_btn_blocked).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        log("click success !!");
      }
    });
  }
   ```

  更多例子：
   https://github.com/byecode/DBClickBlocker/blob/master/sample/
  
  
  引入依赖 ：
  
  编辑 app/build.gradle 
  ```
  dependencies {
    compile fileTree(include: ['*.jar'], dir: 'libs')
    androidTestCompile('com.android.support.test.espresso:espresso-core:2.2.2', {
      exclude group: 'com.android.support', module: 'support-annotations'
    })
    compile 'com.android.support:appcompat-v7:26.+'
    compile 'com.android.support.constraint:constraint-layout:1.0.0-alpha7'
    testCompile 'junit:junit:4.12'
    //here
    compile 'io.zyw.os:fastclickblocker:1.1'
  }
  ```

  
  