package com.ensense.insense.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;



import com.cts.mint.common.model.Link;
import com.cts.mint.crawler.model.NormalizedLink;

public class MintCollectionUtil {

	public static Map<String, NormalizedLink> getNormalizedLink(
			ArrayList<Link> links, ArrayList<Link> normalizeWithLinks) {
		Map<String, NormalizedLink> normalizedLinkMap = new HashMap<String, NormalizedLink>();
		
		for(Link link: links){
			NormalizedLink normalizedLink = new NormalizedLink();
			normalizedLink.setFound(true);
			normalizedLink.setLink(link);
			normalizedLinkMap.put(link.getUrl(), normalizedLink);
		}
		
		for(Link normalizeWithLink: normalizeWithLinks){
			NormalizedLink normalizedLink = new NormalizedLink();
			normalizedLink.setFound(false);
			for(Link link: links){
				if ( link.getUrl().equals(normalizeWithLink.getUrl())){
					normalizedLink.setFound(true);
					break;
				}
			}

			if ( ! normalizedLink.isFound() ){
				normalizedLinkMap.put(normalizeWithLink.getUrl(), normalizedLink);
			}
		}

		return normalizedLinkMap;
	}

}
