package com.algomized.springframework.security.oauth2.provider.token;

import java.sql.Blob;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.security.oauth2.common.OAuth2AccessToken;

/**
 * Entity class for {@link OAuth2AccessToken} for JPA provider.
 * 
 * @author Poh Kah Kong
 */
@Entity
@Table(name = "oauth_access_token")
public class OAuthAccessToken {
	@Id
	@Column(name = "token_id")
	private String tokenId;
	
	@Column(name = "token")
	private Blob token;
	
	@Column(name = "authentication_id")
	@NotNull
	@Size(max = 256)	
	private String authenticationId;
	
	@Column(name = "user_name")
	@NotNull
	@Size(max = 256)	
	private String username;
	
	@Column(name = "client_id")
	@NotNull
	@Size(max = 256)	
	private String clientId;
	
	@Column(name = "authentication")
	private Blob authentication;
	
	@Column(name = "refresh_token")
	@NotNull
	@Size(max = 256)	
	private String refreshToken;

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

	public String getAuthenticationId() {
		return authenticationId;
	}

	public void setAuthenticationId(String authenticationId) {
		this.authenticationId = authenticationId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Blob getAuthentication() {
		return authentication;
	}

	public void setAuthentication(Blob authentication) {
		this.authentication = authentication;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
}
