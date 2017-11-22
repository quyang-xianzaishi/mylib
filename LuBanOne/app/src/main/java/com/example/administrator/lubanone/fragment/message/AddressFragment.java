package com.example.administrator.lubanone.fragment.message;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.lubanone.Config;
import com.example.administrator.lubanone.MyApplication;
import com.example.administrator.lubanone.R;
import com.example.administrator.lubanone.Urls;
import com.example.administrator.lubanone.adapter.message.AddressAdapter;
import com.example.administrator.lubanone.bean.message.Address;
import com.example.administrator.lubanone.bean.message.AddressBean;
import com.example.administrator.lubanone.bean.message.AddressResultBean;
import com.example.administrator.lubanone.customview.MySearchView;
import com.example.administrator.lubanone.customview.SideBarView;
import com.example.administrator.lubanone.interfaces.RequestListener;
import com.example.administrator.lubanone.net.RequestNet;
import com.example.administrator.lubanone.net.RequestParams;
import com.example.administrator.lubanone.utils.ChineseToEnglish;
import com.example.administrator.lubanone.utils.CompareSort;

import com.example.administrator.lubanone.utils.ResultUtil;
import com.example.qlibrary.entity.Result;
import com.example.qlibrary.utils.GsonUtil;
import com.example.qlibrary.utils.SPUtils;
import com.example.qlibrary.utils.ToastUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017\6\28 0028.
 */

public class AddressFragment extends Fragment {

    private LinearLayout groupLinear;
    private MySearchView<Address> addressSearch;
    private ListView addressList;
    private List<Address> addresses;
    private SideBarView addressSlide;
    private AddressAdapter addressAdapter;
    private TextView mTip;
    protected MyApplication myApp;

    public AddressFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_address, container,false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        groupLinear = (LinearLayout) getActivity().findViewById(R.id.address_fragment_group);
        addressSearch = (MySearchView<Address>) getActivity().findViewById(R.id.message_address_search);
        addressList = (ListView) getActivity().findViewById(R.id.address_list);
        addressSlide = (SideBarView) getActivity().findViewById(R.id.address_slide);
        mTip = (TextView) getActivity().findViewById(R.id.tip);

