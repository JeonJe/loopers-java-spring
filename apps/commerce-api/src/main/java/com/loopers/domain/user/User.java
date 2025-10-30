package com.loopers.domain.user;

import com.loopers.domain.BaseEntity;
import com.loopers.support.error.CoreException;
import com.loopers.support.error.ErrorType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Table;
import java.util.regex.Pattern;
import lombok.Getter;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Getter
public class User extends BaseEntity {

  private static final int MAX_USER_ID_LENGTH = 10;
  // 영문 대소문자 + 숫자만 허용 (특수문자, 공백, 한글 불가)
  private static final Pattern USER_ID_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");
  // 이메일 형식: xxx@yyy.zzz (공백 불가)
  private static final String EMAIL_PATTERN = "^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$";

  @Column(unique = true, nullable = false, length = MAX_USER_ID_LENGTH)
  private String userId;

  @Column(nullable = false)
  private String email;

  @Column(nullable = false)
  private LocalDate birth; //yyyy-MM-dd

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private Gender gender;

  protected User() {
  }

  private User(String userId, String email, LocalDate birth, Gender gender, LocalDate currentDate) {
    String normalizedUserId = userId != null ? userId.trim() : null;
    String normalizedEmail = email != null ? email.toLowerCase().trim() : null;

    validateUserId(normalizedUserId);
    validateEmail(normalizedEmail);
    validateBirth(birth, currentDate);
    validateGender(gender);

    this.userId = normalizedUserId;
    this.email = normalizedEmail;
    this.birth = birth;
    this.gender = gender;
  }

  public static User of(String userId, String email, LocalDate birth, Gender gender, LocalDate currentDate) {
    return new User(userId, email, birth, gender, currentDate);
  }

  private void validateUserId(String userId) {
    if (userId == null || userId.isBlank()) {
      throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 비어있을 수 없습니다.");
    }
    if (userId.length() > MAX_USER_ID_LENGTH) {
      throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 10자 이내여야 합니다.");
    }
    if (!USER_ID_PATTERN.matcher(userId).matches()) {
      throw new CoreException(ErrorType.BAD_REQUEST, "사용자 ID는 영문/숫자만 허용됩니다.");
    }
  }

  private void validateEmail(String email) {
    if (email == null || email.isBlank()) {
      throw new CoreException(ErrorType.BAD_REQUEST, "이메일은 비어있을 수 없습니다.");
    }
    if (!email.matches(EMAIL_PATTERN)) {
      throw new CoreException(ErrorType.BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
    }
  }

  private void validateBirth(LocalDate birth, LocalDate currentDate) {
    if (birth == null) {
      throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 비어있을 수 없습니다.");
    }
    if (birth.isAfter(currentDate)) {
      throw new CoreException(ErrorType.BAD_REQUEST, "생년월일은 미래일 수 없습니다.");
    }
  }

  private void validateGender(Gender gender) {
    if (gender == null) {
      throw new CoreException(ErrorType.BAD_REQUEST, "성별은 비어있을 수 없습니다.");
    }
  }
}
