package com.algomized.springframework.security.oauth2.provider.token;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.oauth2.common.OAuth2RefreshToken;

/**
 * Entity class for {@link OAuth2RefreshToken} for JPA provider.
 * 
 * @author Poh Kah Kong
 */
@Entity
@Table(name = "oauth_refresh_token")
public class OAuthRefreshToken {
	@Id
	@Column(name = "token_id")
	private String tokenId;
	
	@Column(name = "token")
	private Blob token;	
	
	@Column(name = "authentication")
	private Blob authentication;

	public String getTokenId() {
		return tokenId;
	}

	public void setTokenId(String tokenId) {
		this.tokenId = tokenId;
	}

	public Blob getToken() {
		return token;
	}

	public void setToken(Blob token) {
		this.token = token;
	}	
	
	public Blob getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Blob authentication) {
		this.authentication = authentication;
	}
}