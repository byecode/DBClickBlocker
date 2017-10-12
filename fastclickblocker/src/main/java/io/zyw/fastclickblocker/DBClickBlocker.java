package io.zyw.fastclickblocker;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * @author yixia zhangyanwei@yixia.com
 * @version version_code
 */
public class DBClickBlocker extends Handler {
  private static final String TAG = "DBClickBlocker";
  private static final long DEFAULT_DOUBLE_CLICK_TIME = 500;
  private long mMinFastBlockTime = 500;
  private HashMap<Runnable, Long> mClickTimes = new HashMap<>();
  private Handler mOrigHandler = null;

  static {
    try {
      ATTACH_INFO_FIELD = View.class.getDeclaredField("mAttachInfo");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static Field ATTACH_INFO_FIELD;

  public static void apply(Activity activity, long minBlockTime) {
    try {
      View decorView = activity.getWindow().getDecorView();
      apply(decorView, minBlockTime);
    } catch (Exception e) {
    }
  }

  public static void apply(Activity activity) {
    try {
      View decorView = activity.getWindow().getDecorView();
      apply(decorView, DEFAULT_DOUBLE_CLICK_TIME);
    } catch (Exception e) {
    }
  }

  public static void apply(View view) {
    try {
      apply(view, DEFAULT_DOUBLE_CLICK_TIME);
    } catch (Exception e) {
    }
  }

  public static void uninstall(View view) {
    try {
      if (ATTACH_INFO_FIELD == null) {
        Log.e(TAG, "un support device");
        return;
      }
      ATTACH_INFO_FIELD.setAccessible(true);
      Object attachInfo = ATTACH_INFO_FIELD.get(view);
      if (attachInfo == null && view != null) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
          @Override public void onViewAttachedToWindow(View v) {
            try {
              Object _attachInfo = ATTACH_INFO_FIELD.get(v);
              uninstallInner(_attachInfo);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override public void onViewDetachedFromWindow(View v) {
            Log.e(TAG, "remove callback success");
            v.removeOnAttachStateChangeListener(this);
          }
        });
      } else if (attachInfo != null && view != null) {
        uninstallInner(attachInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void uninstall(Activity activity) {
    try {
      View decorView = activity.getWindow().getDecorView();
      uninstall(decorView);
    } catch (Exception e) {
    }
  }

  public static void apply(View view, final long minBlockTime) {
    try {
      if (ATTACH_INFO_FIELD == null) {
        Log.e(TAG, "un support device");
        return;
      }
      ATTACH_INFO_FIELD.setAccessible(true);
      Object attachInfo = ATTACH_INFO_FIELD.get(view);
      if (attachInfo == null && view != null) {
        view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
          @Override public void onViewAttachedToWindow(View v) {
            try {
              Object _attachInfo = ATTACH_INFO_FIELD.get(v);
              applyInner(_attachInfo, minBlockTime);
            } catch (Exception e) {
              e.printStackTrace();
            }
          }

          @Override public void onViewDetachedFromWindow(View v) {
            Log.e(TAG, "remove callback success");
            v.removeOnAttachStateChangeListener(this);
          }
        });
      } else if (attachInfo != null && view != null) {
        applyInner(attachInfo, minBlockTime);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static void applyInner(Object attachInfo, long minBlockTime) throws Exception {
    if (attachInfo != null) {
      Field mHandler = attachInfo.getClass().getDeclaredField("mHandler");
      mHandler.setAccessible(true);
      Handler mAttachInfoHandler = (Handler) mHandler.get(attachInfo);
      if(mAttachInfoHandler instanceof DBClickBlocker){
        ((DBClickBlocker) mAttachInfoHandler).setMinClickTime(minBlockTime);
        Log.e(TAG, "already install double blocker  !");
      }else{
        mHandler.set(attachInfo,
            new DBClickBlocker(mAttachInfoHandler).setMinClickTime(minBlockTime));
        Log.e(TAG, "install double blocker success !");
      }
    }
  }

  private static void uninstallInner(Object attachInfo) throws Exception {
    if (attachInfo != null) {
      Field mHandler = attachInfo.getClass().getDeclaredField("mHandler");
      mHandler.setAccessible(true);
      Handler mAttachInfoHandler = (Handler) mHandler.get(attachInfo);
      if (mAttachInfoHandler instanceof DBClickBlocker) {
        mHandler.set(attachInfo, ((DBClickBlocker) mAttachInfoHandler).mOrigHandler);
      }
      Log.e(TAG, "uninstall double blocker success !");
    }
  }

  public DBClickBlocker(Handler mOrigHandler) {
    this.mOrigHandler = mOrigHandler;
    setMinClickTime(DEFAULT_DOUBLE_CLICK_TIME);
  }

  /**
   * 最低拦截延迟
   */
  public DBClickBlocker setMinClickTime(long minClickTime) {
    mMinFastBlockTime = minClickTime;
    return this;
  }

  @Override public void dispatchMessage(Message msg) {
    mOrigHandler.dispatchMessage(msg);
  }

  @Override public String getMessageName(Message message) {
    return mOrigHandler.getMessageName(message);
  }

  @Override public boolean sendMessageAtTime(Message msg, long uptimeMillis) {
    if (msg.getCallback() != null && "android.view.View.PerformClick".equals(
        msg.getCallback().getClass().getCanonicalName())) {
      long currentTime = System.currentTimeMillis();
      if (mClickTimes.containsKey(msg.getCallback())) {
        if (currentTime - mClickTimes.get(msg.getCallback()) > mMinFastBlockTime) {
          //超过双击时间，可以双击
          Log.e(TAG, "block over time...");
          mClickTimes.put(msg.getCallback(), currentTime);
          return mOrigHandler.sendMessageAtTime(msg, uptimeMillis);
        } else {
          Log.e(TAG, "block over success...");
          return true;
        }
      } else {
        Log.e(TAG, "block over new...");
        mClickTimes.put(msg.getCallback(), currentTime);
        return mOrigHandler.sendMessageAtTime(msg, uptimeMillis);
      }
    }
    return mOrigHandler.sendMessageAtTime(msg, uptimeMillis);
  }

  @Override public String toString() {
    return mOrigHandler.toString();
  }

  @Override public void handleMessage(Message msg) {
    mOrigHandler.handleMessage(msg);
  }
}
