package org.mbari.vars.services.impl.vampiresquid.v1;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.mbari.vars.services.gson.ByteArrayConverter;
import org.mbari.vars.services.gson.DurationConverter;
import org.mbari.vars.services.gson.TimecodeConverter;
import org.mbari.vars.services.RetrofitServiceFactory;
import org.mbari.vcr4j.time.Timecode;

import javax.inject.Inject;
import javax.inject.Named;
import java.time.Duration;

/**
 * @author Brian Schlining
 * @since 2017-05-27T12:32:00
 */
public class VamWebServiceFactory extends RetrofitServiceFactory {

    @Inject
    public VamWebServiceFactory(@Named("MEDIA_ENDPOINT") String endpoint, Duration timeout) {
        super(endpoint, timeout);
    }

    public Gson getGson() {
        GsonBuilder gsonBuilder = new GsonBuilder()
                .setPrettyPrinting()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                .registerTypeAdapter(Duration.class, new DurationConverter())
                .registerTypeAdapter(Timecode.class, new TimecodeConverter())
                .registerTypeAdapter(byte[].class, new ByteArrayConverter());

        // Register java.time.Instant
        return Converters.registerInstant(gsonBuilder)
                .create();

    }
}
