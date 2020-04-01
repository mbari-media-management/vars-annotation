package org.mbari.vars.ui.mediaplayers.sharktopoda;


import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.prefs.Preferences;

import com.typesafe.config.Config;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;
import org.mbari.vars.services.model.Media;
import org.mbari.vars.ui.AppConfig;
import org.mbari.vars.ui.Initializer;
import org.mbari.vars.ui.UIToolBox;
import org.mbari.vars.ui.events.MediaChangedEvent;
import org.mbari.vars.ui.messages.ShowNonfatalErrorAlert;
import org.mbari.vars.ui.javafx.prefs.IPrefs;
import org.mbari.vars.ui.util.FXMLUtils;

/**
 * @author Brian Schlining
 * @since 2017-08-08T15:21:00
 */
public class SharktopodaSettingsPaneController implements IPrefs {

    @FXML
    private TextField controlPortTextField;

    @FXML
    private TextField framegrabPortTextField;

    @FXML
    private GridPane root;

    private Preferences prefs = Preferences.userNodeForPackage(getClass());
    public static final String CONTROL_PORT_KEY = "sharktopoda-control-port";
    public static final String FRAMEGRAB_PORT_KEY = "sharktopoda-framegrab-port";

    @FXML
    void initialize() {

        UnaryOperator<TextFormatter.Change> filter = change -> {
            String text = change.getText();
            if (text.matches("[0-9]*")) {
                return change;
            }
            return null;
        };

        TextFormatter<String> textFormatter1 = new TextFormatter<>(filter);
        TextFormatter<String> textFormatter2 = new TextFormatter<>(filter);
        controlPortTextField.setTextFormatter(textFormatter1);
        framegrabPortTextField.setTextFormatter(textFormatter2);

        load();
    }

    /**
     *
     * @return Pair with Sharkopoda control port (key) and Framecapture port (value)
     */
    public static Pair<Integer, Integer> getPortNumbers() {
        Preferences prefs = Preferences.userNodeForPackage(SharktopodaSettingsPaneController.class);
        AppConfig appConfig = Initializer.getToolBox().getAppConfig();
        int dSharkPort = appConfig.getSharktopodaDefaultsControlPort();
        int dFgPort = appConfig.getSharktopodaDefaultsFramegrabPort();

        try {
            int cport = prefs.getInt(CONTROL_PORT_KEY, dSharkPort);
            int fport = prefs.getInt(FRAMEGRAB_PORT_KEY, dFgPort);
            return new Pair<>(cport, fport);
        }
        catch (Exception e) {
            return null;
        }
    }

    public static SharktopodaSettingsPaneController newInstance() {
        ResourceBundle i18n = Initializer.getToolBox().getI18nBundle();
        return FXMLUtils.newInstance(SharktopodaSettingsPaneController.class,
                "/fxml/SharktopodaSettingsPane.fxml",
                i18n);
    }

    public GridPane getRoot() {
        return root;
    }

    @Override
    public void load() {
        AppConfig appConfig = Initializer.getToolBox().getAppConfig();
        int dSharkPort = appConfig.getSharktopodaDefaultsControlPort();
        int dFgPort = appConfig.getSharktopodaDefaultsFramegrabPort();

        int sharkPort = prefs.getInt(CONTROL_PORT_KEY, dSharkPort);
        int fgPort = prefs.getInt(FRAMEGRAB_PORT_KEY, dFgPort);
        controlPortTextField.setText(sharkPort + "");
        framegrabPortTextField.setText(fgPort + "");
    }

    @Override
    public void save() {

        UIToolBox toolBox = Initializer.getToolBox();
        AppConfig appConfig = toolBox.getAppConfig();
        int sharkPort = appConfig.getSharktopodaDefaultsControlPort();
        int fgPort = appConfig.getSharktopodaDefaultsFramegrabPort();
        ResourceBundle i18n = toolBox.getI18nBundle();

        try {
            sharkPort = Integer.parseInt(controlPortTextField.getText());
            fgPort = Integer.parseInt(framegrabPortTextField.getText());
        }
        catch (Exception e) {
            toolBox.getEventBus()
                    .send(new ShowNonfatalErrorAlert(i18n.getString("mediaplayer.sharktopoda.error.title"),
                            i18n.getString("mediaplayer.sharktopoda.error.header"),
                            i18n.getString("mediaplayer.sharktopoda.error.content"),
                            e));
        }
        prefs.putInt(CONTROL_PORT_KEY, sharkPort);
        prefs.putInt(FRAMEGRAB_PORT_KEY, fgPort);

        Media media = toolBox.getData()
                .getMedia();
        if (media != null) {
            toolBox.getEventBus()
                    .send(new MediaChangedEvent(this, media));
        }
    }
}
