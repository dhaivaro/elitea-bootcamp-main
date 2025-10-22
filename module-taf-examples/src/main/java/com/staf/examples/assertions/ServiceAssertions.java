package com.staf.examples.assertions;

import com.staf.examples.api.model.cards.Deck;
import com.staf.examples.api.service.impl.Response;
import com.staf.examples.api.service.impl.ServiceException;
import lombok.experimental.UtilityClass;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.ThrowableAssert;

@UtilityClass
public class ServiceAssertions extends Assertions {

    public static DeckAssert assertThat(final Response<Deck> actual) {
        return new DeckAssert(actual);
    }

    public static ServiceExceptionAssert assertThat(final ServiceException actual) {
        return new ServiceExceptionAssert(actual);
    }

    public static ServiceException catchServiceException(final ThrowableAssert.ThrowingCallable callable) {
        return catchThrowableOfType(callable, ServiceException.class);
    }
}
