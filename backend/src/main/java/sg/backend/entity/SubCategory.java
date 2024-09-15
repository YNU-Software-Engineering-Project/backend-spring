package sg.backend.entity;

import lombok.Getter;

@Getter
public enum SubCategory {
    // 캐릭터·굿즈
    B0010("애니메이션"),
    B0020("게임"),
    B0030("케이팝"),
    B0040("TV·영화"),
    B0050("브랜드"),
    B0060("크리에이터"),

    // 홈·리빙
    B0070("침실"),
    B0080("욕실"),
    B0090("주방"),
    B0100("청소·세탁"),
    B0110("DIY"),
    B0120("인센스·방향제"),
    B0130("화훼·원예"),
    B0140("홈케어서비스"),

    // 사진
    B0150("사진책"),
    B0160("촬영스튜디오"),
    B0170("사진 NFT"),

    // 게임
    B0180("보드게임"),
    B0190("게임기기"),

    // 키즈
    B0200("의류"),
    B0210("출산·육아용품"),
    B0220("액세서리"),
    B0230("장난감"),
    B0240("교구·문구"),
    B0250("체험"),

    // 도서·전자책
    B0260("부업·수익"),
    B0270("경제·경영"),
    B0280("자기계발"),
    B0290("외국어"),
    B0300("아동"),
    B0310("기술·공학"),
    B0320("종료"),
    B0330("인문·교양"),
    B0340("연애·가정"),

    // 여행
    B0350("국내여행"),
    B0360("해외여행"),
    B0370("호텔·숙박"),
    B0380("액티비티"),
    B0390("여행용품"),

    // 만화·웹툰
    B0400("만화책"),
    B0410("웹툰"),
    B0420("만화 페스티벌"),

    // 스포츠·아웃도어
    B0430("캠핑"),
    B0440("골프"),
    B0450("자전거"),
    B0460("러닝"),
    B0470("테니스"),
    B0480("헬스"),
    B0490("홈트레이닝"),
    B0500("등산"),
    B0510("낚시"),
    B0520("스포츠 레슨"),

    // 테크·가전
    B0530("생활가전"),
    B0540("주방가전"),
    B0550("스마트가전"),
    B0560("DIY"),
    B0570("엔터테인먼트가전"),
    B0580("웨어러블"),
    B0590("주변기기"),
    B0600("로봇"),
    B0610("App·Web"),

    // 자동차
    B0620("관리·세차용품"),
    B0630("전자기기"),
    B0640("실내용품"),
    B0650("바이크"),
    B0660("모빌리티"),
    B0670("렌트"),

    // 패션
    B0680("의류"),
    B0690("패션소품"),
    B0700("주얼리"),
    B0710("가방"),
    B0720("신발"),
    B0730("아이웨어"),
    B0740("언더웨어"),
    B0750("키즈"),
    B0760("럭셔리"),

    // 아트
    B0770("그림"),
    B0780("일러스트"),
    B0790("도예"),
    B0800("전시"),
    B0810("아트 NFT"),

    // 소셜
    B0820("캠페인"),
    B0830("커뮤니티모임"),
    B0840("후원"),

    // 영화·음악
    B0850("영화"),
    B0860("음악"),
    B0870("악기"),
    B0880("댄스"),
    B0890("콘텐츠 NFT"),
    B0900("공연·페스티벌"),

    // 반려동물
    B0910("강아지"),
    B0920("고양이"),
    B0930("파충류"),
    B0940("물고기"),

    // 디자인
    B0950("인테리어"),
    B0960("문구"),
    B0970("그래픽디자인"),
    B0980("템플릿"),
    B0990("공공디자인"),
    B1000("디자인 NFT"),
    B1010("공예"),
    B1020("소품"),
    B1030("뜨개질"),
    B1040("DIY");

    private final String message;

    SubCategory(String message) {
        this.message = message;
    }
}
