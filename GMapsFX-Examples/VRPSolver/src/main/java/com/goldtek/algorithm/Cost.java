package com.goldtek.algorithm;

public class Cost {
    private final RelationKey mKey;
    private final double mDistance;
    
    public Cost(int fromID, int toID, double dis) {
        mKey = RelationKey.newKey(String.valueOf(fromID), String.valueOf(toID));
        mDistance = dis;
    }
    
    public Cost(String from, String to, double dis) {
        mKey = RelationKey.newKey(from, to);
        mDistance = dis;
    }
    
    public RelationKey getKey() { return mKey; }
    
    public double getCost() { return mDistance; }
}
