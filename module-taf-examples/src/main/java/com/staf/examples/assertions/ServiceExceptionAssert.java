package com.staf.examples.assertions;

import com.staf.examples.api.model.cards.ErrorBody;
import com.staf.examples.api.service.impl.ServiceException;
import org.assertj.core.api.AbstractAssert;

public class ServiceExceptionAssert extends AbstractAssert<ServiceExceptionAssert, ServiceException> {

    protected ServiceExceptionAssert(final ServiceException actual) {
        super(actual, ServiceExceptionAssert.class);
    }

    public ServiceExceptionAssert hasStatusCode(final int expected) {
        isNotNull();
        objects.assertEqual(info, actual.getCode(), expected);
        return myself;
    }

    public ServiceExceptionAssert hasStatus(final String expected) {
        isNotNull();
        objects.assertEqual(info, actual.getStatus(), expected);
        return myself;
    }

    public ServiceExceptionAssert hasErrorBody(final ErrorBody expected) {
        isNotNull();
        objects.assertEqual(info, actual.getErrorBody(), expected);
        return myself;
    }

    public ServiceExceptionAssert doesNotHaveErrorBody() {
        isNotNull();
        objects.assertNull(info, actual.getErrorBody());
        return myself;
    }
}
