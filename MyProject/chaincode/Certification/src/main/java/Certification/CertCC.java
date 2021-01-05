package Certification;
import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contact;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;


@Contract(
        name = "Get Certified!",
        info = @Info(
                title = "My contract",
                description = "Certificate of origin contract",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html"),
                contact = @Contact(
                        email = "f.carr@example.com",
                        name = "F Carr",
                        url = "https://hyperledger.example.com")))
@Default
public final class CertCC implements ContractInterface {

    private final Genson genson = new Genson();
    
    //common certification issues in the blockchain§
    private enum CertErrors {
        CERTIFICATION_NOT_FOUND,
        CERTIFICATION_ALREADY_EXISTS,
        CERTIFICATION_NOT_VALID
    }
    /**
     * Creates an initial cert on the ledger.
     *
     * @param ctx the transaction context
     */
    @Transaction()
    public void initLedger(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        String[] certData = {
        		"{ \"origin\": \"USA\", \"exporter\": \"Company1\", \"importer\": \"Company2\", \"DateAndtime\": \"here&now\", \"description\": \"Certification of origin to get a us product\", \"Valid by customs:\": \"False\" }",
        		"{ \"origin\": \"Canada\", \"exporter\": \"Company3\", \"importer\": \"Company4\", \"DateAndtime\": \"here&now\", \"description\": \"Certification of origin to get a canadian product\", \"Valid by customs:\": \"True\" }"
        };

        for (int i = 0; i < certData.length; i++) {
            String key = String.format("CERT%d", i);

            Certification cert = genson.deserialize(certData[i], Certification.class);
            String certState = genson.serialize(cert);
            stub.putStringState(key, certState);
        }
    }
   

    

    /**
     * Creates a new cert on the ledger.
     *
     * @param ctx the transaction context
     * @param key the key for the new cert
     * @param origin the origin of the new cert
     * @param exporter the exporter of the new cert
     * @param importer the importer of the new cert
     * @param dateAndTime the dateAndTime of the new cert
     * @param description the description of the new cert
     * @param valid the state of validity of the new cert
     * @return the created Cert
     */
    @Transaction()
    public Certification createCert(final Context ctx, final String key, final String origin, final String exporter,
            final String importer, final String dateAndTime,final String description,final boolean valid) {
        ChaincodeStub stub = ctx.getStub();

        String certState = stub.getStringState(key);
        if (!certState.isEmpty()) {
            String errorMessage = String.format("Cert %s already exists", key);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertErrors.CERTIFICATION_ALREADY_EXISTS.toString());
        }

        Certification cert = new Certification(origin,exporter,importer,dateAndTime,description,valid);
        certState = genson.serialize(cert);
        stub.putStringState(key, certState);

        return cert;
    }

     /**
     * Retrieves a certification with the specified key from the ledger.
     *
     * @param ctx the transaction context
     * @param key the key
     * @return the Cert found on the ledger if there was one
     */
    @Transaction()
    public Certification queryCert(final Context ctx, final String key) {
        ChaincodeStub stub = ctx.getStub();
        String certState = stub.getStringState(key);

        if (certState.isEmpty()) {
            String errorMessage = String.format("Cert %s does not exist", key);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertErrors.CERTIFICATION_NOT_FOUND.toString());
        }

        Certification cert = genson.deserialize(certState, Certification.class);

        return cert;
    }
    
    /**
     * Retrieves all certs from the ledger.
     *
     * @param ctx the transaction context
     * @return array of Certs found on the ledger
     */
    @Transaction()
    public String queryAllCerts(final Context ctx) {
        ChaincodeStub stub = ctx.getStub();

        final String startKey = "CERT1";//start from 1 since 0 is the dummy data
        final String endKey = "CERT99";
        List<CertQueryResult> queryResults = new ArrayList<CertQueryResult>();

        QueryResultsIterator<KeyValue> results = stub.getStateByRange(startKey, endKey);

        for (KeyValue result: results) {
            Certification cert = genson.deserialize(result.getStringValue(), Certification.class);
            queryResults.add(new CertQueryResult(result.getKey(), cert));
        }

        final String response = genson.serialize(queryResults);

        return response;
    }

    /**
     * Validates the certification.
     *
     * @param ctx the transaction context
     * @param key the key
     * @param valid the original state
     * @return the updated Car
     */
    @Transaction()
    public Certification changeValidity (final Context ctx, final boolean valid ,final String key) {
        ChaincodeStub stub = ctx.getStub();

        String certState = stub.getStringState(key);

        if (certState.isEmpty()) {
            String errorMessage = String.format("Cert %s does not exist", key);
            System.err.println(errorMessage);
            throw new ChaincodeException(errorMessage, CertErrors.CERTIFICATION_NOT_FOUND.toString());
        }

        Certification cert = genson.deserialize(certState, Certification.class);

        Certification  newCert = new Certification(cert.getOrigin(), cert.getExporter(), cert.getImporter(),cert.getDateAndTime(),cert.getDescription(),true);
        String newCertState = genson.serialize(newCert);
        stub.putStringState(key, newCertState);

        return newCert;
    }
}

