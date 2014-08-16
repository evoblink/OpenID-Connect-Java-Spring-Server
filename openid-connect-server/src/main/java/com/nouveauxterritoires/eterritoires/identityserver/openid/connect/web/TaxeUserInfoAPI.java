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
package com.nouveauxterritoires.eterritoires.identityserver.openid.connect.web;

import com.google.common.base.Strings;
import java.lang.reflect.Type;
import java.util.Collection;

import org.mitre.jose.JWEAlgorithmEmbed;
import org.mitre.jose.JWEEncryptionMethodEmbed;
import org.mitre.jose.JWSAlgorithmEmbed;
import org.mitre.openid.connect.service.UserInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.model.TaxeUserInfo;
import com.nouveauxterritoires.eterritoires.identityserver.openid.connect.service.TaxeUserInfoService;

/**
 * @author Michael Jett <mjett@mitre.org>
 */

@Controller
@RequestMapping("/api/taxeusers")
@PreAuthorize("hasRole('ROLE_USER')")
public class TaxeUserInfoAPI {

	@Autowired
	private TaxeUserInfoService taxeUserInfoService;

	@Autowired
	private UserInfoService userInfoService;

	private JsonParser parser = new JsonParser();

	private Gson gson = new GsonBuilder()
	.serializeNulls()
	.registerTypeAdapter(JWSAlgorithmEmbed.class, new JsonDeserializer<JWSAlgorithmEmbed>() {
		@Override
		public JWSAlgorithmEmbed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (json.isJsonPrimitive()) {
				return JWSAlgorithmEmbed.getForAlgorithmName(json.getAsString());
			} else {
				return null;
			}
		}
	})
	.registerTypeAdapter(JWEAlgorithmEmbed.class, new JsonDeserializer<JWEAlgorithmEmbed>() {
		@Override
		public JWEAlgorithmEmbed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (json.isJsonPrimitive()) {
				return JWEAlgorithmEmbed.getForAlgorithmName(json.getAsString());
			} else {
				return null;
			}
		}
	})
	.registerTypeAdapter(JWEEncryptionMethodEmbed.class, new JsonDeserializer<JWEEncryptionMethodEmbed>() {
		@Override
		public JWEEncryptionMethodEmbed deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			if (json.isJsonPrimitive()) {
				return JWEEncryptionMethodEmbed.getForAlgorithmName(json.getAsString());
			} else {
				return null;
			}
		}
	})
	.setDateFormat("dd/MM/yyyy'T'HH:mm:ssZ")
	.create();

	private static Logger logger = LoggerFactory.getLogger(TaxeUserInfoAPI.class);

	/**
	 * Get a list of all taxeUserInfos
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.GET, produces = "application/json")
	public String apiGetAllTaxeUserInfos(Model model, Authentication auth) {

		Collection<TaxeUserInfo> taxeUserInfos = taxeUserInfoService.getAllUsers();
		model.addAttribute("entity", taxeUserInfos);

		if (isAdmin(auth)) {
			return "taxeUserInfoViewAdmins";
		} else {
			return "taxeUserInfoViewUsers";
		}
	}

	/**
	 * Create a new taxeUserInfo
	 * @param json
	 * @param m
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public String apiAddTaxeUserInfo(@RequestBody String jsonString, Model m, Authentication auth) {

		JsonObject json = null;
		TaxeUserInfo taxeUserInfo = null;

		try {
			json = parser.parse(jsonString).getAsJsonObject();
			taxeUserInfo = gson.fromJson(json, TaxeUserInfo.class);
		}
		catch (JsonSyntaxException e) {
			logger.error("apiAddTaxeUserInfo failed due to JsonSyntaxException", e);
			m.addAttribute("code", HttpStatus.BAD_REQUEST);
			m.addAttribute("errorMessage", "Could not save new taxeUserInfo. The server encountered a JSON syntax exception. Contact a system administrator for assistance.");
			return "jsonErrorView";
		} catch (IllegalStateException e) {
			logger.error("apiAddTaxeUserInfo failed due to IllegalStateException", e);
			m.addAttribute("code", HttpStatus.BAD_REQUEST);
			m.addAttribute("errorMessage", "Could not save new taxeUserInfo. The server encountered an IllegalStateException. Refresh and try again - if the problem persists, contact a system administrator for assistance.");
			return "jsonErrorView";
		}

		// if they leave the taxeUserInfo identifier empty, force it to be generated
		if (Strings.isNullOrEmpty(taxeUserInfo.getSub())) {
			taxeUserInfo = taxeUserInfoService.generateTaxeUserInfoSub(taxeUserInfo);
		}		
		
//		if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_BASIC) 
//				|| taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_POST) 
//				|| taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_JWT)) {
//			
//			// if they've asked for us to generate a taxeUserInfo secret (or they left it blank but require one), do so here
//			if (json.has("generateClientSecret") && json.get("generateClientSecret").getAsBoolean() 
//					|| Strings.isNullOrEmpty(taxeUserInfo.getClientSecret())) {
//				taxeUserInfo = taxeUserInfoService.generateClientSecret(taxeUserInfo);
//			}
//
//		} else if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.PRIVATE_KEY)) {
//
//			if (Strings.isNullOrEmpty(taxeUserInfo.getJwksUri())) {
//				logger.error("tried to create taxeUserInfo with private key auth but no private key");
//				m.addAttribute("code", HttpStatus.BAD_REQUEST);
//				m.addAttribute("errorMessage", "Can not create a taxeUserInfo with private key authentication without registering a key via the JWS Set URI.");
//				return "jsonErrorView";
//			}
//			
//			// otherwise we shouldn't have a secret for this taxeUserInfo
//			taxeUserInfo.setClientSecret(null);
//			
//		} else if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.NONE)) {
//			// we shouldn't have a secret for this taxeUserInfo
//			
//			taxeUserInfo.setClientSecret(null);
//			
//		} else {
//			
//			logger.error("unknown auth method");
//			m.addAttribute("code", HttpStatus.BAD_REQUEST);
//			m.addAttribute("errorMessage", "Unknown auth method requested");
//			return "jsonErrorView";
//			
//			
//		}

//		taxeUserInfo.setDynamicallyRegistered(false);

		TaxeUserInfo newTaxeUserInfo = taxeUserInfoService.save(taxeUserInfo);
		m.addAttribute("entity", newTaxeUserInfo);

		if (isAdmin(auth)) {
			return "taxeUserInfoViewAdmins";
		} else {
			return "taxeUserInfoViewUsers";
		}
	}

	/**
	 * Update an existing taxeUserInfo
	 * @param id
	 * @param jsonString
	 * @param m
	 * @param principal
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/{id}", method = RequestMethod.PUT, consumes = "application/json", produces = "application/json")
	public String apiUpdateTaxeUserInfo(@PathVariable("id") Long id, @RequestBody String jsonString, Model m, Authentication auth) {

		JsonObject json = null;
		TaxeUserInfo taxeUserInfo = null;

		try {
			// parse the taxeUserInfo passed in (from JSON) and fetch the old taxeUserInfo from the store
			json = parser.parse(jsonString).getAsJsonObject();
			taxeUserInfo = gson.fromJson(json, TaxeUserInfo.class);
		}
		catch (JsonSyntaxException e) {
			logger.error("apiUpdateTaxeUserInfo failed due to JsonSyntaxException", e);
			m.addAttribute("code", HttpStatus.BAD_REQUEST);
			m.addAttribute("errorMessage", "Could not update taxeUserInfo. The server encountered a JSON syntax exception. Contact a system administrator for assistance.");
			return "jsonErrorView";
		} catch (IllegalStateException e) {
			logger.error("apiUpdateTaxeUserInfo failed due to IllegalStateException", e);
			m.addAttribute("code", HttpStatus.BAD_REQUEST);
			m.addAttribute("errorMessage", "Could not update taxeUserInfo. The server encountered an IllegalStateException. Refresh and try again - if the problem persists, contact a system administrator for assistance.");
			return "jsonErrorView";
		}

		TaxeUserInfo oldTaxeUserInfo = taxeUserInfoService.getById(id);

		if (oldTaxeUserInfo == null) {
			logger.error("apiUpdateTaxeUserInfo failed; taxeUserInfo with id " + id + " could not be found.");
			m.addAttribute("code", HttpStatus.NOT_FOUND);
			m.addAttribute("errorMessage", "Could not update taxeUserInfo. The requested taxeUserInfo with id " + id + "could not be found.");
			return "jsonErrorView";
		}

		// if they leave the taxeUserInfo identifier empty, force it to be generated
		if (Strings.isNullOrEmpty(taxeUserInfo.getSub())) {
			taxeUserInfo = taxeUserInfoService.generateTaxeUserInfoSub(taxeUserInfo);
		}
//
//		if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_BASIC) 
//				|| taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_POST) 
//				|| taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.SECRET_JWT)) {
//			
//			// if they've asked for us to generate a taxeUserInfo secret (or they left it blank but require one), do so here
//			if (json.has("generateClientSecret") && json.get("generateClientSecret").getAsBoolean() 
//					|| Strings.isNullOrEmpty(taxeUserInfo.getClientSecret())) {
//				taxeUserInfo = taxeUserInfoService.generateClientSecret(taxeUserInfo);
//			}
//
//		} else if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.PRIVATE_KEY)) {
//
//			if (Strings.isNullOrEmpty(taxeUserInfo.getJwksUri())) {
//				logger.error("tried to create taxeUserInfo with private key auth but no private key");
//				m.addAttribute("code", HttpStatus.BAD_REQUEST);
//				m.addAttribute("errorMessage", "Can not create a taxeUserInfo with private key authentication without registering a key via the JWS Set URI.");
//				return "jsonErrorView";
//			}
//			
//			// otherwise we shouldn't have a secret for this taxeUserInfo
//			taxeUserInfo.setClientSecret(null);
//			
//		} else if (taxeUserInfo.getTokenEndpointAuthMethod().equals(AuthMethod.NONE)) {
//			// we shouldn't have a secret for this taxeUserInfo
//			
//			taxeUserInfo.setClientSecret(null);
//			
//		} else {
//			
//			logger.error("unknown auth method");
//			m.addAttribute("code", HttpStatus.BAD_REQUEST);
//			m.addAttribute("errorMessage", "Unknown auth method requested");
//			return "jsonErrorView";
//			
//			
//		}

		TaxeUserInfo newTaxeUserInfo = taxeUserInfoService.updateTaxeUserInfo(oldTaxeUserInfo, taxeUserInfo);
		m.addAttribute("entity", newTaxeUserInfo);

		if (isAdmin(auth)) {
			return "taxeUserInfoViewAdmins";
		} else {
			return "taxeUserInfoViewUsers";
		}
	}

	/**
	 * Delete a taxeUserInfo
	 * @param id
	 * @param modelAndView
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public String apiDeleteTaxeUserInfo(@PathVariable("id") Long id, ModelAndView modelAndView) {

		TaxeUserInfo taxeUserInfo = taxeUserInfoService.getById(id);

		if (taxeUserInfo == null) {
			logger.error("apiDeleteTaxeUserInfo failed; taxeUserInfo with id " + id + " could not be found.");
			modelAndView.getModelMap().put("code", HttpStatus.NOT_FOUND);
			modelAndView.getModelMap().put("errorMessage", "Could not delete taxeUserInfo. The requested taxeUserInfo with id " + id + "could not be found.");
			return "jsonErrorView";
		} else {
			modelAndView.getModelMap().put("code", HttpStatus.OK);
			taxeUserInfoService.remove(taxeUserInfo);
		}

		return "httpCodeView";
	}
        
        
	/**
	 * Get an individual taxeUserInfo
	 * @param sub
	 * @param modelAndView
	 * @return
	 */
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces = "application/json")
	public String apiShowTaxeUserInfo(@PathVariable("id") Long id, Model model, Authentication auth) {

		TaxeUserInfo taxeUserInfo = taxeUserInfoService.getById(id);

		if (taxeUserInfo == null) {
			logger.error("apiShowTaxeUserInfo failed; taxeUserInfo with id " + id + " could not be found.");
			model.addAttribute("code", HttpStatus.NOT_FOUND);
			model.addAttribute("errorMessage", "The requested taxeUserInfo with id " + id + " could not be found.");
			return "jsonErrorView";
		}

		model.addAttribute("entity", taxeUserInfo);

		if (isAdmin(auth)) {
			return "taxeUserInfoViewAdmins";
		} else {
			return "taxeUserInfoViewUsers";
		}
	}

