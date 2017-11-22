package com.example.administrator.lubanone.bean.homepage;

import java.io.Serializable;

/**
 * 用户已添加银行卡
 * Created by quyang on 2017/6/26.
 */

public class UserCardInfoBean implements Serializable {

    private String cardBand = "北京银行";

    private String accountNumber = "353535r2345345";

    private String userName = "曲洋";


    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getCardBand() {
        return cardBand;
    }

    public void setCardBand(String cardBand) {
        this.cardBand = cardBand;
    }
}
