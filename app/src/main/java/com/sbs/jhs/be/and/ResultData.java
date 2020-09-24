package com.sbs.jhs.be.and;

public class ResultData<BodyType> {
    public String resultCode, msg;
    public BodyType body;

    public boolean isSuccess() {
        return resultCode.startsWith("S-");
    }

    public boolean isFail() {
        return !isSuccess();
    }
}
