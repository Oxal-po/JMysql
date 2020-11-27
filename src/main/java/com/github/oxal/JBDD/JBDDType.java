package com.github.oxal.JBDD;

import java.util.Optional;

public interface JBDDType {

    String getName();
    Optional<String> getJBDDTypeString(Object o);
    Optional<? extends JBDDType> getJBDDType(Object o);
}
