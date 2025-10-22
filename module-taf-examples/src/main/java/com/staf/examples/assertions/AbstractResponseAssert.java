package com.staf.examples.assertions;

import com.staf.examples.api.service.impl.Response;
import org.assertj.core.api.AbstractAssert;

import javax.annotation.Nullable;

public abstract class AbstractResponseAssert<S extends AbstractResponseAssert<S, A>, A>
        extends AbstractAssert<S, Response<A>> {

    protected AbstractResponseAssert(final Response<A> actual,
                                         final Class<? extends AbstractResponseAssert<S, A>> clazz) {
        super(actual, clazz);
    }

    @Nullable
    protected A getActualBody() {
        return actual.getBody();
    }

    public S hasBody() {
        objects.assertNotNull(info, getActualBody());
        return myself;
    }

    public S hasStatusCode(final int expected) {
        isNotNull();
        objects.assertEqual(info, actual.getCode(), expected);
        return myself;
    }

    public S hasStatus(final String expected) {
        isNotNull();
        objects.assertEqual(info, actual.getStatus(), expected);
        return myself;
    }
}
