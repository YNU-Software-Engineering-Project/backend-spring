package sg.backend.entity;

import lombok.Getter;

@Getter
public enum Category {
    CHARACTER_GOODS("캐릭터_굿즈"),
    HOME_LIVING("홈_리빙"),
    PHOTOGRAPHY("사진"),
    GAMES("게임"),
    KIDS("키즈"),
    BOOKS_EBOOKS("도서_전자책"),
    TRAVEL("여행"),
    COMICS_WEBTOONS("만화_웹툰"),
    SPORTS_OUTDOOR("스포츠_아웃도어"),
    TECH_ELECTRONICS("테크_가전"),
    AUTOMOTIVE("자동차"),
    FASHION("패션"),
    ART("아트"),
    SOCIAL("소셜"),
    MOVIES_MUSIC("영화_음악"),
    PETS("반려동물"),
    DESIGN("디자인");

    private final String message;

    Category(String message) {
        this.message = message;
    }

}
