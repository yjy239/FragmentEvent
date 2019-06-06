package com.yjy.fragmentevent;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * <pre>
 *     @author : yjy
 *     @e-mail : yujunyu12@gmail.com
 *     @date   : 2019/05/28
 *     desc   :
 *     github:yjy239@gitub.com
 * </pre>
 */
public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragmenttest);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
