package com.simca.attendance.Model;

public class Users {

    private String name,email,phone,password,roll_no,div,stream;

    public Users() {
    }

    public Users(String name, String email, String phone, String password, String roll_no, String div, String stream) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.roll_no = roll_no;
        this.div = div;
        this.stream = stream;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoll_no() {
        return roll_no;
    }

    public void setRoll_no(String roll_no) {
        this.roll_no = roll_no;
    }

    public String getDiv() {
        return div;
    }

    public void setDiv(String div) {
        this.div = div;
    }

    public String getStream() {
        return stream;
    }

    public void setStream(String stream) {
        this.stream = stream;
    }
}
