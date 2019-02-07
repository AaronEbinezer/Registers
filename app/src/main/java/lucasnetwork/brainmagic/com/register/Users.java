package lucasnetwork.brainmagic.com.register;

public class Users {
    private String Name;
    private String Email;
    private String Phonenumber;
    private String Address;
    private String Password;

    public Users(String name, String phonenumber,String email) {
        Name = name;
        Phonenumber = phonenumber;
        Email = email;
    }
     public Users(){

     }

    public String getName() {
        return Name;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }
}
