package backend.a105.member.domain;

import backend.a105.type.Email;
import backend.a105.type.MemberId;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Table(name = "member")
@Getter
@NoArgsConstructor(access = PROTECTED)
@Entity
public class Member {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Convert(converter = MemberIdConverter.class)
    private MemberId id;
    @Convert(converter = EmailConverter.class)
    private Email email;
    private String username;
    private String password;

    @Builder
    private Member(final String username, final String password, final Email email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
