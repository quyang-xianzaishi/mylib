package com.example.administrator.lubanone.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.activity.training.ExamCenterFragment;
import com.example.administrator.lubanone.activity.training.MyTrainingPagerFragment;
import com.example.administrator.lubanone.activity.training.TrainCenterFragment;
import com.example.administrator.lubanone.adapter.CommonTabLayoutFragmentAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TrainingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TrainingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrainingFragment extends Fragment {

  private String[] pagerTitles ;
  private TabLayout trainingTabLayout;
  private ViewPager trainingViewPager;

  private CommonTabLayoutFragmentAdapter adapter;

  private TrainCenterFragment trainCenterFragment;
  private ExamCenterFragment examCenterFragment;
  private MyTrainingPagerFragment myTrainingPagerFragment;

  private List<Fragment> fragments = new ArrayList<>();
  private FragmentManager fm;

  // TODO: Rename parameter arguments, choose names that match
  private static final String ARG_PARAM1 = "param1";
  private static final String ARG_PARAM2 = "param2";

  // TODO: Rename and change types of parameters
  private String mParam1;
  private String mParam2;

  private OnFragmentInteractionListener mListener;

  public TrainingFragment() {
    // Required empty public constructor
  }

  /**
   * Use this factory method to create a new instance of
   * this fragment using the provided parameters.
   *
   * @param param1 Parameter 1.
   * @param param2 Parameter 2.
   * @return A new instance of fragment TrainingFragment.
   */
  // TODO: Rename and change types and number of parameters
  public static TrainingFragment newInstance(String param1, String param2) {
    TrainingFragment fragment = new TrainingFragment();
    Bundle args = new Bundle();
    args.putString(ARG_PARAM1, param1);
    args.putString(ARG_PARAM2, param2);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getArguments() != null) {
      mParam1 = getArguments().getString(ARG_PARAM1);
      mParam2 = getArguments().getString(ARG_PARAM2);
    }
  }

  @Override
  public void onActivityCreated(Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);

    initViews();
  }


  private void initViews() {
    trainingTabLayout = (TabLayout) getActivity().findViewById(R.id.training_tabLayout);
    trainingViewPager = (ViewPager) getActivity().findViewById(R.id.training_fragment_viewPager);
    pagerTitles = new String[]{getActivity().getString(R.string.train_center),
        getActivity().getString(R.string.train_test_center),
        getActivity().getString(R.string.train_my_train)};
    trainCenterFragment = new TrainCenterFragment();
    examCenterFragment = new ExamCenterFragment();
    myTrainingPagerFragment = new MyTrainingPagerFragment();
    fragments.add(trainCenterFragment);
    fragments.add(examCenterFragment);
    fragments.add(myTrainingPagerFragment);
    fm = getActivity().getSupportFragmentManager();
    adapter = new CommonTabLayoutFragmentAdapter(fm, fragments, pagerTitles);

    //使用适配器将ViewPager与Fragment绑定在一起
    trainingViewPager.setAdapter(adapter);
    //将TabLayout与ViewPager绑定在一起
    trainingTabLayout.setupWithViewPager(trainingViewPager);


  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    return inflater.inflate(R.layout.fragment_training, container, false);
  }

  // TODO: Rename method, update argument and hook method into UI event
  public void onButtonPressed(Uri uri) {
    if (mListener != null) {
      mListener.onFragmentInteraction(uri);
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    if (context instanceof OnFragmentInteractionListener) {
      mListener = (OnFragmentInteractionListener) context;
    } else {
      throw new RuntimeException(context.toString()
          + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }

  public void setCurrentPage(int i) {
    if (null != trainingViewPager) {
      trainingViewPager.setCurrentItem(i);
    }
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {

    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);
  }
}
