package com.shenjiajun.customizeviewdemo.viewModels;

import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.shenjiajun.customizeviewdemo.R;
import com.shenjiajun.customizeviewdemo.databinding.ContentLuckWheelBinding;
import com.shenjiajun.customizeviewdemo.models.AwardModel;
import com.shenjiajun.customizeviewdemo.views.LuckyWheelView;

import java.util.ArrayList;

/**
 * Created by shenjj on 2016/12/30.
 */

public class LuckyWheelViewModel extends BaseObservable {

    private static ContentLuckWheelBinding contentLuckWheelBinding;

    public ArrayList<String> contentStringList = new ArrayList<>();
    public ArrayList<AwardModel> awardModelArrayList = new ArrayList<>();
    public String[] viewNames =
            {"00000", "111111", "222222", "333333", "4444444", "5555555"};
    public String[] viewNames2 =
            {"00000", "111111", "222222", "333333", "4444444", "5555555"};
    public double[] percents = {0.1, 0.1, 0.3, 0.05, 0, 0.45};

    public LuckyWheelViewModel() {
        setAwards();
    }

    public void setAwards() {
        awardModelArrayList.clear();
        contentStringList.clear();
        for (int i = 0; i < viewNames.length; i++) {
            AwardModel awardModel = new AwardModel();
            awardModel.setTitle(viewNames[i]);
            awardModel.setContent(viewNames2[i]);
            awardModel.setIndex(i);
            awardModel.setPercent(percents[i]);
            awardModelArrayList.add(awardModel);
            contentStringList.add(viewNames[i]);
        }
        if (null != contentLuckWheelBinding) {
            contentLuckWheelBinding.luckyWheel.setAwardsList(awardModelArrayList);
        }
    }

    public void setContentLuckWheelBinding(ContentLuckWheelBinding contentLuckWheelBinding) {
        this.contentLuckWheelBinding = contentLuckWheelBinding;
        contentLuckWheelBinding.luckyWheel.setAwardsList(awardModelArrayList);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.controler1:
                contentLuckWheelBinding.luckyWheel.startRotation(1);
                break;
            case R.id.controler2:
                contentLuckWheelBinding.luckyWheel.startRotation(5);
                break;
            case R.id.controler3:
                contentLuckWheelBinding.luckyWheel.startRotation(-1);
                break;
            case R.id.controler4:
                contentLuckWheelBinding.luckyWheel.startPreSetRotation();
                break;
            default:
                break;
        }
    }

    @BindingAdapter("onTextChange")
    public static void setOnTextChange(final EditText view, final LuckyWheelViewModel luckyWheelViewModel) {
        view.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (view.getId()) {
                    case R.id.percent_edit0:
                        luckyWheelViewModel.percents[0] = Double.parseDouble(s.toString());
                        break;
                    case R.id.percent_edit1:
                        luckyWheelViewModel.percents[1] = Double.parseDouble(s.toString());
                        break;
                    case R.id.percent_edit2:
                        luckyWheelViewModel.percents[2] = Double.parseDouble(s.toString());
                        break;
                    case R.id.percent_edit3:
                        luckyWheelViewModel.percents[3] = Double.parseDouble(s.toString());
                        break;
                    case R.id.percent_edit4:
                        luckyWheelViewModel.percents[4] = Double.parseDouble(s.toString());
                        break;
                    case R.id.percent_edit5:
                        luckyWheelViewModel.percents[5] = Double.parseDouble(s.toString());
                        break;
                    default:
                        break;
                }
                luckyWheelViewModel.setAwards();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

}
