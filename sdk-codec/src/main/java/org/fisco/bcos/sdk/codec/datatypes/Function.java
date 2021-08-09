package org.fisco.bcos.sdk.codec.datatypes;

import org.fisco.bcos.sdk.codec.Utils;

import java.util.Collections;
import java.util.List;

/**
 * Function type.
 */
public class Function {
    private String name;
    private List<Type> inputParameters;
    private List<TypeReference<Type>> outputParameters;

    public Function(
            String name, List<Type> inputParameters, List<TypeReference<?>> outputParameters) {
        this.name = name;
        this.inputParameters = inputParameters;
        this.outputParameters = Utils.convert(outputParameters);
    }

    public Function() {
        this.name = "";
        this.inputParameters = Collections.<Type>emptyList();
        this.outputParameters = Collections.<TypeReference<Type>>emptyList();
    }

    public String getName() {
        return this.name;
    }

    public List<Type> getInputParameters() {
        return this.inputParameters;
    }

    public List<TypeReference<Type>> getOutputParameters() {
        return this.outputParameters;
    }
}
