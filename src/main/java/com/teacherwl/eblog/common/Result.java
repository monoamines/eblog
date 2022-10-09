package com.teacherwl.eblog.common;

import com.baomidou.mybatisplus.extension.api.R;
import lombok.Data;
import org.omg.CORBA.PRIVATE_MEMBER;

import java.io.Serializable;

@Data
public class Result implements Serializable {

    private  String msg;
    private  int status;
    private  Object data;

    private  String action;


    public  static  Result  success(String msg,Object data)
    {
        Result result=new Result();
        result.status=0;
        result.msg=msg;
        result.data=data;
        return  result;
    }

    public  static  Result  success(Object data)
    {
       return Result.success("success",data);
    }

    public  static  Result  success()
    {
        return Result.success("success",null);
    }

    public  static  Result  fail(String msg)
    {
        Result result=new Result();
        result.status=-1;
        result.msg=msg;
        return  result;
    }

    public  static  Result  fail()
    {
        Result result=new Result();
        result.status=-1;
        return  result;
    }

    public Result action(String action)
    {
            this.action=action;
            return this;
    }
}
