package RhythmGame;

public class HitPoint {
	private int time;	//노트를 치는 타이밍
	private String noteKeyName;	//노트를 치는 영역에 해당하는 키
	private String noteType = "short";	//노트의 종류
	private long holding_time;
	
	public HitPoint(int time, String noteKeyName) {
		this.setTime(time);
		this.setNoteKeyName(noteKeyName);
	}
	
	public HitPoint(int time, String noteKeyName, String noteType) {
		this(time, noteKeyName);
		this.setNoteType(noteType);
	}
	
	public HitPoint(int time, String noteKeyName, String noteType, long holding_time) {
		this(time, noteKeyName, noteType);
		this.setHolding_time(holding_time);
	}
	
	public int getTime() {
		return time;
	}
	public void setTime(int time) {
		this.time = time;
	}
	public String getNoteKeyName() {
		return noteKeyName;
	}
	public void setNoteKeyName(String noteKeyName) {
		this.noteKeyName = noteKeyName;
	}

	public String getNoteType() {
		return noteType;
	}

	public void setNoteType(String noteType) {
		this.noteType = noteType;
	}

	public long getHolding_time() {
		return holding_time;
	}

	public void setHolding_time(long holding_time) {
		this.holding_time = holding_time;
	}
	
}
