package org.mbari.m3.vars.annotation.ui.buttons;

import de.jensd.fx.glyphs.materialicons.MaterialIcon;
import de.jensd.fx.glyphs.materialicons.utils.MaterialIconFactory;
import io.reactivex.Observable;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.text.Text;
import org.mbari.m3.vars.annotation.UIToolBox;
import org.mbari.m3.vars.annotation.commands.CreateAnnotationFromConceptCmd;
import org.mbari.m3.vars.annotation.events.MediaChangedEvent;
import org.mbari.m3.vars.annotation.events.MediaPlayerChangedEvent;
import org.mbari.m3.vars.annotation.events.UserChangedEvent;
import org.mbari.m3.vars.annotation.mediaplayers.MediaPlayer;
import org.mbari.m3.vars.annotation.messages.ClearCacheMsg;
import org.mbari.m3.vars.annotation.model.Media;
import org.mbari.m3.vars.annotation.model.User;
import org.mbari.vcr4j.VideoError;
import org.mbari.vcr4j.VideoState;

/**
 * @author Brian Schlining
 * @since 2017-08-22T15:25:00
 */
public class NewAnnotationBC {
    private final Button button;
    private final UIToolBox toolBox;
    private String defaultConceptName;

    public NewAnnotationBC(Button button, UIToolBox toolBox) {
        this.button = button;
        this.toolBox = toolBox;
        loadDefaultConcept();

        toolBox.getEventBus()
                .toObserverable()
                .ofType(ClearCacheMsg.class)
                .subscribe(m -> loadDefaultConcept());
        init();
    }

    private void loadDefaultConcept() {
        button.setDisable(true);
        defaultConceptName = null;
        toolBox.getServices()
                .getConceptService()
                .findRoot()
                .thenAccept(concept -> {
                    defaultConceptName = concept.getName();
                    checkEnable();
                });
    }

    private void init() {

        button.setTooltip(new Tooltip(toolBox.getI18nBundle().getString("buttons.new")));
        MaterialIconFactory iconFactory = MaterialIconFactory.get();
        Text icon = iconFactory.createIcon(MaterialIcon.FIBER_NEW, "30px");
        button.setText(null);
        button.setGraphic(icon);
        button.setDisable(true);
        button.setOnAction(e -> toolBox.getEventBus()
                .send(new CreateAnnotationFromConceptCmd(defaultConceptName)));

        Observable<Object> observable = toolBox.getEventBus().toObserverable();
        observable.ofType(MediaChangedEvent.class)
                .subscribe(m -> checkEnable());
        observable.ofType((MediaPlayerChangedEvent.class))
                .subscribe(m -> checkEnable());
        observable.ofType(UserChangedEvent.class)
                .subscribe(m -> checkEnable());
    }

    private void checkEnable() {
        MediaPlayer<? extends VideoState, ? extends VideoError> mediaPlayer = toolBox.getMediaPlayer();
        Media media = toolBox.getData().getMedia();
        User user = toolBox.getData().getUser();
        boolean enable = defaultConceptName != null && mediaPlayer != null && media != null && user != null;
        button.setDisable(!enable);
    }


}