//
//	/**
//	 * Get an individual taxeUserInfo
//	 * @param sub
//	 * @param modelAndView
//	 * @return
//	 */
//	@RequestMapping(value="/{sub}", method=RequestMethod.GET, produces = "application/json")
//	public String apiShowTaxeUserInfo(@PathVariable("sub") String sub, Model model, Authentication auth) {
//
//		TaxeUserInfo taxeUserInfo = taxeUserInfoService.getBySubject(sub);
//
//		if (taxeUserInfo == null) {
//			logger.error("apiShowTaxeUserInfo failed; taxeUserInfo with sub " + sub + " could not be found.");
//			model.addAttribute("code", HttpStatus.NOT_FOUND);
//			model.addAttribute("errorMessage", "The requested taxeUserInfo with sub " + sub + " could not be found.");
//			return "jsonErrorView";
//		}
//
//		model.addAttribute("entity", taxeUserInfo);
//
//		if (isAdmin(auth)) {
//			return "taxeUserInfoViewAdmins";
//		} else {
//			return "taxeUserInfoViewUsers";
//		}
//	}

	/**
	 * Check to see if the given auth object has ROLE_ADMIN assigned to it or not
	 * @param auth
	 * @return
	 */
	private boolean isAdmin(Authentication auth) {
		for (GrantedAuthority grantedAuthority : auth.getAuthorities()) {
			if (grantedAuthority.getAuthority().equals("ROLE_ADMIN")) {
				return true;
			}
		}
		return false;
	}
}
