package shared;

public class Delimiter {
	
	String delim;
	
	public Delimiter(String delim) {
		this.delim = delim;
	}
	
	public Delimiter() {
		this.delim = ";";
	}
	
	public String getDelim() {
		return delim;
	}
 }
