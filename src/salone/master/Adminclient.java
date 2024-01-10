
package salone.master;



public class Adminclient {
    
    int id,countfeild;
    String c_name,s_name,address,phone_no,email,gst,password;

    public Adminclient(int id, String c_name, String s_name, String address,String phone_no, String email, String gst,String password) {
        this.id = id;
        this.c_name = c_name;
        this.s_name = s_name;
        this.address = address;
        this.phone_no = phone_no;
        this.email = email;
        this.gst = gst;
        this.password = password;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getC_name() {
        return c_name;
    }

    public void setC_name(String c_name) {
        this.c_name = c_name;
    }

    public String getS_name() {
        return s_name;
    }

    public void setS_name(String s_name) {
        this.s_name = s_name;
    }
    
     public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
      public String getPhone_no() {
        return phone_no;
    }

    public void setPhone_no(String phone_no) {
        this.phone_no = phone_no;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGst() {
        return gst;
    }

    public void setGst(String gst) {
        this.gst = gst;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }



   

    
    
    
    
   
}