package com.ecommercetest.test.security.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.Map;

@Getter
@Setter
public class GithubUserInfo {
    private String login;
    private Integer id;

    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("avatar_url")
    private String avatarUrl;

    @JsonProperty("gravatar_id")
    private String gravatarId;

    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("followers_url")
    private String followersUrl;

    @JsonProperty("following_url")
    private String followingUrl;

    @JsonProperty("gists_url")
    private String gistsUrl;

    @JsonProperty("starred_url")
    private String starredUrl;

    @JsonProperty("subscriptions_url")
    private String subscriptionsUrl;

    @JsonProperty("organizations_url")
    private String organizationsUrl;

    @JsonProperty("repos_url")
    private String reposUrl;

    @JsonProperty("events_url")
    private String eventsUrl;

    @JsonProperty("received_events_url")
    private String receivedEventsUrl;

    private String type;

    @JsonProperty("user_view_type")
    private String userViewType;

    @JsonProperty("site_admin")
    private Boolean siteAdmin;

    private String name;
    private String company;
    private String blog;
    private String location;
    private String email;
    private Boolean hireable;
    private String bio;

    @JsonProperty("twitter_username")
    private String twitterUsername;

    @JsonProperty("notification_email")
    private String notificationEmail;

    @JsonProperty("public_repos")
    private Integer publicRepos;

    @JsonProperty("public_gists")
    private Integer publicGists;

    private Integer followers;
    private Integer following;

    @JsonProperty("created_at")
    private Instant createdAt;

    @JsonProperty("updated_at")
    private Instant updatedAt;

    @JsonProperty("private_gists")
    private Integer privateGists;

    @JsonProperty("total_private_repos")
    private Integer totalPrivateRepos;

    @JsonProperty("owned_private_repos")
    private Integer ownedPrivateRepos;

    @JsonProperty("disk_usage")
    private Integer diskUsage;

    private Integer collaborators;

    @JsonProperty("two_factor_authentication")
    private Boolean twoFactorAuthentication;

    private Map<String, Object> plan;
}
