package com.goldtek.database;

public class Message {
    private final IDbCallback<?> iCallback;
    private final Action eAction;
    
    public static Message obtain(IDbCallback<?> identity, Action action) {
        return new Message(identity, action);
    }
    
    private Message(IDbCallback<?> identity, Action action) {
        iCallback = identity;
        eAction = action;
    }
    
    public IDbCallback<?> from() { return iCallback; }
    
    public Action getAction() { return eAction; }
}
