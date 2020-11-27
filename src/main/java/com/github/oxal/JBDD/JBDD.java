package com.github.oxal.JBDD;

import java.util.Optional;

public interface JBDD<T> {
    Optional<T> getQuery(String req);
    boolean updateQuery(String req);
    boolean connexion();
    boolean close();
}
