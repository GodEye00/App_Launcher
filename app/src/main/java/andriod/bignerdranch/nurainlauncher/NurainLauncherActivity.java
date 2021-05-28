package andriod.bignerdranch.nurainlauncher;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class NurainLauncherActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return NurainLauncherFragment.newInstance();
    }


}