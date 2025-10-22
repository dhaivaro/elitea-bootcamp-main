package com.staf.examples.demo.api;

import com.staf.examples.api.model.cards.Deck;
import com.staf.examples.api.model.cards.ErrorBody;
import com.staf.examples.api.service.CardsService;
import com.staf.examples.api.service.impl.CardsServiceImpl;
import com.staf.examples.api.service.impl.Response;
import com.staf.examples.api.service.impl.ServiceException;
import org.apache.http.HttpStatus;
import org.apache.http.ReasonPhraseCatalog;
import org.apache.http.impl.EnglishReasonPhraseCatalog;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Locale;

import static com.staf.examples.assertions.ServiceAssertions.assertThat;
import static com.staf.examples.assertions.ServiceAssertions.catchServiceException;

public class DeckTest {

    private CardsService cardsService;
    private ReasonPhraseCatalog reasonPhraseCatalog;

    @BeforeMethod
    public void setup() {
        cardsService = new CardsServiceImpl();
        reasonPhraseCatalog = EnglishReasonPhraseCatalog.INSTANCE;
    }

    @Test
    public void testDeckCreation() {
        Response<Deck> deck = cardsService.brandNewDeck(false);

        assertThat(deck)
                .hasStatusCode(HttpStatus.SC_OK)
                .hasStatus(reasonPhraseCatalog.getReason(HttpStatus.SC_OK, Locale.ENGLISH))
                .hasRemaining(CardsService.DEFAULT_DECK_SIZE);
    }

    @Test
    public void testDeckCreatingWithJokers() {
        Response<Deck> deck = cardsService.brandNewDeck(true);

        assertThat(deck)
                .hasStatusCode(HttpStatus.SC_OK)
                .hasStatus(reasonPhraseCatalog.getReason(HttpStatus.SC_OK, Locale.ENGLISH))
                .hasRemaining(CardsService.DEFAULT_DECK_SIZE_WITH_JOKERS);
    }

    @Test
    public void testDrawCards() {
        Response<Deck> deck = cardsService.brandNewDeck(false);
        assertThat(deck)
                .hasStatusCode(HttpStatus.SC_OK)
                .hasBody();

        int numberOfCards = 5;
        Response<Deck> newDeck = cardsService.drawCards(deck.getBody(), 5);
        assertThat(newDeck)
                .hasRemaining(CardsService.DEFAULT_DECK_SIZE - numberOfCards)
                .hasCards(numberOfCards);
    }

    @Test
    public void testDrawnUnExistedDeck() {
        ServiceException actual = catchServiceException(() -> cardsService.drawCards("error", 1));

        assertThat(actual)
                .isNotNull()
                .hasStatusCode(HttpStatus.SC_NOT_FOUND)
                .hasErrorBody(ErrorBody.builder()
                        .success(false)
                        .error("Deck ID does not exist.")
                        .build());
    }
}