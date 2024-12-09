package src.client;

import src.User;

public class UserTransmission {
    private String userName;
    private String bio;
    private boolean hasUserBlocked;
    private boolean friendExclusive;

    public UserTransmission(String userName, String bio) {
        this(userName, bio, false, false);
    }

    public UserTransmission(String userName, String bio, boolean hasUserBlocked, boolean friendExclusive) {
        this.userName = userName;
        this.bio = bio;
        this.hasUserBlocked = hasUserBlocked;
        this.friendExclusive = friendExclusive;
    }

    public UserTransmission(String userName) {
        setUser(userName);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public boolean isUserBlocked() {
        return hasUserBlocked;
    }

    public void setUserBlocked(boolean hasUserBlocked) {
        this.hasUserBlocked = hasUserBlocked;
    }

    public boolean isFriendExclusive() {
        return friendExclusive;
    }

    public void setFriendExclusive(boolean friendExclusive) {
        this.friendExclusive = friendExclusive;
    }

    public void setUser(UserTransmission user) {
        this.userName = user.getUserName();
        this.bio = user.getBio();
        this.hasUserBlocked = user.isUserBlocked();
        this.friendExclusive = user.isFriendExclusive();
    }

    public void setUser(String user) throws RuntimeException{
        String[] data = user.split("/");
        this.userName = data[0];
        this.bio = data[1];
        this.hasUserBlocked = Boolean.parseBoolean(data[2]);
        this.friendExclusive = Boolean.parseBoolean(data[2]);
    }

    public static UserTransmission convertToUser(String user) {
        return new UserTransmission(user);

    }

    @Override
    public String toString() {
        return this.userName + "/" + this.bio + "/" + this.hasUserBlocked + "/" + this.friendExclusive;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof String) {
            String thing = (String) o;
            String[] data = thing.split("/");
            return data[0].equals(this.userName);
        }
        else if(o instanceof UserTransmission) {
            UserTransmission thing = (UserTransmission) o;
            return thing.equals(this.userName);
        }
        return false;
    }
}
