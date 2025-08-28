package org.example.model;

import java.util.Objects;

/** Community member, identified by Telegram chatId. */
public final class Member {
    private final long chatId;
    private final String userName;

    public Member(long chatId, String userName) {
        this.chatId = chatId;
        this.userName = userName == null ? "Anonymous" : userName;
    }

    public long getChatId() { return chatId; }
    public String getUserName() { return userName; }

    @Override public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Member)) return false;
        Member m = (Member) o;
        return chatId == m.chatId;
    }
    @Override public int hashCode() { return Objects.hash(chatId); }

    @Override public String toString() { return userName + " (" + chatId + ")"; }
}
