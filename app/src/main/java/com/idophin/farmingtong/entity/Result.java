package com.idophin.farmingtong.entity;

/**
 * Created by liyanchuan on 2017/5/31.
 */

public class Result {
    public int code;
    public String msg;//标题

    @Override
    public String toString() {
        return "code " + code + ";  msg"+msg;
    }
}
