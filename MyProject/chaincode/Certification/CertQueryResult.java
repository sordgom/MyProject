package Certification;
import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

/**
 * CertQueryResult structure used for handling result of query
 *
 */
@DataType()
public final class CertQueryResult {
    @Property()
    private final String key;

    @Property()
    private final Certification record;

    public CertQueryResult(@JsonProperty("Key") final String key, @JsonProperty("Record") final Certification record) {
        this.key = key;
        this.record = record;
    }

    public String getKey() {
        return key;
    }

    public Certification getRecord() {
        return record;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        CertQueryResult other = (CertQueryResult) obj;

        Boolean recordsAreEquals = this.getRecord().equals(other.getRecord());
        Boolean keysAreEquals = this.getKey().equals(other.getKey());

        return recordsAreEquals && keysAreEquals;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getKey(), this.getRecord());
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [key=" + key + ", record="
                + record + "]";
    }
}