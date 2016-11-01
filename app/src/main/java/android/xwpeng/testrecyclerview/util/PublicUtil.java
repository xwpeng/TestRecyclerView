package android.xwpeng.testrecyclerview.util;

import android.widget.Toast;
import android.xwpeng.testrecyclerview.MyApp;

/**
 * Created by xwpeng on 16-10-25.
 */

public class PublicUtil {
    public static void showToast(String msg) {
        Toast.makeText(MyApp.get(), msg, Toast.LENGTH_SHORT).show();
    }
}
