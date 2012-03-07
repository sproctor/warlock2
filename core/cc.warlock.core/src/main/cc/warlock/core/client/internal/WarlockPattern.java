package cc.warlock.core.client.internal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import cc.warlock.core.client.IWarlockPattern;

public class WarlockPattern implements IWarlockPattern {

	protected Pattern pattern;
	
	protected String text;
	protected boolean literal = false;
	protected boolean fullWord = true;
	protected boolean caseSensitive = false;
	protected boolean updateDeferred = true;
	
	public WarlockPattern() { }
	
	public WarlockPattern(String text) {
		this.text = text;
	}
	
	public WarlockPattern(String text, boolean literal, boolean caseSensitive, boolean fullWord) {
		this.text = text;
		this.literal = literal;
		this.caseSensitive = caseSensitive;
		this.fullWord = fullWord;
	}
	
	protected void update() throws PatternSyntaxException {
		if (text != null && text.length() > 0) {
			String s = this.text;
			int flags = 0;
			if (literal) {
				s = Pattern.quote(s);
			}
			if (!caseSensitive) {
				flags |= Pattern.CASE_INSENSITIVE;
			}
			if (fullWord) {
				s = "(^|\\b|\\s|\\p{Punct})" + s + "($|\\b|\\s|\\p{Punct})";
			}
			
			pattern = Pattern.compile(s, flags);
		} else {
			pattern = null;
		}
		updateDeferred = false;
	}
	
	public Pattern getPattern() throws PatternSyntaxException{
		if (updateDeferred)
			update();
		return pattern;
	}

	public boolean matches(String text) {
		Matcher m = getPattern().matcher(text);
		return m.matches();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) throws PatternSyntaxException {
		updateDeferred = true;
		this.text = text;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public void setCaseSensitive (boolean caseSensitive)
	{
		updateDeferred = true;
		this.caseSensitive = caseSensitive;
	}

	public boolean isLiteral() {
		return literal;
	}

	public void setLiteral (boolean literal) throws PatternSyntaxException
	{
		updateDeferred = true;
		this.literal = literal;
	}

	public boolean isFullWordMatch() {
		return fullWord;
	}

	public void setFullWordMatch (boolean fullWordMatch)
	{
		updateDeferred = true;
		this.fullWord = fullWordMatch;
	}

}