        //initData();
        //requestData();

    }

    @Override
    public void onResume(){
        super.onResume();
        requestData();
    }


    private void initData(){
        String[] contactsArray = getResources().getStringArray(R.array.data);
        String[] headArray = getResources().getStringArray(R.array.head);
        String[] userid = getResources().getStringArray(R.array.id);


        //模拟添加数据到Arraylist
        int length = contactsArray.length;
        addresses = new ArrayList<>();
        for (int i = 0; i < length; i++) {
            Address address = new Address();
            address.setName(contactsArray[i]);
            String firstSpell = ChineseToEnglish.getFirstSpell(contactsArray[i]);
            String substring = firstSpell.substring(0, 1).toUpperCase();
            if(substring.matches("[A-Z]")){
                address.setLetter(substring);
            }else {
                address.setLetter("#");
            }
            address.setUserId(userid[i]);
            addresses.add(address);
        }

        for (int i = 0; i < headArray.length; i++) {
            Address address = new Address();
            address.setName(headArray[i]);
            address.setLetter("@");
            addresses.add(address);
        }

        //排序
        Collections.sort(addresses, new CompareSort());

        //设置数据
        addressAdapter = new AddressAdapter(getActivity(),addresses);
        //addressAdapter.setData(addresses);
        addressList.setAdapter(addressAdapter);

        //设置回调
        addressSlide.setOnLetterSelectListen(new MyLetterSelectListener());

        addressSearch.setDatas(addresses);
        addressSearch.setAdapter(addressAdapter);
        addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Address>() {
            //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
            @Override
            public List filterDatas(List<Address> datas, List<Address> filterdatas, String inputstr) {
                for (int i = 0; i < datas.size(); i++) {
                    //筛选条件
                    if ((datas.get(i).getName()).contains(inputstr)) {
                        filterdatas.add(datas.get(i));
                    }
                }
                return filterdatas;
            }
        });
    }

    private class MyLetterSelectListener implements SideBarView.LetterSelectListener{

        @Override
        public void onLetterSelected(String letter) {
            setListviewPosition(letter);
            mTip.setText(letter);
            mTip.setVisibility(View.VISIBLE);
        }

        @Override
        public void onLetterChanged(String letter) {
            setListviewPosition(letter);
            mTip.setText(letter);
        }

        @Override
        public void onLetterReleased(String letter) {
            mTip.setVisibility(View.GONE);
        }

    }


    private void setListviewPosition(String letter){
        int firstLetterPosition = addressAdapter.getFirstLetterPosition(letter);
        if(firstLetterPosition != -1){
            addressList.setSelection(firstLetterPosition);
        }
    }

    private void requestData() {
        addresses = new ArrayList<>();
        Address address = new Address();
        address.setName(getActivity().getString(R.string.group_chat_list));
        address.setLetter("@");
        Address address1 = new Address();
        address1.setName(getActivity().getString(R.string.new_friend_title));
        address1.setLetter("@");
        addresses.add(address);
        addresses.add(address1);
        Collections.sort(addresses, new CompareSort());
        addressAdapter = new AddressAdapter(getActivity(),addresses);
        addressList.setAdapter(addressAdapter);
        List<RequestParams> list = new ArrayList<>();
        RequestParams paramsToken = new RequestParams(Config.TOKEN,
            SPUtils.getStringValue(getActivity(), Config.USER_INFO, Config.TOKEN, ""));
        list.add(paramsToken);

        RequestNet requestNet = new RequestNet(myApp, getActivity(), list,
            Urls.GET_ADDRESS_LIST, AddressListener, RequestNet.POST);
    }

    //请求数据
    private RequestListener AddressListener = new RequestListener() {
        @Override
        public void onSuccess(JSONObject jsonObject) {
        }

        @Override
        public void testSuccess(String jsonObject) {
            try {
                Result<AddressResultBean> result = GsonUtil
                    .processJson(jsonObject, AddressResultBean.class);
                if (ResultUtil.isSuccess(result) && null != result && null != result.getResult()) {
                    AddressResultBean addressResultBean = result.getResult();
                    if (addressResultBean != null) {
                        //addresses = new ArrayList<>();
                        addressSlide.setVisibility(View.VISIBLE);
                        if(addressResultBean.getList()!=null&&addressResultBean.getList().size()>0){
                            for (int i = 0; i < addressResultBean.getList().size(); i++) {
                                Address address = new Address();
                                address.setUserimg(addressResultBean.getList().get(i).getUserimg());
                                address.setUserId(addressResultBean.getList().get(i).getUserid());
                                address.setName(addressResultBean.getList().get(i).getUsername());
                                String firstSpell = ChineseToEnglish.getFirstSpell(
                                    addressResultBean.getList().get(i).getUsername());
                                String substring = firstSpell.substring(0, 1).toUpperCase();
                                if(substring.matches("[A-Z]")){
                                    address.setLetter(substring);
                                }else {
                                    address.setLetter("#");
                                }
                                addresses.add(address);
                            }
                        }
                    }
                    //addresses = new ArrayList<>();
                    /*Address address = new Address();
                    address.setName(getActivity().getString(R.string.group_chat_list));
                    address.setLetter("@");
                    Address address1 = new Address();
                    address1.setName(getActivity().getString(R.string.new_friend_title));
                    address1.setLetter("@");
                    addresses.add(address1);
                    addresses.add(address);*/
                    //排序
                    Collections.sort(addresses, new CompareSort());
                    if(addressResultBean.getStardata()!=null&&addressResultBean.getStardata().size()>0){
                        addressSlide.setVisibility(View.VISIBLE);
                        for(int j = 0; j < addressResultBean.getStardata().size(); j++){
                            Address address2 = new Address();
                            address2.setUserimg(addressResultBean.getStardata().get(j).getUserimg());
                            address2.setUserId(addressResultBean.getStardata().get(j).getUserid());
                            address2.setName(addressResultBean.getStardata().get(j).getUsername());
                            address2.setLetter("☆");
                            addresses.add(j+2,address2);
                        }
                    }else {
                        if(addressResultBean.getList()==null||addressResultBean.getList().size()<=0){
                            addressSlide.setVisibility(View.GONE);
                        }
                    }
                    //设置数据
//                    addressAdapter = new AddressAdapter(getActivity(),addresses);
//                    addressList.setAdapter(addressAdapter);
                    addressAdapter.notifyDataSetChanged();
                    //设置回调
                    addressSlide.setOnLetterSelectListen(new MyLetterSelectListener());
                    addressSearch.setDatas(addresses);
                    addressSearch.setAdapter(addressAdapter);
                    addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Address>() {
                        //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                        @Override
                        public List filterDatas(List<Address> datas, List<Address> filterdatas, String inputstr) {
                            for (int i = 0; i < datas.size(); i++) {
                                //筛选条件
                                if ((datas.get(i).getName()).contains(inputstr)) {
                                    filterdatas.add(datas.get(i));
                                }
                            }
                            return filterdatas;
                        }
                    });
                } else {
                    addressSlide.setVisibility(View.GONE);
                    /*addresses = new ArrayList<>();
                    Address address = new Address();
                    address.setName(getActivity().getString(R.string.group_chat_list));
                    address.setLetter("@");
                    Address address1 = new Address();
                    address1.setName(getActivity().getString(R.string.new_friend_title));
                    address1.setLetter("@");
                    addresses.add(address);
                    addresses.add(address1);*/
                    //排序
                    Collections.sort(addresses, new CompareSort());
                    //设置数据
                    /*addressAdapter = new AddressAdapter(getActivity(),addresses);
                    addressList.setAdapter(addressAdapter);*/
                    addressAdapter.notifyDataSetChanged();
                    //设置回调
                    addressSlide.setOnLetterSelectListen(new MyLetterSelectListener());
                    addressSearch.setDatas(addresses);
                    addressSearch.setAdapter(addressAdapter);
                    addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Address>() {
                        //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                        @Override
                        public List filterDatas(List<Address> datas, List<Address> filterdatas, String inputstr) {
                            for (int i = 0; i < datas.size(); i++) {
                                //筛选条件
                                if ((datas.get(i).getName()).contains(inputstr)) {
                                    filterdatas.add(datas.get(i));
                                }
                            }
                            return filterdatas;
                        }
                    });
                    /*ToastUtil.showShort(ResultUtil.getErrorMsg(result),
                        getActivity().getApplicationContext());*/
                    //show(getActivity().getResources().getString(R.string.get_address_list_fail));
                }
            } catch (Exception e) {
                /*addresses = new ArrayList<>();
                Address address = new Address();
                address.setName(getActivity().getString(R.string.group_chat_list));
                address.setLetter("@");
                Address address1 = new Address();
                address1.setName(getActivity().getString(R.string.new_friend_title));
                address1.setLetter("@");
                addresses.add(address);
                addresses.add(address1);*/
                //排序
                //Collections.sort(addresses, new CompareSort());
                //设置数据
                /*addressAdapter = new AddressAdapter(getActivity(),addresses);
                addressList.setAdapter(addressAdapter);*/
                addressSlide.setVisibility(View.GONE);
                Collections.sort(addresses, new CompareSort());
                addressAdapter.notifyDataSetChanged();
                //设置回调
                addressSlide.setOnLetterSelectListen(new MyLetterSelectListener());
                addressSearch.setDatas(addresses);
                addressSearch.setAdapter(addressAdapter);
                addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Address>() {
                    //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                    @Override
                    public List filterDatas(List<Address> datas, List<Address> filterdatas, String inputstr) {
                        for (int i = 0; i < datas.size(); i++) {
                            //筛选条件
                            if ((datas.get(i).getName()).contains(inputstr)) {
                                filterdatas.add(datas.get(i));
                            }
                        }
                        return filterdatas;
                    }
                });
                ToastUtil.showShort(getActivity().getResources().getString(R.string.get_address_list_fail),
                    getActivity().getApplicationContext());
            }
        }

        @Override
        public void onFail(String errorMsf) {
            addressSlide.setVisibility(View.GONE);
            Collections.sort(addresses, new CompareSort());
            addressAdapter.notifyDataSetChanged();
            //设置回调
            addressSlide.setOnLetterSelectListen(new MyLetterSelectListener());
            addressSearch.setDatas(addresses);
            addressSearch.setAdapter(addressAdapter);
            addressSearch.setSearchDataListener(new MySearchView.SearchDatas<Address>() {
                //参数一：全部数据，参数二：筛选后的数据，参数三：输入的内容
                @Override
                public List filterDatas(List<Address> datas, List<Address> filterdatas, String inputstr) {
                    for (int i = 0; i < datas.size(); i++) {
                        //筛选条件
                        if ((datas.get(i).getName()).contains(inputstr)) {
                            filterdatas.add(datas.get(i));
                        }
                    }
                    return filterdatas;
                }
            });
            ToastUtil.showShort(errorMsf,
                getActivity().getApplicationContext());
        }
    };

}
