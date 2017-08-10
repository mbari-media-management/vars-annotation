package org.mbari.m3.vars.annotation.mediaplayers;

import javafx.util.Pair;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.events.MediaChangedEvent;
import org.mbari.m3.vars.annotation.mediaplayers.sharktopoda.Sharktopoda;
import org.mbari.m3.vars.annotation.mediaplayers.sharktopoda.SharktopodaPaneController;
import org.mbari.m3.vars.annotation.model.Media;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoIO;
import org.mbari.vcr4j.VideoState;
import org.mbari.vcr4j.decorators.SchedulerVideoIO;
import org.mbari.vcr4j.decorators.StatusDecorator;
import org.mbari.vcr4j.decorators.VCRSyncDecorator;
import org.mbari.vcr4j.sharktopoda.SharktopodaError;
import org.mbari.vcr4j.sharktopoda.SharktopodaState;
import org.mbari.vcr4j.sharktopoda.SharktopodaVideoIO;
import org.mbari.vcr4j.sharktopoda.commands.OpenCmd;
import org.mbari.vcr4j.sharktopoda.decorators.FauxTimecodeDecorator;

import java.net.SocketException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * @author Brian Schlining
 * @since 2017-08-07T10:50:00
 */
public class MediaPlayers {

    private final UIToolBox toolBox;
    private Sharktopoda sharktopoda = new Sharktopoda();

    public MediaPlayers(UIToolBox toolBox) {
        this.toolBox = toolBox;
        toolBox.getEventBus()
                .toObserverable()
                .ofType(MediaChangedEvent.class)
                .subscribe(e -> open(e.get()));
    }

    private CompletableFuture<MediaPlayer<VideoState, VideoError>> open(Media media) {
        String u = media.getUri().toString();
        if (u.startsWith("urn:tid")) {
            // handle tape
        }
        else if (u.startsWith("http")) {
            // handle file via sharktopoda

            // TODO handlers should be register so that user can select his/her preferred
            // handler in preferences
            try {
                Pair<Integer, Integer> portNumbers = SharktopodaPaneController.getPortNumbers();


                // TODO finish videoIO wiring See vars Sharktopoda
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    private void openSharktopoda(Media media) {
        Pair<Integer, Integer> portNumbers = SharktopodaPaneController.getPortNumbers();
        sharktopoda.open(media, portNumbers.getKey(), portNumbers.getValue())
                .thenAccept(mediaPlayer -> {

                });
    }
}