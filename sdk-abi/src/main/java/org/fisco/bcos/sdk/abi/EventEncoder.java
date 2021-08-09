package org.fisco.bcos.sdk.abi;

import org.fisco.bcos.sdk.codec.Utils;
import org.fisco.bcos.sdk.codec.datatypes.Event;
import org.fisco.bcos.sdk.codec.datatypes.Type;
import org.fisco.bcos.sdk.codec.datatypes.TypeReference;
import org.fisco.bcos.sdk.crypto.CryptoSuite;
import org.fisco.bcos.sdk.utils.Numeric;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Ethereum filter encoding. Further limited details are available <a
 * href="https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI#events">here</a>.
 */
public class EventEncoder {

    private CryptoSuite cryptoSuite;

    public EventEncoder(CryptoSuite cryptoSuite) {
        this.cryptoSuite = cryptoSuite;
    }

    public String encode(Event event) {

        String methodSignature = this.buildMethodSignature(event.getName(), event.getParameters());

        return this.buildEventSignature(methodSignature);
    }

    private <T extends Type> String buildMethodSignature(
            String methodName, List<TypeReference<T>> parameters) {

        StringBuilder result = new StringBuilder();
        result.append(methodName);
        result.append("(");
        String params =
                parameters.stream().map(p -> Utils.getTypeName(p)).collect(Collectors.joining(","));
        result.append(params);
        result.append(")");
        return result.toString();
    }

    public String buildEventSignature(String methodSignature) {
        byte[] input = methodSignature.getBytes();
        byte[] hash = this.cryptoSuite.hash(input);
        return Numeric.toHexString(hash);
    }

    /**
     * @return the cryptoSuite
     */
    public CryptoSuite getCryptoSuite() {
        return this.cryptoSuite;
    }

    /**
     * @param cryptoSuite the cryptoSuite to set
     */
    public void setCryptoSuite(CryptoSuite cryptoSuite) {
        this.cryptoSuite = cryptoSuite;
    }
}
