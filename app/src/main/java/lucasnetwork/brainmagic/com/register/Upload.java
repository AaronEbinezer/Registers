package lucasnetwork.brainmagic.com.register;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.PropertyName;

public class Upload {
    private String mname,mkey;
    private String mimageUri;

public Upload(){

}


    public  Upload(String name, String imageuri){
        if(name.trim().equals("")){
            name = "No Name";
        }
        this.mname = name;
        this.mimageUri = imageuri;
    }
    public String getmName() {
        return mname;
    }
    @Exclude
    public String getmKey() {
        return mkey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mkey = mKey;
    }

    public void setmName(String mName) {
        this.mname = mName;
    }
    public String getmImageUri() {
        return mimageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mimageUri = mImageUri;
    }
}
