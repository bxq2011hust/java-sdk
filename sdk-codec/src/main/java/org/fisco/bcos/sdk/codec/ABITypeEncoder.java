package org.fisco.bcos.sdk.codec;

<<<<<<< Updated upstream:sdk-abi/src/main/java/org/fisco/bcos/sdk/abi/TypeEncoder.java
import static org.fisco.bcos.sdk.abi.datatypes.Type.MAX_BYTE_LENGTH;
=======
import org.fisco.bcos.sdk.codec.datatypes.*;
>>>>>>> Stashed changes:sdk-codec/src/main/java/org/fisco/bcos/sdk/codec/ABITypeEncoder.java

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
<<<<<<< Updated upstream:sdk-abi/src/main/java/org/fisco/bcos/sdk/abi/TypeEncoder.java
import org.fisco.bcos.sdk.abi.datatypes.*;
=======

import static org.fisco.bcos.sdk.codec.datatypes.Type.MAX_BYTE_LENGTH;
>>>>>>> Stashed changes:sdk-codec/src/main/java/org/fisco/bcos/sdk/codec/ABITypeEncoder.java

/**
 * Ethereum Contract Application Binary Interface (ABI) encoding for types. Further details are
 * available <a href= "https://github.com/ethereum/wiki/wiki/Ethereum-Contract-ABI">here</a>.
 */
public class ABITypeEncoder implements TypeEncoder {

<<<<<<< Updated upstream:sdk-abi/src/main/java/org/fisco/bcos/sdk/abi/TypeEncoder.java
    private TypeEncoder() {}
=======
    public ABITypeEncoder() {
    }
>>>>>>> Stashed changes:sdk-codec/src/main/java/org/fisco/bcos/sdk/codec/ABITypeEncoder.java

    @Override
    public byte[] encode(Type parameter) {
        if (parameter instanceof NumericType) {
            return this.encodeNumeric(((NumericType) parameter));
        } else if (parameter instanceof Address) {
            return this.encodeAddress((Address) parameter);
        } else if (parameter instanceof Bool) {
            return this.encodeBool((Bool) parameter);
        } else if (parameter instanceof Bytes) {
            return this.encodeBytes((Bytes) parameter);
        } else if (parameter instanceof DynamicBytes) {
            return this.encodeDynamicBytes((DynamicBytes) parameter);
        } else if (parameter instanceof Utf8String) {
            return this.encodeString((Utf8String) parameter);
        } else if (parameter instanceof StaticArray) {
            return this.encodeArrayValues((StaticArray) parameter);
        } else if (parameter instanceof DynamicArray) {
            return this.encodeDynamicArray((DynamicArray) parameter);
        } else {
            throw new UnsupportedOperationException(
                    "Type cannot be encoded: " + parameter.getClass());
        }
    }

    @Override
    public byte[] encodeAddress(Address address) {
        return this.encodeNumeric(address.toUint160());
    }

    @Override
    public byte[] encodeNumeric(NumericType numericType) {
        byte[] rawValue = this.toByteArray(numericType);
        byte paddingValue = this.getPaddingValue(numericType);
        byte[] paddedRawValue = new byte[MAX_BYTE_LENGTH];
        if (paddingValue != 0) {
            for (int i = 0; i < paddedRawValue.length; i++) {
                paddedRawValue[i] = paddingValue;
            }
        }

        System.arraycopy(
                rawValue, 0, paddedRawValue, MAX_BYTE_LENGTH - rawValue.length, rawValue.length);
        return paddedRawValue;
    }

    private byte getPaddingValue(NumericType numericType) {
        if (numericType.getValue().signum() == -1) {
            return (byte) 0xff;
        } else {
            return 0;
        }
    }

    private byte[] toByteArray(NumericType numericType) {
        BigInteger value = numericType.getValue();
        if (numericType instanceof Ufixed || numericType instanceof Uint) {
            if (value.bitLength() == Type.MAX_BIT_LENGTH) {
                // As BigInteger is signed, if we have a 256 bit value, the resultant
                // byte array will contain a sign byte in it's MSB, which we should
                // ignore for this unsigned integer type.
                byte[] byteArray = new byte[MAX_BYTE_LENGTH];
                System.arraycopy(value.toByteArray(), 1, byteArray, 0, MAX_BYTE_LENGTH);
                return byteArray;
            }
        }
        return value.toByteArray();
    }

    @Override
    public byte[] encodeBool(Bool value) {
        byte[] rawValue = new byte[MAX_BYTE_LENGTH];
        if (value.getValue()) {
            rawValue[rawValue.length - 1] = 1;
        }
        return rawValue;
    }

    @Override
    public byte[] encodeBytes(BytesType bytesType) {
        byte[] value = bytesType.getValue();
        int length = value.length;
        int mod = length % MAX_BYTE_LENGTH;

        byte[] dest;
        if (mod != 0) {
            int padding = MAX_BYTE_LENGTH - mod;
            dest = new byte[length + padding];
            System.arraycopy(value, 0, dest, 0, length);
        } else {
            dest = value;
        }
        return dest;
    }

    @Override
    public byte[] encodeDynamicBytes(DynamicBytes dynamicBytes) {
        int size = dynamicBytes.getValue().length;
        byte[] encodedLength = this.encode(new Uint(BigInteger.valueOf(size)));
        byte[] encodedValue = this.encodeBytes(dynamicBytes);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            byteArrayOutputStream.write(encodedLength);
            byteArrayOutputStream.write(encodedValue);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public byte[] encodeString(Utf8String string) {
        byte[] utfEncoded = string.getValue().getBytes(StandardCharsets.UTF_8);
        return this.encodeDynamicBytes(new DynamicBytes(utfEncoded));
    }

    @Override
    public <T extends Type> byte[] encodeArrayValues(Array<T> value) {

        ByteArrayOutputStream encodedOffset = new ByteArrayOutputStream();
        ByteArrayOutputStream encodedValue = new ByteArrayOutputStream();

        int offset = value.getValue().size() * MAX_BYTE_LENGTH;

        try {

            for (Type type : value.getValue()) {
                byte[] r = this.encode(type);
                encodedValue.write(r);
                if (type.dynamicType()) {
                    encodedOffset.write(this.encode(new Uint(BigInteger.valueOf(offset))));
                    offset += (r.length >> 1);
                }
            }
            encodedOffset.write(encodedValue.toByteArray());
            return encodedOffset.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T extends Type> byte[] encodeDynamicArray(DynamicArray<T> value) {

        ByteArrayOutputStream encodedSize = new ByteArrayOutputStream();
        ByteArrayOutputStream encodedOffset = new ByteArrayOutputStream();
        ByteArrayOutputStream encodedValue = new ByteArrayOutputStream();
        try {
            encodedSize.write(this.encode(new Uint(BigInteger.valueOf(value.getValue().size()))));
            int offset = value.getValue().size() * MAX_BYTE_LENGTH;
            for (Type type : value.getValue()) {
                byte[] r = this.encode(type);
                encodedValue.write(r);
                if (type.dynamicType()) {
                    encodedOffset.write(this.encode(new Uint(BigInteger.valueOf(offset))));
                    offset += (r.length >> 1);
                }
            }
            encodedSize.write(encodedOffset.toByteArray());
            encodedSize.write(encodedValue.toByteArray());
            return encodedSize.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
