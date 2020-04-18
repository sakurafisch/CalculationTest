package com.winnerwinter.calculationtest;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import java.util.Random;
import java.util.logging.Handler;

public class MyViewModel extends AndroidViewModel {
    private SavedStateHandle handle;
    private static String KEY_HIGH_SCORE = "key_high_score";
    private static String KEY_LEFT_NUMBER = "key_left_number";
    private static String KEY_RIGHT_NUMBER = "key_right_number";
    private static String KEY_OPERATOR = "key_operator";
    private static String KEY_ANSWER = "key_answer";
    private static String KEY_CURRENT_SCORE = "key_current_score";
    private static String SAVE_SHP_DATA_NAME = "save_shp_data_name";
    private boolean win_flag = false;
    public MyViewModel(@NonNull Application application, SavedStateHandle handle) {
        super(application);
        if (!handle.contains(KEY_HIGH_SCORE)) {
            SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
            handle.set(KEY_HIGH_SCORE, sharedPreferences.getInt(KEY_HIGH_SCORE, 0));
        }
        initHandle(handle);
        /*generator();*/
        this.handle = handle;
    }

    public void initHandle(SavedStateHandle handle) {
        handle.set(KEY_LEFT_NUMBER, 0);
        handle.set(KEY_RIGHT_NUMBER, 0);
        handle.set(KEY_OPERATOR, "+");
        handle.set(KEY_ANSWER, 0);
        handle.set(KEY_CURRENT_SCORE, 0);
    }

    public MutableLiveData<Integer> getLeftNumber() {
        return handle.getLiveData(KEY_LEFT_NUMBER);
    }

    public MutableLiveData<Integer> getRightNumber() {
        return handle.getLiveData(KEY_RIGHT_NUMBER);
    }

    public MutableLiveData<String> getOperator() {
        return handle.getLiveData(KEY_OPERATOR);
    }

    public MutableLiveData<Integer> getHighScore() {
        return handle.getLiveData(KEY_HIGH_SCORE);
    }

    public MutableLiveData<Integer> getCurrentScore() {
        return handle.getLiveData(KEY_CURRENT_SCORE);
    }

    public MutableLiveData<Integer> getAnswer() {
        return handle.getLiveData(KEY_ANSWER);
    }

    public void generator() {
        int LEVEL = 20;  // 难度系数
        Random random = new Random();
        int x, y;
        x = random.nextInt(LEVEL) + 1;
        y = random.nextInt(LEVEL) + 1;

        // 加法减法各占一半
        if (0 == random.nextInt() % 2) {
            getOperator().setValue("+");
            getAnswer().setValue(x);
            getLeftNumber().setValue(y);
            getRightNumber().setValue(x - y);
        } else {
            getOperator().setValue("-");
            if (x > y) {
                getAnswer().setValue(x - y);
                getLeftNumber().setValue(x);
                getRightNumber().setValue(y);
            } else {
                getAnswer().setValue(y - x);
                getLeftNumber().setValue(y);
                getRightNumber().setValue(x);
            }

        }

    }

    public void save() {
        SharedPreferences sharedPreferences = getApplication().getSharedPreferences(SAVE_SHP_DATA_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(KEY_HIGH_SCORE, getHighScore().getValue());
        editor.apply();
    }

    void answerCorrect() {
        getCurrentScore().setValue(getCurrentScore().getValue() + 1);
        if (getCurrentScore().getValue() > getHighScore().getValue()) {
            getHighScore().setValue(getCurrentScore().getValue());
            win_flag = true;

        }
        generator();
    }

    public boolean isWin_flag() {
        return win_flag;
    }

    public void setWin_flag(boolean win_flag) {
        this.win_flag = win_flag;
    }

}
