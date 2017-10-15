package com.example.basics;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import java.util.Locale;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class CountryHandler {

    public Mono<ServerResponse> getCountry(ServerRequest request) {
        final String code = request.pathVariable("code");
        // TODO: look for Accept-Language header
        // TODO: search locales for country
        final Country country = new LocalizedCountry(Locale.US, Locale.getDefault());

        Mono<Country> info = Mono.just(country);
        return info.flatMap(c -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(c)));
    }

    private class LocalizedCountry implements Country {
    	private Locale wrapped;
    	private java.util.Currency currency;

    	private Locale localized;

    	public LocalizedCountry(Locale wrapped, Locale localized) {
    		this.wrapped = wrapped;
            this.currency = java.util.Currency.getInstance(wrapped);
    		this.localized = localized;
    	}

    	@Override
    	public String getCode() {
    		return wrapped.getCountry();
    	}

        @Override
        public String getISO3Code() {
        	return wrapped.getISO3Country();
        }

        @Override
        public String getName() {
            return wrapped.getDisplayCountry(localized);
        }

        public Currency getCurrency() {
            return new Currency() {

                @Override
                public String getCode() {
                    return currency.getCurrencyCode();
                }

                @Override
                public String getNumericCode() {
                    return String.format("%03d", currency.getNumericCode());
                }

                @Override
                public String getName() {
                    return currency.getDisplayName(localized);
                }

                @Override
                public String getSymbol() {
                    return currency.getSymbol(localized);
                }

                @Override
                public int getDigits() {
                    return currency.getDefaultFractionDigits();
                }
            };
        }
    }
}
