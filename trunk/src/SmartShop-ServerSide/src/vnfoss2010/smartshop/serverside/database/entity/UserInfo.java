package vnfoss2010.smartshop.serverside.database.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import vnfoss2010.smartshop.serverside.Global;
import vnfoss2010.smartshop.serverside.utils.SearchCapable;

import com.google.gson.annotations.Exclude;

@PersistenceCapable
public class UserInfo extends SearchCapable implements Serializable{
	private static final long serialVersionUID = 1L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private String username;

	@Persistent 
	private String password;

	private String old_password;

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
	private String avatarLink;

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
	
	private String sessionId;

	@Persistent
	private List<Long> listInteredProduct;

	@Persistent
	private Set<String> setFriendsUsername;

	@Exclude
	@Persistent
	private List<Long> listSubcribeProduct;

	public List<Long> getListSubcribeProduct() {
		return listSubcribeProduct;
	}

	public void setListSubcribeProduct(List<Long> listSubcribeProduct) {
		this.listSubcribeProduct = listSubcribeProduct;
	}

	@Exclude
	@Persistent
	private Set<String> fts;

	/**
	 * Default contructor. It should be have to serialize
	 */
	public UserInfo() {
		this("", "", "", "", "", "", null, "", 0, 0, "");
	}

	/*
	 * Some basic information, used in download list friend (lightweight as
	 * possible)
	 */
	public UserInfo(String username, String firstName, String lastName) {
		this.username = username;
		this.first_name = firstName;
		this.last_name = lastName;
	}

	/**
	 * Contructor's used to register
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng, String avatar) {
		this(username, password, firstName, lastName, phone, email, birthday,
				address, lat, lng, avatar, 7, "vi", "VietNam");
	}

	/**
	 * Contructor's used to register
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng, String avatarLink,
			double gmt, String lang, String country) {
		this(username, password, firstName, lastName, phone, email, birthday,
				address, lat, lng, avatarLink, 0, 0, gmt, lang, country, 0);
	}

	/**
	 * Constructor with full-fields
	 */
	public UserInfo(String username, String password, String firstName,
			String lastName, String phone, String email, Date birthday,
			String address, double lat, double lng, String avatarLink,
			int sumStar, int countVote, double gmt, String lang,
			String country, int type) {
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
		this.avatarLink = avatarLink;
		sum_star = sumStar;
		count_vote = countVote;
		this.gmt = gmt;
		this.setLang(lang);
		this.country = country;
		this.type = type;

		listInteredProduct = new ArrayList<Long>();
		this.fts = new HashSet<String>();
		// AccountServiceImpl.updateFTSStuffForUserInfo(this);
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
	 * @param lat
	 *            the lat to set
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
	 * @param lng
	 *            the lng to set
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
	 * @param lang
	 *            the lang to set
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
	 * @param fts
	 *            the fts to set
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
	 * @param setFriendsUsername
	 *            the setFriendsUsername to set
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
	 * /**
	 * 
	 * @param avatarLink
	 *            the avatarLink to set
	 */
	public void setAvatarLink(String avatarLink) {
		this.avatarLink = avatarLink;
	}

	/**
	 * @return the old_password
	 */
	public String getOld_password() {
		return old_password;
	}

	/**
	 * @param oldPassword
	 *            the old_password to set
	 */
	public void setOld_password(String oldPassword) {
		old_password = oldPassword;
	}

	/**
	 * @return the avatarLink
	 */
	public String getAvatarLink() {
		return avatarLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	// @Override
	// public String toString() {
	// return "UserInfo [username=" + username + ", password=" + password
	// + "]";
	// }

	// public JSONObject toJSON(JSONObject json) throws JSONException {
	// json.put("username", username);
	// json.put("password", password);
	// json.put("first_name", first_name);
	// json.put("last_name", last_name);
	// json.put("phone", phone);
	// json.put("email", email);
	// json.put("birthday", birthday);
	// json.put("address", address);
	// json.put("lat", lat);
	// json.put("lng", lng);
	// json.put("avatarLink", avatarLink);
	// json.put("sum_star", sum_star);
	// json.put("count_vote", count_vote);
	// json.put("gmt", gmt);
	// json.put("lang", lang);
	// json.put("country", country);
	// json.put("type", type);
	// return json;
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "UserInfo [address=" + address + ", avatarLink=" + avatarLink
				+ ", birthday=" + birthday + ", count_vote=" + count_vote
				+ ", country=" + country + ", email=" + email + ", first_name="
				+ first_name + ", fts=" + fts + ", gmt=" + gmt + ", lang="
				+ lang + ", last_name=" + last_name + ", lat=" + lat
				+ ", listInteredProduct=" + listInteredProduct + ", lng=" + lng
				+ ", old_password=" + old_password + ", password=" + password
				+ ", phone=" + phone + ", setFriendsUsername="
				+ setFriendsUsername + ", sum_star=" + sum_star + ", type="
				+ type + ", username=" + username + "]";
	}

	/**
	 * @param listInteredProduct
	 *            the listInteredProduct to set
	 */
	public void setListInteredProduct(List<Long> listInteredProduct) {
		this.listInteredProduct = listInteredProduct;
	}

	/**
	 * @return the listInteredProduct
	 */
	public List<Long> getListInteredProduct() {
		return listInteredProduct;
	}
	
	@Override
	public String getTokenString() {
		return getUsername() + " " + getFirst_name() + " " + getLast_name();
	}

	public static void main(String[] args) {
		UserInfo newUser = new UserInfo("tamvo", "tamvo", "Tam", "Vo Minh",
				"123123123", "vo.mita.ov@gmail.com", new Date(88, 12, 22),
				"Binh Tan district", 10.213D, 106.123123D, "");

		System.out.println(Global.gsonDateWithoutHour.toJson(newUser));

	}

	/**
	 * @param sessionId the sessionId to set
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the sessionId
	 */
	public String getSessionId() {
		return sessionId;
	}

}
