package com.example.ggj_be.domain.enums;

public enum Bank {
    KEB_HANA("KB국민은행", "001"),
    KDB("기업은행", "004"),
    NH("농협은행", "011"),
    IBK("산업은행", "052"),
    SUHYEOP("수협은행", "045"),
    SHINHAN("신한은행", "002"),
    WOORI("우리은행", "003"),
    POST("우체국", "071"),
    HANABANK("하나은행", "081"),
    SINGHANG("한국씨티은행", "027"),
    SC_CEIL("SC제일은행", "022"),
    KAKAO_BANK("카카오뱅크", "090"),
    K_BANK("케이뱅크", "089"),
    TOSS_BANK("토스뱅크", "093"),
    GYEONGNAM("경남은행", "039"),
    GWANGJU("광주은행", "034"),
    IM_BANK("아이엠뱅크", "094"),
    BUSAN("부산은행", "032"),
    JEONBUK("전북은행", "037"),
    JEJU("제주은행", "040"),
    SAVINGS("저축은행", "086"),
    FOREST("산림조합", "053"),
    SAEMAUL("새마을금고", "070"),
    SHINHYEOP("신협", "052");

    private final String name;
    private final String code;

    Bank(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public static Bank fromCode(String code) {
        for (Bank bank : Bank.values()) {
            if (bank.getCode().equals(code)) {
                return bank;
            }
        }
        throw new IllegalArgumentException("Invalid bank code: " + code);
    }
}
