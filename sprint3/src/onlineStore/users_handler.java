package onlineStore;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
public class users_handler {
	private dataBaseConnection db;
	user current;
	public users_handler() throws ClassNotFoundException, SQLException {
		db = new dataBaseConnection();
	}
 public boolean verifyLogIn(userLoginInfo info) throws SQLException {
	 String q1 = "select * from users WHERE username = '" +	info.getUsername() +  
             "' AND password = '" + info.getPassword() + "'";
	 db.rs= db.st.executeQuery(q1);
	 if(db.rs.next()) {
		 return true;
	 }else {
		 return false;
	 }
}
public user addUser(userLoginInfo info,String name,String DOB,String phone, String country,String Type) throws  SQLException {
		current.setLoginInfo(info);
		current.setName(name);
		current.setPhone(DOB);
		current.setDOB(phone);
		current.setCountry(country);
		String q1 = "insert into users values('" +info.getUsername()+ "', '" +info.getPassword()+  
                "', '" +name+ "', '" +DOB+ "', '" +phone+ "', '" +country+ "', '" +info.getEmail()+ "','" + Type + "')";
		int x = db.st.executeUpdate(q1); 
        if (x > 0) {             
        	return current;
        }else {            
    		return null;
        }
};

public user getUser(userLoginInfo info) throws SQLException{
	user current;
	ArrayList<String> userinfo = new ArrayList<String>();
	 String q1 = "select * from users WHERE username = '" + info.getUsername()+ "' or Email = '"+ info.getUsername() +"'";
	 db.rs= db.st.executeQuery(q1);
	 db.rs.next();
  	for(int i = 2;i<10;i++) {
  		userinfo.add(db.rs.getString(i));
    }
  	if(userinfo.get(8).equals("admin")) {
		current = new admin();
	}else if(userinfo.get(8).equals("client")) {
		current = new client();
	}else {
		current = new business();
	};
	//make username and email in their correct place;
	info.setUsername(userinfo.get(0));
	info.setEmail(userinfo.get(6));
	//add user attributes
	current.setLoginInfo(info);
	current.setName(userinfo.get(2));
	current.setDOB(userinfo.get(3));
	current.setPhone(userinfo.get(4));
	current.setCountry(userinfo.get(5));

    return current;
}

public boolean userNameAvailable(String username) throws SQLException {
	    String s = "select count(*) from (select username as username from admin union all select username from business union all select username from client) a where username = '"+username+"'";     
	    db.rs = db.st.executeQuery(s);
	    db.rs.next();
	    if(db.rs.getInt(1) > 0) {
	    	return false;
	    }else {
	    	return true;
	    }
	    
}

}
