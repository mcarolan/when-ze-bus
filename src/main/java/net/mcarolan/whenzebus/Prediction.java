package net.mcarolan.whenzebus;

import net.mcarolan.whenzebus.api.client.Response;
import net.mcarolan.whenzebus.api.field.Fields;

import org.joda.time.DateTime;

import com.google.common.base.Function;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;
import com.google.common.collect.ComparisonChain;

import java.util.Comparator;

public class Prediction {
	
	private final String lineName;
	private final String destinationText;
	private final DateTime estimatedTime;
	private final DateTime expiryTime;

	public Prediction(String lineName, String destinationText, DateTime estimatedTime, DateTime expiryTime) {
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

	public static Comparator<Prediction> comparator = new Comparator<Prediction>() {
		@Override
		public int compare(Prediction lhs, Prediction rhs) {
			return ComparisonChain.start().compare(lhs.getEstimatedTime(), rhs.getEstimatedTime()).result();
		}
	};

	public static Function<Response, Prediction> builder = new Function<Response, Prediction>() {
		@Override
		public Prediction apply(Response response) {
			return new Prediction(Fields.LineName.extract(response),
					Fields.DestinationText.extract(response),
					Fields.EstimatedTime.extract(response),
					Fields.ExpireTime.extract(response));
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
		Prediction other = (Prediction) obj;

		return Objects.equal(destinationText, other.destinationText) &&
				Objects.equal(estimatedTime, other.estimatedTime) &&
				Objects.equal(expiryTime, other.expiryTime) &&
				Objects.equal(lineName, other.lineName);
	}
}
