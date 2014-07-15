package cc.warlock.core.util;

public class Pair<A, B> {
	private final A first;
	private final B second;
	
	public Pair(A first, B second) {
		this.first = first;
		this.second = second;
	}
	
	public A first() {
		return first;
	}
	
	public B second() {
		return second;
	}
	
	@Override
	public int hashCode() {
    	int hashFirst = first != null ? first.hashCode() : 0;
    	int hashSecond = second != null ? second.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }
	
	@Override
	public boolean equals(Object other) {
    	if (!(other instanceof Pair<?,?>))
    		return false;
    	
    	Pair<?, ?> otherPair = (Pair<?,?>) other;
    	return 
    			(this.first == otherPair.first ||
    			 (this.first != null && otherPair.first != null &&
    			  this.first.equals(otherPair.first))) &&
    			(this.second == otherPair.second ||
    			 (this.second != null && otherPair.second != null &&
    			  this.second.equals(otherPair.second)));
    }
	
	@Override
    public String toString() {
        return "(" + first + ", " + second + ')';
    }
}
