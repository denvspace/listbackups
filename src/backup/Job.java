package backup;

public class Job {
	private String id;
	private String type;
	private String state;
	private String operationStatus;
	private String policy;
	private String shedule;
	private String client;
	private String startTime;
	private String endTime;
	private String volume;
	private String countFiles;

	public Job(String id, String type, String state, String operationStatus, String policy, String shedule, String client,
			String startTime, String endTime, String volume, String countFiles) {
		super();
		this.id = id;
		this.type = type;
		this.state = state;
		this.operationStatus = operationStatus;
		this.policy = policy;
		this.shedule = shedule;
		this.client = client;
		this.startTime = startTime;
		this.endTime = endTime;
		this.volume = volume;
		this.countFiles = countFiles;
	}

	public String getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
	public String getState() {
		return state;
	}

	public String getOperationStatus() {
		return operationStatus;
	}

	public String getPolicy() {
		return policy;
	}

	public String getShedule() {
		return shedule;
	}

	public String getClient() {
		return client;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getVolume() {
		return volume;
	}

	public String getCountFiles() {
		return countFiles;
	}

}
