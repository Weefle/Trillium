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

    public int getRequiredLines() {
        if (sign.equalsIgnoreCase("Warp")) {
            return 1;
        } else {
            return 0;
        }
    }
}
