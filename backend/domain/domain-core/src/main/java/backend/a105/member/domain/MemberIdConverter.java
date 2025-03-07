package backend.a105.member.domain;

import backend.a105.auth.MemberId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MemberIdConverter implements AttributeConverter<MemberId, Long> {

    @Override
    public Long convertToDatabaseColumn(MemberId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public MemberId convertToEntityAttribute(Long dbData) {
        return dbData != null ? new MemberId(dbData) : null;
    }
}
