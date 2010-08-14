package vnfoss2010.smartshop.serverside.database.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import vnfoss2010.smartshop.serverside.database.DatabaseServiceImpl;

@PersistenceCapable 
public class UserInfo implements Serializable{
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String username;

	@Persistent
	private String password;
	
	private String oldPassword;

	@Persistent
	private String first_name;

	@Persistent
	private String last_name;

	@Persistent
	private String phone;

	@Persistent
	private String email;

	@Persistent
	private Date birthday;

	@Persistent
	private String address;
	
	@Persistent
	private double lat;
	
	@Persistent
	private double lng;

	@Persistent
	private int sum_star;

	@Persistent
	private int count_vote;

	@Persistent
	private double gmt;
	
	@Persistent
	private String lang;

	@Persistent
	private String country;

	@Persistent
	private int type;
	
	@Persistent
	private Set<String> setFriendsUsername;
	
	@Persistent
	private Set<String> fts;

	/**
	 * Default contructor. It should be have to serialize
	 */
	public UserInfo() {
		this("", "", "", "", "", "", new Date(), "", 0, 0, 7D, "vi", "Vietnam");
		
	}
	
	public UserInfo(String username, String firstName, String lastName){
		this.username = username;
		this.first_name = firstName;
		this.last_name = lastName;
	}

	/**
	 * Contructor's used to register
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng) {
		this(username, password, firstName, lastName, phone, email, birthday, address, lat, lng, 0, 0, 7, "vi", "VietNam", 0);
	}
	
	/**
	 * Contructor's used to register
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng, double gmt, String lang, String country) {
		this(username, password, firstName, lastName, phone, email, birthday, address, lat, lng, 0, 0, gmt, lang, country, 0);
	}

	/**
	 * Constructor with full-fields
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng, int sumStar, int countVote, double gmt,
			String lang, String country, int type) {
		this.username = username;
		this.password = password;
		first_name = firstName;
		last_name = lastName;
		this.phone = phone;
		this.email = email;
		this.birthday = birthday;
		this.address = address;
		this.lat = lat;
		this.lng = lng;
		sum_star = sumStar;
		count_vote = countVote;
		this.gmt = gmt;
		this.setLang(lang);
		this.country = country;
		this.type = type;
		
		this.fts = new HashSet<String>();
		DatabaseServiceImpl.updateFTSStuffForUserInfo(this);
		
		this.setFriendsUsername = new HashSet<String>();
	}

	/**
	 * @return the username
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            the username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the first_name
	 */
	public String getFirst_name() {
		return first_name;
	}

	/**
	 * @param firstName
	 *            the first_name to set
	 */
	public void setFirst_name(String firstName) {
		first_name = firstName;
	}

	/**
	 * @return the last_name
	 */
	public String getLast_name() {
		return last_name;
	}

	/**
	 * @param lastName
	 *            the last_name to set
	 */
	public void setLast_name(String lastName) {
		last_name = lastName;
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the birthday
	 */
	public Date getBirthday() {
		return birthday;
	}

	/**
	 * @param birthday
	 *            the birthday to set
	 */
	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @param lat the lat to set
	 */
	public void setLat(double lat) {
		this.lat = lat;
	}

	/**
	 * @return the lat
	 */
	public double getLat() {
		return lat;
	}

	/**
	 * @param lng the lng to set
	 */
	public void setLng(double lng) {
		this.lng = lng;
	}

	/**
	 * @return the lng
	 */
	public double getLng() {
		return lng;
	}

	/**
	 * @return the sum_star
	 */
	public int getSum_star() {
		return sum_star;
	}

	/**
	 * @param sumStar
	 *            the sum_star to set
	 */
	public void setSum_star(int sumStar) {
		sum_star = sumStar;
	}

	/**
	 * @return the count_vote
	 */
	public int getCount_vote() {
		return count_vote;
	}

	/**
	 * @param countVote
	 *            the count_vote to set
	 */
	public void setCount_vote(int countVote) {
		count_vote = countVote;
	}

	/**
	 * @return the gmt
	 */
	public double getGmt() {
		return gmt;
	}

	/**
	 * @param gmt
	 *            the gmt to set
	 */
	public void setGmt(double gmt) {
		this.gmt = gmt;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @param lang the lang to set
	 */
	public void setLang(String lang) {
		this.lang = lang;
	}

	/**
	 * @return the lang
	 */
	public String getLang() {
		return lang;
	}

	/**
	 * @param fts the fts to set
	 */
	public void setFts(Set<String> fts) {
		this.fts = fts;
	}

	/**
	 * @return the fts
	 */
	public Set<String> getFts() {
		return fts;
	}

	/**
	 * @param setFriendsUsername the setFriendsUsername to set
	 */
	public void setSetFriendsUsername(Set<String> setFriendsUsername) {
		this.setFriendsUsername = setFriendsUsername;
	}

	/**
	 * @return the setFriendsUsername
	 */
	public Set<String> getSetFriendsUsername() {
		return setFriendsUsername;
	}

	/**
	 * @param oldPassword the oldPassword to set
	 */
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	/**
	 * @return the oldPassword
	 */
	public String getOldPassword() {
		return oldPassword;
	}

}
