package sg.backend.entity;

import lombok.Getter;

@Getter
public enum Category {
    A0010("캐릭터·굿즈"),
    A0020("홈·리빙"),
    A0030("사진"),
    A0040("게임"),
    A0050("키즈"),
    A0060("도서·전자책"),
    A0070("여행"),
    A0080("만화·웹툰"),
    A0090("스포츠·아웃도어"),
    A0100("테크·가전"),
    A0110("자동차"),
    A0120("패션"),
    A0130("아트"),
    A0140("소셜"),
    A0150("영화·음악"),
    A0160("반려동물"),
    A0170("디자인");

    private final String message;

    Category(String message) {
        this.message = message;
    }

}
