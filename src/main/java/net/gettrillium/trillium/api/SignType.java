package net.gettrillium.trillium.api;

public enum SignType {

    HEAL("Heal"),
    FEED("Feed"),
    WARP("Warp");

    private String sign;

    SignType(String sign) {
        this.sign = sign;
    }

    public String getSignType() {
        return sign;
    }
}
