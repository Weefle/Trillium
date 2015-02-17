package net.gettrillium.trillium.api.serializer;

public abstract class Serializer<T> {
    public abstract T deserialize(String in);

    public abstract String serialize(T in);
}
