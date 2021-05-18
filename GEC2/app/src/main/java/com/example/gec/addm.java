package com.example.gec;

public class addm {
    String mname;
    int expense;

    public addm()
    {

    }
    public addm(String mname, int expense) {
        this.mname = mname;
        this.expense = expense;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public int getExpense() {
        return expense;
    }

    public void setExpense(int expense) {
        this.expense = expense;
    }
}
