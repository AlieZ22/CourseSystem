package basis;

/**
 * 选课信息
 */
public class SelectInfo {
	private long sID;
	private long cID;
	private String date;
	
	public SelectInfo(long sID, long cID, String date) {
		super();
		this.sID = sID;
		this.cID = cID;
		if(date.length()>16){
			date=date.substring(0, 16);
		}
		else{
			while(date.length()<16){
				date=date+'\u0000';
			}
		}
		this.date = date;
	}

	public long getsID() {
		return sID;
	}

	public void setsID(long sID) {
		this.sID = sID;
	}

	public long getcID() {
		return cID;
	}

	public void setcID(long cID) {
		this.cID = cID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "SelectInfo [sID=" + sID + ", cID=" + cID + ", date=" + date + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if(obj.getClass() == SelectInfo.class) {
			SelectInfo si = (SelectInfo)obj;
			return this.sID == si.sID && this.cID == si.cID
					&& this.date.equals(si.date);
		}
		return false;
	} 
	
}
