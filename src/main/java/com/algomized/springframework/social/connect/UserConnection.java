package com.algomized.springframework.social.connect;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.social.connect.ConnectionData;;

/**
 * Entity class for {@link ConnectionData} for JPA provider.
 * 
 * @author Poh Kah Kong
 */
@Entity
@Table(name = "UserConnection")
public class UserConnection {
	@SuppressWarnings("serial")
	@Embeddable
	protected class UserConnectionId implements Serializable {
		@Column(name = "userId")
		@NotNull
		@Size(max = 255)	
		private String userId;

		@Column(name = "providerId")
		@NotNull
		@Size(max = 255)	
		private String providerId;
		
		@Column(name = "providerUserId")
		@NotNull
		@Size(max = 255)		
		private String providerUserId;
	}	
	
	@EmbeddedId
	private UserConnectionId id;
	
	@Column(name = "rank")
	@NotNull	
	private int rank;
	
	@Column(name = "displayName")
	@Size(max = 255)	
	private String displayName;

	@Column(name = "profileUrl")
	@Size(max = 512)	
	private String profileUrl;

	@Column(name = "imageUrl")
	@Size(max = 512)	
	private String imageUrl;
	
	@Column(name = "accessToken")
	@NotNull
	@Size(max = 255)		
	private String accessToken;

	@Column(name = "secret")
	@Size(max = 255)		
	private String secret;

	@Column(name = "refreshToken")
	@Size(max = 255)			
	private String refreshToken;
	
	@Column(name = "expireTime")
	private long expireTime;

	public String getUserId() {
		return id.userId;
	}

	public void setUserId(String userId) {
		id.userId = userId;
	}

	public String getProviderId() {
		return id.providerId;
	}

	public void setProviderId(String providerId) {
		id.providerId = providerId;
	}

	public String getProviderUserId() {
		return id.providerUserId;
	}

	public void setProviderUserId(String providerUserId) {
		id.providerUserId = providerUserId;
	}

	public int getRank() {
		return rank;
	}

	public void setRank(int rank) {
		this.rank = rank;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getProfileUrl() {
		return profileUrl;
	}

	public void setProfileUrl(String profileUrl) {
		this.profileUrl = profileUrl;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSecret() {
		return secret;
	}

	public void setSecret(String secret) {
		this.secret = secret;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public long getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(long expireTime) {
		this.expireTime = expireTime;
	}
}