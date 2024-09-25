package sg.backend.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum CategoryUtil {

    A0010("캐릭터·굿즈", Arrays.asList("A0010", "B0010", "B0020", "B0030", "B0040", "B0050", "B0060")),

    A0020("홈·리빙", Arrays.asList("A0020", "B0070", "B0080", "B0090", "B0100", "B0110", "B0120", "B0130", "B0140")),

    A0030("사진", Arrays.asList("A0030", "B0150", "B0160", "B0170")),

    A0040("게임", Arrays.asList("A0040", "B0180", "B0190")),

    A0050("키즈", Arrays.asList("A0050", "B0200", "B0210", "B0220", "B0230", "B0240", "B0250")),

    A0060("도서·전자책", Arrays.asList("A0060", "B0260", "B0270", "B0280", "B0290", "B0300", "B0310", "B0320", "B0330", "B0340")),

    A0070("여행", Arrays.asList("A0070", "B0350", "B0360", "B0370", "B0380", "B0390")),

    A0080("만화·웹툰", Arrays.asList("A0080", "B0400", "B0410", "B0420")),

    A0090("스포츠·아웃도어", Arrays.asList("A0090", "B0430", "B0440", "B0450", "B0460", "B0470", "B0480", "B0490", "B0500", "B0510", "B0520")),

    A0100("테크·가전", Arrays.asList("A0100", "B0530", "B0540", "B0550", "B0560", "B0570", "B0580", "B0590", "B0600", "B0610")),

    A0110("자동차", Arrays.asList("A0110", "B0620", "B0630", "B0640", "B0650", "B0660", "B0670")),

    A0120("패션", Arrays.asList("A0120", "B0680", "B0690", "B0700", "B0710", "B0720", "B0730", "B0740", "B0750", "B0760")),

    A0130("아트", Arrays.asList("A0130", "B0770", "B0780", "B0790", "B0800", "B0810")),

    A0140("소셜", Arrays.asList("A0140", "B0820", "B0830", "B0840")),

    A0150("영화·음악", Arrays.asList("A0150", "B0850", "B0860", "B0870", "B0880", "B0890", "B0900")),

    A0160("반려동물", Arrays.asList("A0160", "B0910", "B0920", "B0930", "B0940")),

    A0170("디자인", Arrays.asList("A0170", "B0950", "B0960", "B0970", "B0980", "B0990", "B1000", "B1010", "B1020", "B1030", "B1040"));

    private final String message;
    private final List<String> categories;

    CategoryUtil(String message, List<String> categories) {
        this.message = message;
        this.categories = categories;
    }
}
