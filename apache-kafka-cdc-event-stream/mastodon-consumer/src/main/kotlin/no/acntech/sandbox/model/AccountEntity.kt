package no.acntech.sandbox.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Table(name = "ACCOUNT")
@Entity
class AccountEntity(
        @Id @Column(name = "id", nullable = false, unique = true) val id: String? = null,
        @Column(name = "url", nullable = false) val url: String? = null,
        @Column(name = "avatar", nullable = false) val avatar: String? = null,
        @Column(name = "username", nullable = false) val username: String? = null,
        @Column(name = "account", nullable = false, unique = true) val account: String? = null,
        @Column(name = "display_name", nullable = false) val displayName: String? = null,
        @Column(name = "followers_count", nullable = false) val followersCount: Int? = null,
        @Column(name = "following_count", nullable = false) val followingCount: Int? = null,
        @Column(name = "statuses_count", nullable = false) val statusesCount: Int? = null,
        @Column(name = "created_at", nullable = false) val createdAt: ZonedDateTime? = null,
        @OneToOne(mappedBy = "account") val status: StatusEntity? = null
)
