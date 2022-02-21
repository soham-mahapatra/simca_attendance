package com.simca.attendance.Model;

public class Code {

    private String code,subCode,subject;

    public Code() {
    }

    public Code(String code, String subCode, String subject) {
        this.code = code;
        this.subCode = subCode;
        this.subject = subject;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}
