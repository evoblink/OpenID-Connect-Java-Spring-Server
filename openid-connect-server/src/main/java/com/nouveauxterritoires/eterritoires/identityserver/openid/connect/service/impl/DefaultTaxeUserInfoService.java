/*******************************************************************************
 * Copyright 2014 The MITRE Corporation
 *   and the MIT Kerberos and Internet Trust Consortium
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.nouveauxterritoires.eterritoires.identityserver.openid.connect.service.impl;

import com.google.common.base.Strings;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.model.TaxeUserInfo;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.repositoty.TaxeUserInfoRepository;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.service.TaxeUserInfoService;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import org.mitre.oauth2.model.ClientDetailsEntity;
import org.mitre.oauth2.model.ClientDetailsEntity.SubjectType;
import org.mitre.oauth2.service.ClientDetailsEntityService;
import org.mitre.openid.connect.model.UserInfo;
import org.mitre.openid.connect.service.PairwiseIdentiferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Implementation of the TaxeUserInfoService
 * 
 * @author Michael Joseph Walsh, jricher
 * 
 */
@Service
public class DefaultTaxeUserInfoService implements TaxeUserInfoService {

	private static Logger logger = LoggerFactory.getLogger(DefaultTaxeUserInfoService.class);

	@Autowired
	private TaxeUserInfoRepository taxeUserInfoRepository;

	@Autowired
	private ClientDetailsEntityService clientService;

	@Autowired
	private PairwiseIdentiferService pairwiseIdentifierService;

	@Override
	public void save(UserInfo userInfo) {
		taxeUserInfoRepository.save(userInfo);
	}

	public TaxeUserInfo save(TaxeUserInfo taxeUserInfo) {
		return (TaxeUserInfo)taxeUserInfoRepository.save(taxeUserInfo);
	}

	@Override
	public TaxeUserInfo getById(Long id) {
		return (TaxeUserInfo)taxeUserInfoRepository.getById(id);
	}

	@Override
	public TaxeUserInfo getBySubject(String sub) {
		return (TaxeUserInfo)taxeUserInfoRepository.getBySubject(sub);
	}

	@Override
	public void remove(UserInfo userInfo) {
		taxeUserInfoRepository.remove(userInfo);
	}

	@Override
	public TaxeUserInfo getByUsername(String username) {
		return taxeUserInfoRepository.getByUsername(username);
	}

	@Override
	public TaxeUserInfo getByUsernameAndClientId(String username, String clientId) {

		ClientDetailsEntity client = clientService.loadClientByClientId(clientId);

		TaxeUserInfo taxeUserInfo = getByUsername(username);

		if (client == null || taxeUserInfo == null) {
			return null;
		}

		if (SubjectType.PAIRWISE.equals(client.getSubjectType())) {
			String pairwiseSub = pairwiseIdentifierService.getIdentifier(taxeUserInfo, client);
			taxeUserInfo.setSub(pairwiseSub);
		}

		return taxeUserInfo;

	}

	public Collection<TaxeUserInfo> getAllUsers() {
		return taxeUserInfoRepository.getAll();
	}

