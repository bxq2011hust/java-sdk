package org.fisco.bcos.sdk.codec;

import org.fisco.bcos.sdk.codec.datatypes.*;

public interface TypeEncoder {
    default byte[] encode(Type parameter) {
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

    byte[] encodeBool(Bool value);

    byte[] encodeBytes(BytesType bytesType);

    byte[] encodeDynamicBytes(DynamicBytes dynamicBytes);

    byte[] encodeString(Utf8String string);

    <T extends Type> byte[] encodeArrayValues(Array<T> value);

    <T extends Type> byte[] encodeDynamicArray(DynamicArray<T> value);

    default byte[] encodeAddress(Address address) {
        return this.encodeNumeric(address.toUint160());
    }

    byte[] encodeNumeric(NumericType numericType);

}
