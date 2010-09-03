package vnfoss2010.smartshop.serverside.utils;

import java.util.HashSet;
import java.util.Set;

import vnfoss2010.smartshop.serverside.Global;

/**
 * The entity can be searched, must be implemented {@link SearchCapable}
 * @author VoMinhTam
 */
public abstract class SearchCapable {
	public abstract void setFts(Set<String> fts);
	
	public abstract Set<String> getFts();
	
	public abstract String getTokenString();
	
	public void updateFTS() {
		Set<String> new_ftsTokens = SearchJanitorUtils
				.getTokensForIndexingOrQuery(getTokenString(),
						Global.MAX_NUMBER_OF_WORDS_TO_PUT_IN_INDEX);
		Set<String> ftsTokens = getFts();
		if (ftsTokens == null){
			ftsTokens = new HashSet<String>();
			setFts(ftsTokens);
		}
		ftsTokens.clear();

		for (String token : new_ftsTokens) {
			ftsTokens.add(token);
		}
	}
}
