package com.example.administrator.lubanone.utils;

import com.example.administrator.lubanone.bean.message.Address;

import java.util.Comparator;

/**
 * 排序类
 * //@标签代表A前面的那些，#代表除了A-Z以外的其他标签
 */
public class CompareSort implements Comparator<Address> {
    @Override
    public int compare(Address address1, Address address2) {
        if(address1.getLetter().equals("@") || address2.getLetter().equals("@")){
            //通讯录前面的ｉｔｅｍ(公众号，标签......)
            return address1.getLetter().equals("@") ? -1:1;
        }
        //user1属于#标签，放到后面
        else if(!address1.getLetter().matches("[A-z]+")){
            return 1;
        //user2属于#标签，放到后面
        }else if(!address2.getLetter().matches("[A-z]+")){
            return -1;
        }else {
            return address1.getLetter().compareTo(address2.getLetter());
        }
    }
}
