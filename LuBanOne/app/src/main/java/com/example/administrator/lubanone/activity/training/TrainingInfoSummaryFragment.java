package com.example.administrator.lubanone.activity.training;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lubanone.R;

/**
 * Created by Administrator on 2017\6\23 0023.
 */

public class TrainingInfoSummaryFragment extends Fragment {

    private TextView summaryText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.training_info_summary_frgment, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        summaryText = (TextView)getActivity().findViewById(R.id.training_info_detail_summary);
        summaryText.setText("如果你无法简单的表达你的想法，那只说明你还不够了解它。" +
                "如果你无法简单的表达你的想法，那只说明你还不够了解它。" +
                "如果你无法简单的表达你的想法，那只说明你还不够了解它。" +
                "如果你无法简单的表达你的想法，那只说明你还不够了解它。" +
                "如果你无法简单的表达你的想法，那只说明你还不够了解它。");
    }
}
