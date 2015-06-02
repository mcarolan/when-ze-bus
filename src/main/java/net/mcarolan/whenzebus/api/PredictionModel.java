package net.mcarolan.whenzebus.api;

import net.mcarolan.whenzebus.api.field.Fields;

import org.joda.time.DateTime;

import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import java.util.Comparator;

public class PredictionModel {
	
	private final String lineName;
	private final String destinationText;
	private final DateTime estimatedTime;
	private final DateTime expiryTime;

	public PredictionModel(String lineName, String destinationText, DateTime estimatedTime, DateTime expiryTime) {
		this.lineName = lineName;
		this.destinationText = destinationText;
		this.estimatedTime = estimatedTime;
		this.expiryTime = expiryTime;
	}

	public static PredictionModel fromResponse(Response response) {
		return new PredictionModel(Fields.LineName.extract(response),
							Fields.DestinationText.extract(response),
							Fields.EstimatedTime.extract(response),
							Fields.ExpireTime.extract(response));
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

	public static Comparator<PredictionModel> comparator = new Comparator<PredictionModel>() {
		@Override
		public int compare(PredictionModel lhs, PredictionModel rhs) {
			return ComparisonChain.start().compare(lhs.getEstimatedTime(), rhs.getEstimatedTime()).result();
		}
	};

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.addValue(lineName)
				.addValue(destinationText)
				.addValue(estimatedTime)
				.addValue(expiryTime)
				.toString();
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(destinationText, estimatedTime, expiryTime, lineName);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PredictionModel other = (PredictionModel) obj;

		return Objects.equal(destinationText, other.destinationText) &&
				Objects.equal(estimatedTime, other.estimatedTime) &&
				Objects.equal(expiryTime, other.expiryTime) &&
				Objects.equal(lineName, other.lineName);
	}
}
