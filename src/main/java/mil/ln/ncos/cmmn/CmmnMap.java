package mil.ln.ncos.cmmn;

import org.apache.commons.collections4.map.ListOrderedMap;

import mil.ln.ncos.cmmn.util.StringUtil;

public class CmmnMap extends ListOrderedMap<Object, Object> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4093177974878552129L;

	/**
	 * 
	 */

	@Override
	public Object put(Object key, Object value) {
		return super.put(StringUtil.convertCamelCase((String) key), value);
	}

}
