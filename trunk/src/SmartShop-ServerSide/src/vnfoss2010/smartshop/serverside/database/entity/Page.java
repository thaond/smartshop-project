package vnfoss2010.smartshop.serverside.database.entity;

import java.util.Date;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable 
public class Page {
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Long id;
	
	@Persistent
	private String name;
	
	@Persistent
	private String content;
	
	@Persistent
	private String link_thumbnail;
	
	@Persistent
	private int page_view;
	
	@Persistent
	private Date date_post;
	
	@Persistent
	private Date last_modified;
	
	@Persistent
	private String username;
	
	@Persistent
	private int category_id;
	
	
}
