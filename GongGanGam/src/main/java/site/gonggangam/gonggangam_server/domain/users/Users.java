package site.gonggangam.gonggangam_server.domain.users;

import lombok.*;
import site.gonggangam.gonggangam_server.domain.BaseTimeEntity;
import site.gonggangam.gonggangam_server.domain.users.types.ProviderType;
import site.gonggangam.gonggangam_server.domain.users.types.GenderType;
import site.gonggangam.gonggangam_server.domain.users.types.Role;
import site.gonggangam.gonggangam_server.domain.users.types.UserStatus;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
@Entity
public class Users extends BaseTimeEntity {

    @Id
    @Column(name = "USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;



    @Column(columnDefinition = "VARCHAR(45)", length = 45, nullable = false)
    private String nickname;

    @Column(name = "BIRTH_YEAR", nullable = false)
    private Integer birthYear;

    @Convert(converter = GenderType.Converter.class)
    @Column(columnDefinition = "CHAR(1)", length = 1, nullable = false)
    private GenderType genderType;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String profImg;

    @Column(name = "EMAIL", columnDefinition = "VARCHAR(50)", length = 50, nullable = false, unique = true)
    private String email;

    @Convert(converter = ProviderType.Converter.class)
    @Column(name = "PROVIDER", columnDefinition = "CHAR(10)", length = 10, nullable = false)
    private ProviderType provider;

    @Column(name = "DEVICE_TOKEN", columnDefinition = "TEXT", nullable = true)
    private String deviceToken;

    @Convert(converter = Role.Converter.class)
    @Column(name = "ROLE", columnDefinition = "CHAR(1) DEFAULT 'U'", length = 1, nullable = false)
    private Role role;

    @Convert(converter = UserStatus.Converter.class)
    @Column(name = "USER_STATUS", columnDefinition = "CHAR(1) DEFAULT 'N'", length = 1, nullable = false)
    private UserStatus userStatus;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, optional = false)
    @PrimaryKeyJoinColumn
    private UserSettings settings;

    @Builder
    public Users(String nickname, Integer birthYear, GenderType genderType, String profImg, String email, ProviderType provider, String deviceToken, Role role, UserStatus userStatus, UserSettings settings) {
        this.nickname = nickname;
        this.birthYear = birthYear;
        this.genderType = genderType;
        this.profImg = profImg;
        this.email = email;
        this.provider = provider;
        this.deviceToken = deviceToken;
        this.role = role;
        this.userStatus = userStatus;
        this.settings = settings;
    }

    public void update(String nickname, int birthYear, GenderType genderType) {
        this.nickname = nickname;
        this.birthYear = birthYear;
        this.genderType = genderType;
    }
}
