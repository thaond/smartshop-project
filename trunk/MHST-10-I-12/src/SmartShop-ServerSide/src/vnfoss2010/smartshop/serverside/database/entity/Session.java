package vnfoss2010.smartshop.serverside.database.entity;

import java.util.Date;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class Session {
	@PrimaryKey
    private String username;

    @Persistent
    private String jsessionid;

    @Persistent
    private Date creationDate;

    public Session(String username, String jsessionid, Date creationDate) {
        this.username = username;
        this.jsessionid = jsessionid;
        this.creationDate = creationDate;
    }
    
    // Accessors for the fields.  JDO doesn't use these, but your application does.
    public String getUsername() {
        return username;
    } 
    public void setUsername(String username) {
        this.username = username;
    } 

    public String getJsessionid() {
        return jsessionid;
    } 
    public void setJsessionid(String jsessionid) {
        this.jsessionid = jsessionid;
    } 

    public Date getCreationDate() {
        return creationDate;
    } 
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
}
