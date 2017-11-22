package com.example.administrator.lubanone.activity.training;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.lubanone.R;

import com.example.administrator.lubanone.activity.training.activity.ApplyTrainingActivity;
import com.example.administrator.lubanone.activity.training.activity.MyTeamActivity;
import com.example.administrator.lubanone.activity.training.activity.TrainingIntegrationActivity;
import com.example.administrator.lubanone.bean.training.MyApplyTrainingBean;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zheng on 2017\6\24 0024.
 */

public class MyTrainingFragment extends Fragment {

    private TextView myTrainingApply;
    private RecyclerView myTrainingRecyclear;
    private List<MyApplyTrainingBean> myTrainingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_training, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myTrainingApply = (TextView) getActivity().findViewById(R.id.my_training_apply);
        myTrainingRecyclear = (RecyclerView) getActivity().findViewById(R.id.my_training_recyclerview);

    }



}
