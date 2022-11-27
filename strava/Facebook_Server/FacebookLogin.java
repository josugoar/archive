import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.codec.digest.*;

public class FacebookLogin {
    
    private boolean exists = false;

    private List<List<String>> users = new ArrayList<>();
    //TODO Add users

    public boolean exists(String email, String password){
        for (List<String> user : userss) {
            String encodedPass = DigestUtils.sha1Hex(user.get(1));
            if (email.equals(user.get(0)) && password.equals(encodedPass)) {
                exists = true;
                break;
            } else {
                exists = false;
            }

            return exists;
            
        }
    }


}
