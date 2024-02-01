package ee.lhv.homework.service;

public interface CrudSupport<E, D, T> {

    default E get(T id) {
        throw new UnsupportedOperationException("get");
    }

    default E insert(D data) {
        throw new UnsupportedOperationException("insert");
    }

    default E update(T id, D data) {
        throw new UnsupportedOperationException("update");
    }

    default void delete(T id) {
        throw new UnsupportedOperationException("delete");
    }

}