	/**
	 * Generates a clientId for the given client and sets it to the client's clientId field. Returns the client that was passed in, now with id set.
	 */
	@Override
	public TaxeUserInfo generateTaxeUserInfoSub(TaxeUserInfo taxeUserInfo) {
		taxeUserInfo.setSub(UUID.randomUUID().toString());
		return taxeUserInfo;
	}
        
        
	@Override
	public TaxeUserInfo saveNewTaxeUserInfo(TaxeUserInfo taxeUserInfo) {
		if (taxeUserInfo.getId() != null) { // if it's not null, it's already been saved, this is an error
			throw new IllegalArgumentException("Tried to save a new taxeUserInfo with an existing ID: " + taxeUserInfo.getId());
		}

//		if (taxeUserInfo.getRegisteredRedirectUri() != null) {
//			for (String uri : taxeUserInfo.getRegisteredRedirectUri()) {
//				if (blacklistedSiteService.isBlacklisted(uri)) {
//					throw new IllegalArgumentException("TaxeUserInfo URI is blacklisted: " + uri);
//				}
//			}
//		}

		// assign a random clientid if it's empty
		// NOTE: don't assign a random taxeUserInfo secret without asking, since public clients have no secret
		if (Strings.isNullOrEmpty(taxeUserInfo.getSub())) {
			taxeUserInfo = generateTaxeUserInfoSub(taxeUserInfo);
		}

//		// if the taxeUserInfo is flagged to allow for refresh tokens, make sure it's got the right granted scopes
//		if (taxeUserInfo.isAllowRefresh()) {
//			taxeUserInfo.getScope().add(SystemScopeService.OFFLINE_ACCESS);
//		} else {
//			taxeUserInfo.getScope().remove(SystemScopeService.OFFLINE_ACCESS);
//		}

		// timestamp this to right now
		taxeUserInfo.setCreatedAt(new Date());


//		// check the sector URI
//		if (!Strings.isNullOrEmpty(taxeUserInfo.getSectorIdentifierUri())) {
//			try {
//				List<String> redirects = sectorRedirects.get(taxeUserInfo.getSectorIdentifierUri());
//
//				if (taxeUserInfo.getRegisteredRedirectUri() != null) {
//					for (String uri : taxeUserInfo.getRegisteredRedirectUri()) {
//						if (!redirects.contains(uri)) {
//							throw new IllegalArgumentException("Requested Redirect URI " + uri + " is not listed at sector identifier " + redirects);
//						}
//					}
//				}
//
//			} catch (ExecutionException e) {
//				throw new IllegalArgumentException("Unable to load sector identifier URI: " + taxeUserInfo.getSectorIdentifierUri());
//			}
//		}


		// make sure a taxeUserInfo doesn't get any special system scopes
//		taxeUserInfo.setScope(scopeService.removeRestrictedScopes(taxeUserInfo.getScope()));

		TaxeUserInfo tu = taxeUserInfoRepository.save(taxeUserInfo);

//		statsService.resetCache();

		return tu;
	}

	/**
	 * Update the oldClient with information from the newClient. The
	 * id from oldClient is retained.
	 * 
	 * Checks to make sure the refresh grant type and
	 * the scopes are set appropriately.
	 * 
	 * Checks to make sure the redirect URIs aren't blacklisted.
	 * 
	 * Attempts to load the redirect URI (possibly cached) to check the
	 * sector identifier against the contents there.
	 * 
	 * 
	 */
	@Override
	public TaxeUserInfo updateTaxeUserInfo(TaxeUserInfo oldTaxeUserInfo, TaxeUserInfo newTaxeUserInfo) throws IllegalArgumentException {
		if (oldTaxeUserInfo != null && newTaxeUserInfo != null) {

//			for (String uri : newTaxeUserInfo.getRegisteredRedirectUri()) {
//				if (blacklistedSiteService.isBlacklisted(uri)) {
//					throw new IllegalArgumentException("TaxeUserInfo URI is blacklisted: " + uri);
//				}
//			}

//			// if the client is flagged to allow for refresh tokens, make sure it's got the right scope
//			if (newTaxeUserInfo.isAllowRefresh()) {
//				newTaxeUserInfo.getScope().add(SystemScopeService.OFFLINE_ACCESS);
//			} else {
//				newTaxeUserInfo.getScope().remove(SystemScopeService.OFFLINE_ACCESS);
//			}

//			// check the sector URI
//			if (!Strings.isNullOrEmpty(newTaxeUserInfo.getSectorIdentifierUri())) {
//				try {
//					List<String> redirects = sectorRedirects.get(newTaxeUserInfo.getSectorIdentifierUri());
//
//					if (newTaxeUserInfo.getRegisteredRedirectUri() != null) {
//						for (String uri : newTaxeUserInfo.getRegisteredRedirectUri()) {
//							if (!redirects.contains(uri)) {
//								throw new IllegalArgumentException("Requested Redirect URI " + uri + " is not listed at sector identifier " + redirects);
//							}
//						}
//					}
//				} catch (UncheckedExecutionException ue) {
//					throw new IllegalArgumentException("Unable to load sector identifier URI: " + newTaxeUserInfo.getSectorIdentifierUri());
//				} catch (ExecutionException e) {
//					throw new IllegalArgumentException("Unable to load sector identifier URI: " + newTaxeUserInfo.getSectorIdentifierUri());
//				}
//			}

//			// make sure a client doesn't get any special system scopes
//			newTaxeUserInfo.setScope(scopeService.removeRestrictedScopes(newTaxeUserInfo.getScope()));
                    newTaxeUserInfo.setId(oldTaxeUserInfo.getId());
                    
                    return taxeUserInfoRepository.save(newTaxeUserInfo);
		}
		throw new IllegalArgumentException("Neither old client or new client can be null!");
	}

}
