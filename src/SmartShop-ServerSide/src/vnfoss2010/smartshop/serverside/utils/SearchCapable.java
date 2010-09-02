package vnfoss2010.smartshop.serverside.utils;

import java.util.Set;

/**
 * The entity can be searched, must be implemented {@link SearchCapable}
 * @author VoMinhTam
 */
public interface SearchCapable {
	void setFts(Set<String> fts);
	
	Set<String> getFts();
}
