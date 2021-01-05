package Certification;

import java.util.Objects;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

import com.owlike.genson.annotation.JsonProperty;

@DataType()
public final class Certification {

    @Property()
    private final String origin;

    @Property()
    private final String exporter;

    @Property()
    private final String importer;

    @Property()
    private final String dateAndTime; 

    @Property()
    private final String description; 

    @Property()
    private  boolean valid; 

    public String getOrigin() {
        return origin;
    }

    public String getExporter() {
        return exporter;
    }

    public String getImporter() {
        return importer;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }

    public String getDescription() {
        return description;
    }

    public boolean isValid() {
        return valid;
    }
    public String getValidity() {
        return valid?"is valid":"it's not valid";
    }

    public Certification(@JsonProperty("origin") final String origin, @JsonProperty("exporter") final String exporter,
            @JsonProperty("importer") final String importer, @JsonProperty("dateAndTime") final String dateAndTime, @JsonProperty("description") final String description, @JsonProperty("Valid by customs:") final boolean valid) {
        this.origin=origin;
        this.exporter=exporter;
        this.importer=importer;
        this.dateAndTime=dateAndTime;
        this.description=description;
        this.valid=valid;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if ((obj == null) || (getClass() != obj.getClass())) {
            return false;
        }

        Certification other = (Certification) obj;

        return Objects.deepEquals(new String[] {getOrigin(), getExporter(), getImporter(), getDateAndTime(),getDescription(),getValidity()},
                new String[] {other.getOrigin(), other.getExporter(), other.getImporter(), other.getDateAndTime(), other.getDescription(), other.getValidity()});
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOrigin(), getExporter(), getImporter(), getDateAndTime(),getDescription(),getValidity());
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode()) + " [Origin=" + origin + ", exporter="
                + exporter + ", importer=" + importer + ", DateAndtime=" + dateAndTime + ", Description:"+description+", Valditiy:"+isValid()+"]";
    }

    
}