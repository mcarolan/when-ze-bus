package net.mcarolan.whenzebus;

import org.joda.time.DateTime;

public class PredictionModel {
	
	private final String lineName;
	private final String destinationText;
	private final DateTime estimatedTime;
	private final DateTime expiryTime;
	
	public PredictionModel(String lineName, String destinationText,
			DateTime estimatedTime, DateTime expiryTime) {
		this.lineName = lineName;
		this.destinationText = destinationText;
		this.estimatedTime = estimatedTime;
		this.expiryTime = expiryTime;
	}

	public String getLineName() {
		return lineName;
	}

	public String getDestinationText() {
		return destinationText;
	}

	public DateTime getEstimatedTime() {
		return estimatedTime;
	}

	public DateTime getExpiryTime() {
		return expiryTime;
	}

	@Override
	public String toString() {
		return "PredictionModel [lineName=" + lineName + ", destinationText="
				+ destinationText + ", estimatedTime=" + estimatedTime
				+ ", expiryTime=" + expiryTime + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((destinationText == null) ? 0 : destinationText.hashCode());
		result = prime * result
				+ ((estimatedTime == null) ? 0 : estimatedTime.hashCode());
		result = prime * result
				+ ((expiryTime == null) ? 0 : expiryTime.hashCode());
		result = prime * result
				+ ((lineName == null) ? 0 : lineName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredictionModel other = (PredictionModel) obj;
		if (destinationText == null) {
			if (other.destinationText != null)
				return false;
		} else if (!destinationText.equals(other.destinationText))
			return false;
		if (estimatedTime == null) {
			if (other.estimatedTime != null)
				return false;
		} else if (!estimatedTime.equals(other.estimatedTime))
			return false;
		if (expiryTime == null) {
			if (other.expiryTime != null)
				return false;
		} else if (!expiryTime.equals(other.expiryTime))
			return false;
		if (lineName == null) {
			if (other.lineName != null)
				return false;
		} else if (!lineName.equals(other.lineName))
			return false;
		return true;
	}

}
