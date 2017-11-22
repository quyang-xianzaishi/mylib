package com.example.administrator.lubanone.bean.model;

/**
 * Created by hou on 2017/8/4.
 */

public class TrainTestQuestions {

  private String question;
  private String a,b,c,d;
  private String answer;

  public TrainTestQuestions(String question, String a, String b, String c, String d,
      String answer) {
    this.question = question;
    this.a = a;
    this.b = b;
    this.c = c;
    this.d = d;
    this.answer = answer;
  }

  @Override
  public String toString() {
    return "TrainTestQuestions{" +
        "question='" + question + '\'' +
        ", a='" + a + '\'' +
        ", b='" + b + '\'' +
        ", c='" + c + '\'' +
        ", d='" + d + '\'' +
        ", answer='" + answer + '\'' +
        '}';
  }

  public String getQuestion() {
    return question;
  }

  public void setQuestion(String question) {
    this.question = question;
  }

  public String getA() {
    return a;
  }

  public void setA(String a) {
    this.a = a;
  }

  public String getB() {
    return b;
  }

  public void setB(String b) {
    this.b = b;
  }

  public String getC() {
    return c;
  }

  public void setC(String c) {
    this.c = c;
  }

  public String getD() {
    return d;
  }

  public void setD(String d) {
    this.d = d;
  }

  public String getAnswer() {
    return answer;
  }

  public void setAnswer(String answer) {
    this.answer = answer;
  }
}
